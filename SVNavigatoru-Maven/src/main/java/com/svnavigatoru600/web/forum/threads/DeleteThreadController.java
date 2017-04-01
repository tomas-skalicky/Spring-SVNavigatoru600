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
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.url.forum.ThreadsUrlParts;
import com.svnavigatoru600.web.AbstractMetaController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteThreadController extends AbstractThreadController {

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

    @RequestMapping(value = ThreadsUrlParts.EXISTING_URL + "{threadId}/"
            + CommonUrlParts.DELETE_EXTENSION, method = RequestMethod.GET)
    @Transactional
    public String delete(@PathVariable int threadId, HttpServletRequest request, ModelMap model) {

        ThreadService threadService = getThreadService();
        threadService.canDelete(threadId);

        try {
            // Deletes the thread from the repository.
            Thread thread = threadService.findById(threadId);
            if (thread.getContributions().size() > 0) {
                String view = this.listController.initPage(request, model);
                model.addAttribute("error", DeleteThreadController.CANNOT_DELETE_DUE_CONTRIBUTION_MESSAGE_CODE);
                return view;
            }
            threadService.delete(thread);

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, ThreadsUrlParts.DELETED_URL);
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
