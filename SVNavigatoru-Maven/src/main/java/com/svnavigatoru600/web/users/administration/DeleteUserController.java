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

import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.web.AbstractMetaController;

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
            this.getUserService().delete(username, request, this.getMessageSource());

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, DeleteUserController.BASE_URL
                    + "smazano/");
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(e);
            model.addAttribute("error", "user-administration.deletion-failed-due-to-database-error");
            return PageViews.LIST.getViewName();
        }
    }
}
