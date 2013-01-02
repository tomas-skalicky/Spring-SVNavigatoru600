package com.svnavigatoru600.web.users;

import javax.inject.Inject;

import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.users.validator.UpdateUserDataValidator;
import com.svnavigatoru600.service.util.UserUtils;
import com.svnavigatoru600.viewmodel.users.UpdateUserData;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;
import com.svnavigatoru600.web.Configuration;

/**
 * The controller bound to the <i>user-account.jsp</i> and <i>user-administration.jsp</i> form. For more
 * details about the concepts used here, see the <i>ForgottenPasswordController</i> controller.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditMyAccountController extends AbstractPrivateSectionMetaController {

    private static final String BASE_URL = "/uzivatelsky-ucet/";
    private static final String COMMAND = "updateUserDataCommand";
    private static final String PAGE_VIEW = "editMyUserAccount";

    private UserService userService;
    private Validator validator;

    /**
     * Constructor.
     */
    @Inject
    public EditMyAccountController(UserService userService, UpdateUserDataValidator validator) {
        LogFactory.getLog(this.getClass()).debug("The UserAccountController object created.");
        this.userService = userService;
        this.validator = validator;
    }

    /**
     * Initializes the form.
     */
    @RequestMapping(value = EditMyAccountController.BASE_URL, method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
    public String initForm(ModelMap model) {

        UpdateUserData command = new UpdateUserData();

        final User user = UserUtils.getLoggedUser();
        command.setUser(user);

        model.addAttribute(EditMyAccountController.COMMAND, command);
        return EditMyAccountController.PAGE_VIEW;
    }

    /**
     * Initializes the form after the modified data were successfully saved to a repository.
     */
    @RequestMapping(value = EditMyAccountController.BASE_URL + "ulozeno/", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
    public String initFormAfterSave(final ModelMap model) {
        final String view = this.initForm(model);
        ((UpdateUserData) model.get(EditMyAccountController.COMMAND)).setDataSaved(true);
        return view;
    }

    /**
     * If values in the form are OK, update date of the current {@link User}. Otherwise, returns back to the
     * form.
     * 
     * @return The name of the view which is to be shown.
     */
    @RequestMapping(value = EditMyAccountController.BASE_URL, method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
    @Transactional
    public String processSubmittedForm(
            @ModelAttribute(EditMyAccountController.COMMAND) UpdateUserData command, BindingResult result,
            SessionStatus status, ModelMap model) {

        this.validator.validate(command, result);
        if (result.hasErrors()) {
            return EditMyAccountController.PAGE_VIEW;
        }

        User originalUser = null;
        try {
            // We cannot use the User object from the session, i.e. Users.getLoggedUser() since
            // if the new user's data (e.g. email) were not valid, e.g. because of the occupied email,
            // the old valid user's logging data would be available anymore. They were removed by the new
            // invalid data.
            originalUser = this.userService.findByUsername(UserUtils.getLoggedUser().getUsername());
            this.userService.update(originalUser, command.getUser(), command.getNewPassword());

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE, EditMyAccountController.BASE_URL
                    + "ulozeno/");
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(originalUser, e);
            result.rejectValue("user.email", "email.occupied-by-somebody");
            return EditMyAccountController.PAGE_VIEW;
        }
    }
}
