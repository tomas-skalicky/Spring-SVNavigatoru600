package com.svnavigatoru600.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.AutoPopulatingList;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.service.users.SendNewPassword;
import com.svnavigatoru600.service.users.SendNewPasswordValidator;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Hash;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.Password;

/**
 * The controller bound to the <i>forgotten-password.jsp</i> form.
 * 
 * @author Tomas Skalicky
 */
@Controller
public class ForgottenPasswordController extends MetaController {

    private static final String PAGE_VIEW = "forgottenPassword";

    private UserDao userDao;
    private Validator validator;

    // MessageSource is necessary; otherwise we would not be able to resolve a
    // message which is to be sent via email.
    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Constructor.
     */
    @Autowired
    public ForgottenPasswordController(UserDao userDao, SendNewPasswordValidator validator) {
        this.logger.debug("The ForgottenPasswordController object created.");
        this.userDao = userDao;
        this.validator = validator;
    }

    /**
     * Initializes the form.
     */
    @RequestMapping(value = "/prihlaseni/zapomenute-heslo/", method = RequestMethod.GET)
    public String initForm(ModelMap model) {

        SendNewPassword command = new SendNewPassword();

        User user = new User();
        user.setEmail("@");
        command.setUser(user);

        List<User> adminsFromDb = this.userDao.findByAuthority(AuthorityType.ROLE_USER_ADMINISTRATOR.name());
        List<User> newAdmins = new ArrayList<User>();
        // Excludes me (Tomas Skalicky).
        for (User admin : adminsFromDb) {
            boolean isMe = (admin.getEmail() != null) && admin.getEmail().equals("skalicky.tomas@gmail.com");
            if (!isMe) {
                newAdmins.add(admin);
            }
        }
        // Note that AutoPopulatingList is used here. The reason is that in the
        // form forgotten-password.jsp, we need to bind each administrator back
        // to the list.
        AutoPopulatingList<User> adminsForModel = new AutoPopulatingList<User>(newAdmins, User.class);
        command.setAdministrators(adminsForModel);

        // ATTENTION: The attribute called "sendNewPasswordCommand" is connected
        // with the commandName of the form forgotten-password.jsp. Moreover, it
        // appears as the first parameter of the
        // ForgottenPasswordController.sendNewPassword method.
        model.addAttribute("sendNewPasswordCommand", command);
        return ForgottenPasswordController.PAGE_VIEW;
    }

    /**
     * If values in the form are OK, generates a new password, stores it to a repository and sends it to the
     * provided email address. If something is wrong, returns back to the form.
     * 
     * @return The name of the view which is to be shown.
     */
    @RequestMapping(value = "/prihlaseni/poslat-nove-heslo/", method = RequestMethod.POST)
    public String processSubmittedForm(@ModelAttribute("sendNewPasswordCommand") SendNewPassword command,
            BindingResult result, SessionStatus status, HttpServletRequest request, ModelMap model) {

        this.validator.validate(command, result);
        if (result.hasErrors()) {
            // ATTENTION: The view name cannot be redirected via the redirection
            // page. The reason is that in that case, the ModelMap is lost (=
            // empty), i.e. errors cannot be displayed.
            return ForgottenPasswordController.PAGE_VIEW;
        }

        try {
            // Checks whether user with the provided email address exists.
            String inputEmail = command.getUser().getEmail();
            User user = this.userDao.findByEmail(inputEmail);
            String newPassword = Password.generateNew();
            user.setPassword(Hash.doSha1Hashing(newPassword));
            this.userDao.update(user);

            // Sends the new password to the user.
            this.sendEmailOnPasswordReset(inputEmail, user.getLastName(), newPassword, request);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE, "/prihlaseni/");
            // ATTENTION: The view name must be redirected. Otherwise, the user
            // would be able to "resend" the request and his password would be
            // reset once again. This is the counterpart to the following catch.
            return Configuration.REDIRECTION_PAGE;

        } catch (NonTransientDataAccessException e) {
            // Returns back since the email address was wrong.
            this.logger.error(null, e);
            result.rejectValue("user.email", "email.occupied-by-nobody");
            return ForgottenPasswordController.PAGE_VIEW;
        }
    }

    /**
     * Sends an email with the <code>newPassword</code> to the given <code>emailAddress</code>. The function
     * is invoked when user's password has been successfully reset.
     */
    private void sendEmailOnPasswordReset(final String emailAddress, final String lastName,
            final String newPassword, final HttpServletRequest request) {
        if (!Email.isSpecified(emailAddress)) {
            return;
        }

        final String subject = Localization.findLocaleMessage(this.messageSource, request,
                "email.subject.password-reset");
        final Object[] messageParams = new Object[] { lastName, Configuration.DOMAIN, newPassword };
        final String messageText = Localization.findLocaleMessage(this.messageSource, request,
                "email.text.password-reset", messageParams);

        Email.sendMail(emailAddress, subject, messageText);
    }
}
