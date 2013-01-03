package com.svnavigatoru600.web.users.administration;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.web.Configuration;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteUserController extends AbstractUserController {

    private static final String BASE_URL = "/administrace-uzivatelu/";

    /**
     * Constructor.
     */
    @Inject
    public DeleteUserController(UserService userService, MessageSource messageSource) {
        super(userService, messageSource);
    }

    @RequestMapping(value = DeleteUserController.BASE_URL + "existujici/{username}/smazat/", method = RequestMethod.GET)
    @Transactional
    public String delete(@PathVariable String username, HttpServletRequest request, ModelMap model) {
        try {
            // Deletes the user.
            final UserService userService = this.getUserService();
            final User user = userService.findByUsername(username);
            userService.delete(user);

            // Notifies the user about the deletion of his account.
            this.sendEmailOnUserDeletion(user, request);

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE, DeleteUserController.BASE_URL
                    + "smazano/");
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(e);
            model.addAttribute("error", "user-administration.deletion-failed-due-to-database-error");
            return PageViews.LIST.getViewName();
        }
    }

    /**
     * Sends an email to the given <code>user</code> that his account has been deleted by the administrator.
     */
    private void sendEmailOnUserDeletion(final User user, final HttpServletRequest request) {
        final String emailAddress = user.getEmail();
        if (!Email.isSpecified(emailAddress)) {
            return;
        }

        final MessageSource messageSource = this.getMessageSource();
        final String subject = Localization.findLocaleMessage(messageSource, request,
                "email.subject.user-deleted");
        final Object[] messageParams = new Object[] { user.getLastName(), user.getUsername(),
                Configuration.DOMAIN };
        final String messageText = Localization.findLocaleMessage(messageSource, request,
                "email.text.user-deleted", messageParams);

        Email.sendMail(emailAddress, subject, messageText);
    }
}
