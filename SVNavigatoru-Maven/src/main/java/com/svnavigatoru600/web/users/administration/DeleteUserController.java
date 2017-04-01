package com.svnavigatoru600.web.users.administration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.url.users.UserAdministrationUrlParts;
import com.svnavigatoru600.web.AbstractMetaController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteUserController extends AbstractUserController {

    @RequestMapping(value = UserAdministrationUrlParts.EXISTING_URL + "{username}/"
            + CommonUrlParts.DELETE_EXTENSION, method = RequestMethod.GET)
    @Transactional
    public String delete(@PathVariable final String username, final HttpServletRequest request, final ModelMap model) {
        try {
            getUserService().delete(username, request, getMessageSource());

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, UserAdministrationUrlParts.DELETED_URL);
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(e);
            model.addAttribute("error", "user-administration.deletion-failed-due-to-database-error");
            return PageViews.LIST.getViewName();
        }
    }
}
