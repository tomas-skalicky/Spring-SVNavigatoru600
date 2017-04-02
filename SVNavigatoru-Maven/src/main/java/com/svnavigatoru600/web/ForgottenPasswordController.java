package com.svnavigatoru600.web;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.AutoPopulatingList;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.google.common.collect.Lists;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.url.LoginUrlParts;
import com.svnavigatoru600.viewmodel.users.SendNewPassword;
import com.svnavigatoru600.viewmodel.users.validator.SendNewPasswordValidator;

/**
 * The controller bound to the <i>forgotten-password.jsp</i> form.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ForgottenPasswordController extends AbstractMetaController {

    private static final String PAGE_VIEW = "forgottenPassword";

    private final UserService userService;
    private final Validator validator;

    // MessageSource is necessary; otherwise we would not be able to resolve a
    // message which is to be sent via email.
    private MessageSource messageSource;

    @Inject
    public void setMessageSource(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Inject
    public ForgottenPasswordController(final UserService userService, final SendNewPasswordValidator validator) {
        LogFactory.getLog(this.getClass()).debug("The ForgottenPasswordController object created.");
        this.userService = userService;
        this.validator = validator;
    }

    /**
     * Initializes the form.
     */
    @GetMapping(value = LoginUrlParts.FORGOTTEN_PASSWORD_URL)
    public String initForm(final ModelMap model) {

        final SendNewPassword command = new SendNewPassword();

        final User user = new User();
        user.setEmail("@");
        command.setUser(user);

        final List<User> adminsFromDb = userService.findAllAdministrators();
        final List<User> newAdmins = Lists.newArrayList();
        // Excludes me (Tomas Skalicky).
        for (final User admin : adminsFromDb) {
            final boolean isMe = (admin.getEmail() != null) && "skalicky.tomas@gmail.com".equals(admin.getEmail());
            if (!isMe) {
                newAdmins.add(admin);
            }
        }
        // Note that AutoPopulatingList is used here. The reason is that in the
        // form forgotten-password.jsp, we need to bind each administrator back
        // to the list.
        final AutoPopulatingList<User> adminsForModel = new AutoPopulatingList<User>(newAdmins, User.class);
        command.setAdministrators(adminsForModel);

        // ATTENTION: The attribute called "sendNewPasswordCommand" is connected
        // with the commandName of the form forgotten-password.jsp. Moreover, it
        // appears as the first parameter of the
        // ForgottenPasswordController.sendNewPassword method.
        model.addAttribute("sendNewPasswordCommand", command);
        return ForgottenPasswordController.PAGE_VIEW;
    }

    /**
     * If values in the form are OK, generates a new password, stores it to a repository and sends it to the provided
     * email address. If something is wrong, returns back to the form.
     *
     * @return The name of the view which is to be shown.
     */
    @PostMapping(value = LoginUrlParts.SEND_NEW_PASSWORD_URL)
    public String processSubmittedForm(@ModelAttribute("sendNewPasswordCommand") final SendNewPassword command,
            final BindingResult result, final SessionStatus status, final HttpServletRequest request,
            final ModelMap model) {

        validator.validate(command, result);
        if (result.hasErrors()) {
            // ATTENTION: The view name cannot be redirected via the redirection
            // page. The reason is that in that case, the ModelMap is lost (=
            // empty), i.e. errors cannot be displayed.
            return ForgottenPasswordController.PAGE_VIEW;
        }

        try {
            userService.resetPasswordAndNotifyUser(command.getUser().getEmail(), request, messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, LoginUrlParts.BASE_URL);
            // ATTENTION: The view name must be redirected. Otherwise, the user
            // would be able to "resend" the request and his password would be
            // reset once again. This is the counterpart to the following catch.
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final NonTransientDataAccessException e) {
            // Returns back since the email address was wrong.
            LogFactory.getLog(this.getClass()).error(null, e);
            result.rejectValue("user.email", "email.occupied-by-nobody");
            return ForgottenPasswordController.PAGE_VIEW;
        }
    }
}
