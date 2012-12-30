package com.svnavigatoru600.web.users.administration;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.service.users.validator.AdministrateUserDataValidator;
import com.svnavigatoru600.service.util.AuthorityUtils;
import com.svnavigatoru600.service.util.CheckboxUtils;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Hash;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.viewmodel.users.AdministrateUserData;
import com.svnavigatoru600.web.Configuration;

/**
 * The controller bound mainly to the <i>user/administration/edit-user.jsp</i> page.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditUserController extends NewEditUserController {

    private static final String BASE_URL = "/administrace-uzivatelu/existujici/";

    /**
     * Constructor.
     */
    @Autowired
    public EditUserController(UserDao userDao, AdministrateUserDataValidator validator,
            MessageSource messageSource) {
        super(userDao, validator, messageSource);
    }

    /**
     * Initializes the form.
     * 
     * @param username
     *            The username of the administrated user.
     */
    @RequestMapping(value = EditUserController.BASE_URL + "{username}/", method = RequestMethod.GET)
    public String initForm(@PathVariable String username, HttpServletRequest request, ModelMap model) {

        AdministrateUserData command = new AdministrateUserData();

        User user = this.userDao.findByUsername(username);
        command.setUser(user);

        // Collection of authorities is converted to a map.
        command.setNewAuthorities(AuthorityUtils.getArrayOfCheckIndicators(user.getAuthorities()));

        // Sets up all auxiliary (but necessary) maps.
        command.setRoleCheckboxId(this.getRoleCheckboxId());
        command.setLocalizedRoleCheckboxTitles(this.getLocalizedRoleCheckboxTitles(request));

        model.addAttribute(EditUserController.COMMAND, command);
        return PageViews.EDIT.getViewName();
    }

    /**
     * Initializes the form after the modified data were successfully saved to the repository.
     */
    @RequestMapping(value = EditUserController.BASE_URL + "{username}/ulozeno/", method = RequestMethod.GET)
    public String initFormAfterSave(@PathVariable String username, HttpServletRequest request, ModelMap model) {
        final String view = this.initForm(username, request, model);
        ((AdministrateUserData) model.get(EditUserController.COMMAND)).setDataSaved(true);
        return view;
    }

    /**
     * If values in the form are OK, updates date of the given <code>username</code>. Otherwise, returns back
     * to the form.
     * 
     * @return The name of the view which is to be shown.
     */
    @RequestMapping(value = EditUserController.BASE_URL + "{username}/", method = RequestMethod.POST)
    @Transactional
    public String processSubmittedForm(
            @ModelAttribute(EditUserController.COMMAND) AdministrateUserData command, BindingResult result,
            final SessionStatus status, @PathVariable String username, HttpServletRequest request,
            ModelMap model) {

        // Sets up all auxiliary (but necessary) maps.
        command.setRoleCheckboxId(this.getRoleCheckboxId());
        command.setLocalizedRoleCheckboxTitles(this.getLocalizedRoleCheckboxTitles(request));

        this.validator.validate(command, result);
        if (result.hasErrors()) {
            return PageViews.EDIT.getViewName();
        }

        // Updates the original data.
        final User newUser = command.getUser();
        final User originalUser = this.userDao.findByUsername(username);
        // The default value of the following indicator has to be true.
        // Otherwise, the notification email would be sent to the user.
        boolean arePasswordSame = true;
        final String newPassword = command.getNewPassword();
        final boolean isPasswordUpdated = StringUtils.isNotBlank(newPassword);
        if (isPasswordUpdated) {
            final String newPasswordHash = Hash.doSha1Hashing(newPassword);
            arePasswordSame = newPasswordHash.equals(originalUser.getPassword());
            originalUser.setPassword(newPasswordHash);
        }
        originalUser.setFirstName(newUser.getFirstName());
        originalUser.setLastName(newUser.getLastName());

        final Set<GrantedAuthority> checkedAuthorities = AuthorityUtils.convertIndicatorsToAuthorities(
                command.getNewAuthorities(), username);
        // The role ROLE_REGISTERED_USER is automatically added.
        checkedAuthorities.add(new Authority(username, AuthorityType.ROLE_REGISTERED_USER.name()));
        final boolean areAuthoritiesSame = CheckboxUtils.areSame(
                AuthorityUtils.getArrayOfCheckIndicators(checkedAuthorities),
                AuthorityUtils.getArrayOfCheckIndicators(originalUser.getAuthorities()));
        originalUser.setAuthorities(checkedAuthorities);

        // Sets user's email to null if the email is blank. The reason is the
        // UNIQUE DB constraint.
        originalUser.setEmailToNullIfBlank();

        try {
            // Stores the data.
            this.userDao.update(originalUser);

            // Notifies the user about the change.
            if (!arePasswordSame) {
                this.sendEmailOnPasswordChange(originalUser, newPassword, request);
            }
            if (!areAuthoritiesSame) {
                this.sendEmailOnAuthoritiesChange(originalUser, request);
            }

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE,
                    String.format(EditUserController.BASE_URL + "%s/ulozeno/", username));
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(originalUser, e);
            result.reject("edit.changes-not-saved-due-to-database-error");
            return PageViews.EDIT.getViewName();
        }
    }

    /**
     * Sends an email with the <code>newPassword</code> to the given <code>user</code>. The function is
     * invoked when user's password has been successfully changed by the administrator.
     */
    private void sendEmailOnPasswordChange(User user, String newPassword, HttpServletRequest request) {
        String emailAddress = user.getEmail();
        if (!Email.isSpecified(emailAddress)) {
            return;
        }

        final String subject = Localization.findLocaleMessage(this.messageSource, request,
                "email.subject.password-changed");
        final Object[] messageParams = new Object[] { user.getLastName(), Configuration.DOMAIN, newPassword };
        final String messageText = Localization.findLocaleMessage(this.messageSource, request,
                "email.text.password-changed", messageParams);

        Email.sendMail(emailAddress, subject, messageText);
    }

    /**
     * Sends an email with the new {@link Authority}s of the given <code>user</code>. The function is invoked
     * when user's authorities have been successfully changed by the administrator.
     */
    private void sendEmailOnAuthoritiesChange(User user, HttpServletRequest request) {
        final String emailAddress = user.getEmail();
        if (!Email.isSpecified(emailAddress)) {
            return;
        }

        final String subject = Localization.findLocaleMessage(this.messageSource, request,
                "email.subject.authorities-changed");

        // Converts the new authorities to the String representation.
        final StringBuilder newAuthorities = new StringBuilder();
        if (user.canSeeNews()) {
            if (user.canEditNews()) {
                newAuthorities.append(Localization.findLocaleMessage(this.messageSource, request,
                        "user-roles.member-of-board"));
            } else {
                newAuthorities.append(Localization.findLocaleMessage(this.messageSource, request,
                        "user-roles.member-of-sv"));
            }
        }
        if (user.canSeeUsers()) {
            if (newAuthorities.length() > 0) {
                newAuthorities.append(", ");
            }
            newAuthorities.append(Localization.findLocaleMessage(this.messageSource, request,
                    "user-roles.user-administrator"));
        }
        if (user.getAuthorities().size() == 1) {
            // The user is registered, but has no other authority.
            newAuthorities.append(Localization.findLocaleMessage(this.messageSource, request,
                    "user-roles.no-authority"));
        }
        // End of the conversion.

        final Object[] messageParams = new Object[] { user.getLastName(), Configuration.DOMAIN,
                newAuthorities };
        final String messageText = Localization.findLocaleMessage(this.messageSource, request,
                "email.text.authorities-changed", messageParams);

        Email.sendMail(emailAddress, subject, messageText);
    }
}
