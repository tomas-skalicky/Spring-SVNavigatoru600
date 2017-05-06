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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.users.AuthorityService;
import com.svnavigatoru600.service.util.AuthorityUtils;
import com.svnavigatoru600.viewmodel.users.AdministrateUserData;
import com.svnavigatoru600.viewmodel.users.validator.NewUserValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.url.users.UserAdministrationUrlParts;

/**
 * The controller bound to the <i>new-user.jsp</i> page.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class NewUserController extends AbstractNewEditUserController {

    @Inject
    private AuthorityService authorityService;

    private Validator validator;

    public void setAuthorityService(final AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * Trivial setter
     */
    @Inject
    @Required
    public void setValidator(final NewUserValidator validator) {
        this.validator = validator;
    }

    /**
     * Initializes the form.
     */
    @GetMapping(value = UserAdministrationUrlParts.NEW_URL)
    public String initForm(final HttpServletRequest request, final ModelMap model) {

        final AdministrateUserData command = new AdministrateUserData();

        final User user = new User();
        user.setEnabled(true);
        command.setUser(user);

        // Collection of authorities is converted to a map.
        command.setNewAuthorities(AuthorityUtils.getDefaultArrayOfCheckIndicators());

        // Sets up all (but necessary) maps.
        command.setRoleCheckboxId(AuthorityService.getRoleCheckboxId());
        command.setLocalizedRoleCheckboxTitles(authorityService.getLocalizedRoleTitles(request, getMessageSource()));

        model.addAttribute(AbstractNewEditUserController.COMMAND, command);
        return PageViews.NEW.getViewName();
    }

    /**
     * If values in the form are OK, new user is stored to a repository. Otherwise, returns back to the form.
     *
     * @return The name of the view which is to be shown.
     */
    @PostMapping(value = UserAdministrationUrlParts.NEW_URL)
    @Transactional
    public String processSubmittedForm(@ModelAttribute(NewUserController.COMMAND) final AdministrateUserData command,
            final BindingResult result, final SessionStatus status, final HttpServletRequest request,
            final ModelMap model) {

        // Sets up all (but necessary) maps.
        command.setRoleCheckboxId(AuthorityService.getRoleCheckboxId());
        final MessageSource messageSource = getMessageSource();
        command.setLocalizedRoleCheckboxTitles(authorityService.getLocalizedRoleTitles(request, messageSource));

        validator.validate(command, result);
        if (result.hasErrors()) {
            return PageViews.NEW.getViewName();
        }

        final User newUser = command.getUser();
        try {
            getUserService().saveAndNotifyUser(newUser, command.getNewPassword(), command.getNewAuthorities(), request,
                    messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, UserAdministrationUrlParts.CREATED_URL);
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(newUser, e);
            result.reject("user-administration.creation-failed-due-to-database-error");
            return PageViews.NEW.getViewName();
        }
    }
}
