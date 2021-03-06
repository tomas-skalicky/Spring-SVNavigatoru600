package com.svnavigatoru600.web.users.administration;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.users.AuthorityService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.util.AuthorityUtils;
import com.svnavigatoru600.viewmodel.users.AdministrateUserData;
import com.svnavigatoru600.viewmodel.users.validator.AdministrateUserDataValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.url.CommonUrlParts;
import com.svnavigatoru600.web.url.users.UserAdministrationUrlParts;

/**
 * The controller bound mainly to the <i>user/administration/edit-user.jsp</i> page.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditUserController extends AbstractNewEditUserController {

    @Inject
    private AuthorityService authorityService;

    private Validator validator;

    /**
     * Trivial setter
     */
    @Inject
    @Required
    public void setValidator(final AdministrateUserDataValidator validator) {
        this.validator = validator;
    }

    /**
     * Initializes the form.
     *
     * @param username
     *            The username of the administrated user.
     */
    @GetMapping(value = UserAdministrationUrlParts.EXISTING_URL + "{username}/")
    public String initForm(@PathVariable final String username, final HttpServletRequest request,
            final ModelMap model) {

        final AdministrateUserData command = new AdministrateUserData();

        final User user = getUserService().findByUsername(username);
        command.setUser(user);

        // Collection of authorities is converted to a map.
        command.setNewAuthorities(AuthorityUtils.getArrayOfCheckIndicators(user.getAuthorities()));

        // Sets up all auxiliary (but necessary) maps.
        command.setRoleCheckboxId(AuthorityService.getRoleCheckboxId());
        command.setLocalizedRoleCheckboxTitles(authorityService.getLocalizedRoleTitles(request, getMessageSource()));

        model.addAttribute(AbstractNewEditUserController.COMMAND, command);
        return PageViews.EDIT.getViewName();
    }

    /**
     * Initializes the form after the modified data were successfully saved to the repository.
     */
    @GetMapping(value = UserAdministrationUrlParts.EXISTING_URL + "{username}/" + CommonUrlParts.SAVED_EXTENSION)
    public String initFormAfterSave(@PathVariable final String username, final HttpServletRequest request,
            final ModelMap model) {
        final String view = initForm(username, request, model);
        ((AdministrateUserData) model.get(AbstractNewEditUserController.COMMAND)).setDataSaved(true);
        return view;
    }

    /**
     * If values in the form are OK, updates date of the given <code>username</code>. Otherwise, returns back to the
     * form.
     *
     * @return The name of the view which is to be shown.
     */
    @PostMapping(value = UserAdministrationUrlParts.EXISTING_URL + "{username}/")
    @Transactional
    public String processSubmittedForm(
            @ModelAttribute(AbstractNewEditUserController.COMMAND) final AdministrateUserData command,
            final BindingResult result, final SessionStatus status, @PathVariable final String username,
            final HttpServletRequest request, final ModelMap model) {

        // Sets up all auxiliary (but necessary) maps.
        command.setRoleCheckboxId(AuthorityService.getRoleCheckboxId());
        final MessageSource messageSource = getMessageSource();
        command.setLocalizedRoleCheckboxTitles(authorityService.getLocalizedRoleTitles(request, messageSource));

        validator.validate(command, result);
        if (result.hasErrors()) {
            return PageViews.EDIT.getViewName();
        }

        final UserService userService = getUserService();
        User originalUser = null;
        try {
            originalUser = userService.findByUsername(username);
            userService.updateAndNotifyUser(originalUser, command.getUser(), command.getNewPassword(),
                    command.getNewAuthorities(), request, messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, String.format("%s%s/%s",
                    UserAdministrationUrlParts.EXISTING_URL, username, CommonUrlParts.SAVED_EXTENSION));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(originalUser, e);
            result.reject("edit.changes-not-saved-due-to-database-error");
            return PageViews.EDIT.getViewName();
        }
    }
}
