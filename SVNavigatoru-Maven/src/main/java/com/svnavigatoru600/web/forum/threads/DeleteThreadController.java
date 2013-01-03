package com.svnavigatoru600.web.forum.threads;

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

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.web.AbstractMetaController;

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
    private ListThreadsController listController;

    @Inject
    public void setListController(ListThreadsController listController) {
        this.listController = listController;
    }

    /**
     * Constructor.
     */
    @Inject
    public DeleteThreadController(ThreadService threadService, MessageSource messageSource) {
        super(threadService, messageSource);
    }

    @RequestMapping(value = DeleteThreadController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    @Transactional
    public String delete(@PathVariable int threadId, HttpServletRequest request, ModelMap model) {

        ThreadService threadService = this.getThreadService();
        threadService.canDelete(threadId);

        try {
            // Deletes the thread from the repository.
            Thread thread = threadService.findById(threadId);
            if (thread.getContributions().size() > 0) {
                String view = this.listController.initPage(request, model);
                model.addAttribute("error",
                        DeleteThreadController.CANNOT_DELETE_DUE_CONTRIBUTION_MESSAGE_CODE);
                return view;
            }
            threadService.delete(thread);

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE,
                    DeleteThreadController.SUCCESSFUL_DELETE_URL);
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(e);
            String view = this.listController.initPage(request, model);
            model.addAttribute("error", DeleteThreadController.DATABASE_ERROR_MESSAGE_CODE);
            return view;
        }
    }
}
