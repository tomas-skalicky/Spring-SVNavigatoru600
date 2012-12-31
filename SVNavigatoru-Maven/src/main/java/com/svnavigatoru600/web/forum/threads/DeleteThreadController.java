package com.svnavigatoru600.web.forum.threads;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.service.forum.threads.ThreadService;
import com.svnavigatoru600.web.Configuration;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteThreadController extends AbstractThreadController {

    private static final String REQUEST_MAPPING_BASE_URL = DeleteThreadController.BASE_URL
            + "existujici/{threadId}/smazat/";
    private static final String SUCCESSFUL_DELETE_URL = DeleteThreadController.BASE_URL + "smazano/";
    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "forum.threads.deletion-failed-due-to-database-error";
    public static final String CANNOT_DELETE_DUE_CONTRIBUTION_MESSAGE_CODE = "forum.threads.deletion-failed-due-to-contribution-count";
    private ThreadService threadService;
    private ListThreadsController listController;

    @Inject
    public void setThreadService(final ThreadService threadService) {
        this.threadService = threadService;
    }

    @Inject
    public void setListController(final ListThreadsController listController) {
        this.listController = listController;
    }

    /**
     * Constructor.
     */
    @Inject
    public DeleteThreadController(final ThreadDao threadDao, final MessageSource messageSource) {
        super(threadDao, messageSource);
    }

    @RequestMapping(value = DeleteThreadController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    @Transactional
    public String delete(@PathVariable int threadId, final HttpServletRequest request, final ModelMap model) {

        this.threadService.canDelete(threadId);

        try {
            // Deletes the thread from the repository.
            final Thread thread = this.threadDao.findById(threadId);
            if (thread.getContributions().size() > 0) {
                final String view = this.listController.initPage(request, model);
                model.addAttribute("error",
                        DeleteThreadController.CANNOT_DELETE_DUE_CONTRIBUTION_MESSAGE_CODE);
                return view;
            }
            this.threadDao.delete(thread);

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE,
                    DeleteThreadController.SUCCESSFUL_DELETE_URL);
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(e);
            final String view = this.listController.initPage(request, model);
            model.addAttribute("error", DeleteThreadController.DATABASE_ERROR_MESSAGE_CODE);
            return view;
        }
    }
}
