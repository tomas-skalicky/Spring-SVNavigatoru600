package com.svnavigatoru600.web.users.administration;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.url.users.UserAdministrationUrlParts;
import com.svnavigatoru600.viewmodel.users.ShowAllUsers;

/**
 * The controller bound mainly to the <i>users/administration/list.jsp</i> page. For more details about the
 * concepts used here, see the <i>ForgottenPasswordController</i> controller.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListUsersController extends AbstractUserController {

    private static final String COMMAND = "showAllUsersCommand";

    /**
     * Initializes the page with all users.
     */
    @RequestMapping(value = UserAdministrationUrlParts.BASE_URL, method = RequestMethod.GET)
    public String initPage(HttpServletRequest request, ModelMap model) {

        ShowAllUsers command = new ShowAllUsers();

        List<User> users = getUserService().findAllOrdered();
        command.setUsers(users);

        // Sets up all (but necessary) maps.
        command.setLocalizedDeleteQuestions(UserService.getLocalizedDeleteQuestions(users, request,
                getMessageSource()));

        model.addAttribute(ListUsersController.COMMAND, command);
        return PageViews.LIST.getViewName();
    }

    /**
     * Initializes the page with all available users including the one which has been successfully added to
     * the repository recently.
     */
    @RequestMapping(value = UserAdministrationUrlParts.CREATED_URL, method = RequestMethod.GET)
    public String initPageAfterCreate(HttpServletRequest request, ModelMap model) {
        String view = initPage(request, model);
        ((ShowAllUsers) model.get(ListUsersController.COMMAND)).setUserCreated(true);
        return view;
    }

    /**
     * Initializes the page with all available users and notifies that one other user has been successfully
     * deleted from the repository.
     */
    @RequestMapping(value = UserAdministrationUrlParts.DELETED_URL, method = RequestMethod.GET)
    public String initPageAfterDelete(HttpServletRequest request, ModelMap model) {
        String view = initPage(request, model);
        ((ShowAllUsers) model.get(ListUsersController.COMMAND)).setUserDeleted(true);
        return view;
    }
}
