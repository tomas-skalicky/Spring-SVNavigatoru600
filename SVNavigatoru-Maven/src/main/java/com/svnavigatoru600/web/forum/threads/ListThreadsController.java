package com.svnavigatoru600.web.forum.threads;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.viewmodel.forum.threads.ShowAllThreads;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListThreadsController extends AbstractThreadController {

    private static final String REQUEST_MAPPING_BASE_URL = ListThreadsController.BASE_URL;
    /**
     * Command used in /main-content/forum/threads/list-threads.jsp.
     */
    public static final String COMMAND = "showAllThreadsCommand";
    private UserService userService;

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Constructor.
     */
    @Inject
    public ListThreadsController(ThreadService threadService, MessageSource messageSource) {
        super(threadService, messageSource);
    }

    @RequestMapping(value = ListThreadsController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public String initPage(HttpServletRequest request, ModelMap model) {

        ShowAllThreads command = new ShowAllThreads();

        ThreadService threadService = this.getThreadService();
        List<Thread> threads = threadService.loadAll();
        // Sorts in the descending order according to the last saved
        // contributions of the threads.
        Collections.sort(threads);
        command.setThreads(threads);

        // Sets up all auxiliary (but necessary) maps.
        command.setLastSavedContributions(Thread.getLastSavedContributions(threads));
        command.setLocalizedDeleteQuestions(ThreadService.getLocalizedDeleteQuestions(threads, request,
                this.getMessageSource()));

        // Gets more information about authors of the last saved contributions.
        this.userService.loadAuthorsOfLastSavedContributions(command.getLastSavedContributions());

        model.addAttribute(ListThreadsController.COMMAND, command);
        return PageViews.LIST.getViewName();
    }

    @RequestMapping(value = ListThreadsController.REQUEST_MAPPING_BASE_URL + "vytvoreno/", method = RequestMethod.GET)
    public String initPageAfterCreate(HttpServletRequest request, ModelMap model) {
        String view = this.initPage(request, model);
        ((ShowAllThreads) model.get(ListThreadsController.COMMAND)).setThreadCreated(true);
        return view;
    }

    @RequestMapping(value = ListThreadsController.REQUEST_MAPPING_BASE_URL + "smazano/", method = RequestMethod.GET)
    public String initPageAfterDelete(HttpServletRequest request, ModelMap model) {
        String view = this.initPage(request, model);
        ((ShowAllThreads) model.get(ListThreadsController.COMMAND)).setThreadDeleted(true);
        return view;
    }
}
