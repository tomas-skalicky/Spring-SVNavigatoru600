package com.svnavigatoru600.web.users.administration;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.viewmodel.users.ShowAllUsers;
import com.svnavigatoru600.web.url.users.UserAdministrationUrlParts;

/**
 * The controller bound mainly to the <i>users/administration/list.jsp</i> page. For more details about the concepts
 * used here, see the <i>ForgottenPasswordController</i> controller.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListUsersController extends AbstractUserController {

    private static final String COMMAND = "showAllUsersCommand";

    /**
     * Initializes the page with all users.
     */
    @GetMapping(value = UserAdministrationUrlParts.BASE_URL)
    public String initPage(final HttpServletRequest request, final ModelMap model) {

        final ShowAllUsers command = new ShowAllUsers();

        final List<User> users = getUserService().findAllOrdered();
        command.setUsers(users);

        // Sets up all (but necessary) maps.
        command.setLocalizedDeleteQuestions(
                UserService.getLocalizedDeleteQuestions(users, request, getMessageSource()));

        model.addAttribute(ListUsersController.COMMAND, command);
        return PageViews.LIST.getViewName();
    }

    /**
     * Initializes the page with all available users including the one which has been successfully added to the
     * repository recently.
     */
    @GetMapping(value = UserAdministrationUrlParts.CREATED_URL)
    public String initPageAfterCreate(final HttpServletRequest request, final ModelMap model) {
        final String view = initPage(request, model);
        ((ShowAllUsers) model.get(ListUsersController.COMMAND)).setUserCreated(true);
        return view;
    }

    /**
     * Initializes the page with all available users and notifies that one other user has been successfully deleted from
     * the repository.
     */
    @GetMapping(value = UserAdministrationUrlParts.DELETED_URL)
    public String initPageAfterDelete(final HttpServletRequest request, final ModelMap model) {
        final String view = initPage(request, model);
        ((ShowAllUsers) model.get(ListUsersController.COMMAND)).setUserDeleted(true);
        return view;
    }
}
