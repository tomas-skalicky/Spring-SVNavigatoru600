package com.svnavigatoru600.web.forum.threads;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.url.forum.ThreadsUrlParts;
import com.svnavigatoru600.viewmodel.forum.threads.ShowAllThreads;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListThreadsController extends AbstractThreadController {

    /**
     * Command used in /main-content/forum/threads/list-threads.jsp.
     */
    public static final String COMMAND = "showAllThreadsCommand";

    @Inject
    public ListThreadsController(final ThreadService threadService, final MessageSource messageSource) {
        super(threadService, messageSource);
    }

    @GetMapping(value = ThreadsUrlParts.BASE_URL)
    public String initPage(final HttpServletRequest request, final ModelMap model) {

        final ShowAllThreads command = new ShowAllThreads();

        final ThreadService threadService = getThreadService();
        final List<Thread> threads = threadService.loadAll();
        // Sorts in the descending order according to the last saved
        // contributions of the threads.
        Collections.sort(threads);
        command.setThreads(threads);

        // Sets up all auxiliary (but necessary) maps.
        command.setLastSavedContributions(Thread.getLastSavedContributions(threads));
        command.setLocalizedDeleteQuestions(
                ThreadService.getLocalizedDeleteQuestions(threads, request, getMessageSource()));

        // Gets more information about authors of the last saved contributions.
        getUserService().loadAuthorsOfLastSavedContributions(command.getLastSavedContributions());

        model.addAttribute(ListThreadsController.COMMAND, command);
        return PageViews.LIST.getViewName();
    }

    @GetMapping(value = ThreadsUrlParts.CREATED_URL)
    public String initPageAfterCreate(final HttpServletRequest request, final ModelMap model) {
        final String view = initPage(request, model);
        ((ShowAllThreads) model.get(ListThreadsController.COMMAND)).setThreadCreated(true);
        return view;
    }

    @GetMapping(value = ThreadsUrlParts.DELETED_URL)
    public String initPageAfterDelete(final HttpServletRequest request, final ModelMap model) {
        final String view = initPage(request, model);
        ((ShowAllThreads) model.get(ListThreadsController.COMMAND)).setThreadDeleted(true);
        return view;
    }
}
