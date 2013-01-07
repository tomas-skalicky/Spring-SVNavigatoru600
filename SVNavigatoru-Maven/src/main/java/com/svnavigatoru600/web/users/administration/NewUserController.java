package com.svnavigatoru600.web.users.administration;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.users.AuthorityService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.util.AuthorityUtils;
import com.svnavigatoru600.viewmodel.users.AdministrateUserData;
import com.svnavigatoru600.viewmodel.users.validator.NewUserValidator;
import com.svnavigatoru600.web.AbstractMetaController;

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
    public NewUserController(UserService userService, NewUserValidator validator, MessageSource messageSource) {
        super(userService, validator, messageSource);
    }

    /**
     * Initializes the form.
     */
    @RequestMapping(value = NewUserController.BASE_URL + "novy/", method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, ModelMap model) {

        AdministrateUserData command = new AdministrateUserData();

        User user = new User();
        user.setEnabled(true);
        command.setUser(user);

        // Collection of authorities is converted to a map.
        command.setNewAuthorities(AuthorityUtils.getDefaultArrayOfCheckIndicators());

        // Sets up all (but necessary) maps.
        command.setRoleCheckboxId(AuthorityService.getRoleCheckboxId());
        command.setLocalizedRoleCheckboxTitles(AuthorityService.getLocalizedRoleTitles(request,
                this.getMessageSource()));

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
            @ModelAttribute(NewUserController.COMMAND) AdministrateUserData command, BindingResult result,
            SessionStatus status, HttpServletRequest request, ModelMap model) {

        // Sets up all (but necessary) maps.
        command.setRoleCheckboxId(AuthorityService.getRoleCheckboxId());
        MessageSource messageSource = this.getMessageSource();
        command.setLocalizedRoleCheckboxTitles(AuthorityService
                .getLocalizedRoleTitles(request, messageSource));

        this.getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViews.NEW.getViewName();
        }

        User newUser = command.getUser();
        try {
            this.getUserService().saveAndNotifyUser(newUser, command.getNewPassword(),
                    command.getNewAuthorities(), request, messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, NewUserController.BASE_URL
                    + "vytvoreno/");
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(newUser, e);
            result.reject("user-administration.creation-failed-due-to-database-error");
            return PageViews.NEW.getViewName();
        }
    }
}
