package com.svnavigatoru600.web.users.administration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.web.Configuration;

@Controller
public class DeleteUserController extends UserController {

    private static final String BASE_URL = "/administrace-uzivatelu/";

    /**
     * Constructor.
     */
    @Autowired
    public DeleteUserController(UserDao userDao, MessageSource messageSource) {
        super(userDao, messageSource);
    }

    @RequestMapping(value = DeleteUserController.BASE_URL + "existujici/{username}/smazat/", method = RequestMethod.GET)
    public String delete(@PathVariable String username, HttpServletRequest request, ModelMap model) {
        try {
            // Deletes the user.
            User user = this.userDao.findByUsername(username);
            this.userDao.delete(user);

            // Notifies the user about the deletion of his account.
            this.sendEmailOnUserDeletion(user, request);

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE, DeleteUserController.BASE_URL
                    + "smazano/");
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(e);
            model.addAttribute("error", "user-administration.deletion-failed-due-to-database-error");
            return PageViews.LIST.getViewName();
        }
    }

    /**
     * Sends an email to the given <code>user</code> that his account has been deleted by the administrator.
     */
    private void sendEmailOnUserDeletion(User user, HttpServletRequest request) {
        String emailAddress = user.getEmail();
        if (!Email.isSpecified(emailAddress)) {
            return;
        }

        String subject = Localization.findLocaleMessage(this.messageSource, request,
                "email.subject.user-deleted");
        Object[] messageParams = new Object[] { user.getLastName(), user.getUsername(), Configuration.DOMAIN };
        String messageText = Localization.findLocaleMessage(this.messageSource, request,
                "email.text.user-deleted", messageParams);

        Email.sendMail(emailAddress, subject, messageText);
    }
}
