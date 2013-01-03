package com.svnavigatoru600.web.users.administration;

import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.users.validator.NewUserValidator;
import com.svnavigatoru600.service.util.AuthorityUtils;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Hash;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.viewmodel.users.AdministrateUserData;
import com.svnavigatoru600.web.Configuration;

/**
 * The controller bound to the <i>new-user.jsp</i> page.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class NewUserController extends AbstractNewEditUserController {

    private static final String BASE_URL = "/administrace-uzivatelu/";

    /**
     * Constructor.
     */
    @Inject
    public NewUserController(final UserService userService, final NewUserValidator validator,
            final MessageSource messageSource) {
        super(userService, validator, messageSource);
    }

    /**
     * Initializes the form.
     */
    @RequestMapping(value = NewUserController.BASE_URL + "novy/", method = RequestMethod.GET)
    public String initForm(final HttpServletRequest request, final ModelMap model) {

        final AdministrateUserData command = new AdministrateUserData();

        final User user = new User();
        user.setEnabled(true);
        command.setUser(user);

        // Collection of authorities is converted to a map.
        command.setNewAuthorities(AuthorityUtils.getDefaultArrayOfCheckIndicators());

        // Sets up all (but necessary) maps.
        command.setRoleCheckboxId(this.getRoleCheckboxId());
        command.setLocalizedRoleCheckboxTitles(this.getLocalizedRoleCheckboxTitles(request));

        model.addAttribute(AbstractNewEditUserController.COMMAND, command);
        return PageViews.NEW.getViewName();
    }

    /**
     * If values in the form are OK, new user is stored to a repository. Otherwise, returns back to the form.
     * 
     * @return The name of the view which is to be shown.
     */
    @RequestMapping(value = NewUserController.BASE_URL + "novy/", method = RequestMethod.POST)
    @Transactional
    public String processSubmittedForm(
            @ModelAttribute(NewUserController.COMMAND) AdministrateUserData command,
            final BindingResult result, final SessionStatus status, final HttpServletRequest request,
            final ModelMap model) {

        // Sets up all (but necessary) maps.
        command.setRoleCheckboxId(this.getRoleCheckboxId());
        command.setLocalizedRoleCheckboxTitles(this.getLocalizedRoleCheckboxTitles(request));

        this.getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViews.NEW.getViewName();
        }

        // Updates the data of the new user.
        final User newUser = command.getUser();
        final String newPassword = command.getNewPassword();
        newUser.setPassword(Hash.doSha1Hashing(newPassword));

        final String username = newUser.getUsername();
        final Set<GrantedAuthority> checkedAuthorities = AuthorityUtils.convertIndicatorsToAuthorities(
                command.getNewAuthorities(), username);
        // The role ROLE_REGISTERED_USER is automatically added.
        checkedAuthorities.add(new Authority(username, AuthorityType.ROLE_REGISTERED_USER.name()));
        newUser.setAuthorities(checkedAuthorities);

        // Sets user's email to null if the email is blank. The reason is the
        // UNIQUE DB constraint.
        newUser.setEmailToNullIfBlank();

        try {
            // Stores the data.
            this.getUserService().save(newUser);

            // Notifies the user about its new account.
            if (!StringUtils.isBlank(newUser.getEmail())) {
                this.sendEmailOnUserCreation(newUser, newPassword, request);
            }

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE, NewUserController.BASE_URL + "vytvoreno/");
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(newUser, e);
            result.reject("user-administration.creation-failed-due-to-database-error");
            return PageViews.NEW.getViewName();
        }
    }

    /**
     * Sends an email with the credentials of the <code>newUser</code>. The function is invoked when the
     * {@link User} has been successfully added to the repository by the administrator.
     */
    private void sendEmailOnUserCreation(final User newUser, final String newPassword,
            final HttpServletRequest request) {
        final String emailAddress = newUser.getEmail();
        if (!Email.isSpecified(emailAddress)) {
            return;
        }

        final MessageSource messageSource = this.getMessageSource();
        final String subject = Localization.findLocaleMessage(messageSource, request,
                "email.subject.new-user");
        final Object[] messageParams = new Object[] { newUser.getLastName(), Configuration.DOMAIN,
                newUser.getUsername(), emailAddress, newPassword };
        final String messageText = Localization.findLocaleMessage(messageSource, request,
                "email.text.new-user", messageParams);

        Email.sendMail(emailAddress, subject, messageText);
    }
}
