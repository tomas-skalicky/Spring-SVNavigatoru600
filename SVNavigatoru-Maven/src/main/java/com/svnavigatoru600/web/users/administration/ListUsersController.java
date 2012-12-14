package com.svnavigatoru600.web.users.administration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.service.users.ShowAllUsers;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderType;

/**
 * The controller bound mainly to the <i>users/administration/list.jsp</i> page. For more details about the
 * concepts used here, see the <i>ForgottenPasswordController</i> controller.
 * 
 * @author Tomas Skalicky
 */
@Controller
public class ListUsersController extends UserController {

    private static final String BASE_URL = "/administrace-uzivatelu/";
    private static final String COMMAND = "showAllUsersCommand";

    /**
     * Constructor.
     */
    @Autowired
    public ListUsersController(UserDao userDao, MessageSource messageSource) {
        super(userDao, messageSource);
    }

    /**
     * Initializes the page with all users.
     */
    @RequestMapping(value = ListUsersController.BASE_URL, method = RequestMethod.GET)
    public String initPage(HttpServletRequest request, ModelMap model) {

        ShowAllUsers command = new ShowAllUsers();

        final boolean testUsers = false;
        List<User> users = this.userDao.loadAllOrdered(OrderType.ASCENDING, testUsers);
        command.setUsers(users);

        // Sets up all (but necessary) maps.
        command.setLocalizedDeleteQuestions(this.getLocalizedDeleteQuestions(users, request));

        model.addAttribute(ListUsersController.COMMAND, command);
        return PageViews.LIST.getViewName();
    }

    /**
     * Initializes the page with all available users including the one which has been successfully added to
     * the repository recently.
     */
    @RequestMapping(value = ListUsersController.BASE_URL + "vytvoreno/", method = RequestMethod.GET)
    public String initPageAfterCreate(HttpServletRequest request, ModelMap model) {
        String view = this.initPage(request, model);
        ((ShowAllUsers) model.get(ListUsersController.COMMAND)).setUserCreated(true);
        return view;
    }

    /**
     * Initializes the page with all available users and notifies that one other user has been successfully
     * deleted from the repository.
     */
    @RequestMapping(value = ListUsersController.BASE_URL + "smazano/", method = RequestMethod.GET)
    public String initPageAfterDelete(HttpServletRequest request, ModelMap model) {
        String view = this.initPage(request, model);
        ((ShowAllUsers) model.get(ListUsersController.COMMAND)).setUserDeleted(true);
        return view;
    }

    /**
     * Gets a {@link Map} which for each input {@link User} contains an appropriate localized delete
     * questions.
     */
    private Map<User, String> getLocalizedDeleteQuestions(List<User> users, HttpServletRequest request) {
        final String messageCode = "user-administration.do-you-really-want-to-delete-user";
        Map<User, String> questions = new HashMap<User, String>();

        for (User user : users) {
            Object[] messageParams = new Object[] { user.getUsername(), user.getFullName() };
            questions.put(user,
                    Localization.findLocaleMessage(this.messageSource, request, messageCode, messageParams));
        }
        return questions;
    }
}
