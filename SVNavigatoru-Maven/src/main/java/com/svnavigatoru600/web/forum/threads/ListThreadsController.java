package com.svnavigatoru600.web.forum.threads;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.service.util.Localization;
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
    private UserDao userDao;

    @Inject
    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Constructor.
     */
    @Inject
    public ListThreadsController(final ThreadDao threadDao, final MessageSource messageSource) {
        super(threadDao, messageSource);
    }

    @RequestMapping(value = ListThreadsController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public String initPage(final HttpServletRequest request, final ModelMap model) {

        final ShowAllThreads command = new ShowAllThreads();

        final List<Thread> threads = this.threadDao.loadAll();
        // Sorts in the descending order according to the last saved
        // contributions of the threads.
        Collections.sort(threads);
        command.setThreads(threads);

        // Sets up all auxiliary (but necessary) maps.
        command.setLastSavedContributions(this.getLastSavedContributions(threads));
        command.setLocalizedDeleteQuestions(this.getLocalizedDeleteQuestions(threads, request));

        // Gets more information about authors of the last saved contributions.
        this.loadAuthorsOfLastSavedContributions(command.getLastSavedContributions());

        model.addAttribute(ListThreadsController.COMMAND, command);
        return PageViews.LIST.getViewName();
    }

    @RequestMapping(value = ListThreadsController.REQUEST_MAPPING_BASE_URL + "vytvoreno/", method = RequestMethod.GET)
    public String initPageAfterCreate(final HttpServletRequest request, final ModelMap model) {
        final String view = this.initPage(request, model);
        ((ShowAllThreads) model.get(ListThreadsController.COMMAND)).setThreadCreated(true);
        return view;
    }

    @RequestMapping(value = ListThreadsController.REQUEST_MAPPING_BASE_URL + "smazano/", method = RequestMethod.GET)
    public String initPageAfterDelete(final HttpServletRequest request, final ModelMap model) {
        final String view = this.initPage(request, model);
        ((ShowAllThreads) model.get(ListThreadsController.COMMAND)).setThreadDeleted(true);
        return view;
    }

    /**
     * Gets a {@link Map} which for each input {@link Thread} contains a {@link Contribution} which has the
     * highest <code>lastSaveTime</code> and which belongs to the {@link Thread}. Moreover, the method finds
     * the author of such {@link Contribution}.
     */
    private Map<Thread, Contribution> getLastSavedContributions(final List<Thread> threads) {
        final Map<Thread, Contribution> lastSavedContributions = new HashMap<Thread, Contribution>();

        for (Thread thread : threads) {
            lastSavedContributions.put(thread, thread.getLastSavedContribution());
        }
        return lastSavedContributions;
    }

    /**
     * Gets a {@link Map} which for each input {@link Thread} contains an appropriate localized delete
     * questions.
     */
    private Map<Thread, String> getLocalizedDeleteQuestions(final List<Thread> threads,
            final HttpServletRequest request) {
        final String messageCode = "forum.threads.do-you-really-want-to-delete-thread";
        final Map<Thread, String> questions = new HashMap<Thread, String>();

        for (Thread thread : threads) {
            final Object[] messageParams = new Object[] { thread.getName() };
            questions.put(thread,
                    Localization.findLocaleMessage(this.messageSource, request, messageCode, messageParams));
        }
        return questions;
    }

    /**
     * Loads more information about authors of the last saved contributions.
     */
    private void loadAuthorsOfLastSavedContributions(Map<Thread, Contribution> lastSavedContributions) {
        for (Thread thread : lastSavedContributions.keySet()) {
            Contribution contribution = lastSavedContributions.get(thread);
            if (contribution == null) {
                continue;
            }

            String authorUsername = contribution.getAuthor().getUsername();
            contribution.setAuthor(this.userDao.findByUsername(authorUsername));
        }
    }
}
