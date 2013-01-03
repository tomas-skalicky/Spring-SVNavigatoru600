package com.svnavigatoru600.web.forum.threads;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.viewmodel.forum.threads.EditThread;
import com.svnavigatoru600.viewmodel.forum.threads.validator.EditThreadValidator;
import com.svnavigatoru600.web.AbstractMetaController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditThreadController extends AbstractNewEditThreadController {

    private static final String REQUEST_MAPPING_BASE_URL = EditThreadController.BASE_URL
            + "existujici/{threadId}/";
    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";

    /**
     * Constructor.
     */
    @Inject
    public EditThreadController(ThreadService threadService, EditThreadValidator validator,
            MessageSource messageSource) {
        super(threadService, validator, messageSource);
    }

    @RequestMapping(value = EditThreadController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public String initForm(@PathVariable int threadId, HttpServletRequest request, ModelMap model) {

        ThreadService threadService = this.getThreadService();
        threadService.canEdit(threadId);

        EditThread command = new EditThread();

        Thread thread = threadService.findById(threadId);
        command.setThread(thread);

        model.addAttribute(AbstractNewEditThreadController.COMMAND, command);
        return PageViews.EDIT.getViewName();
    }

    @RequestMapping(value = EditThreadController.REQUEST_MAPPING_BASE_URL + "ulozeno/", method = RequestMethod.GET)
    public String initFormAfterSave(@PathVariable int threadId, HttpServletRequest request, ModelMap model) {
        String view = this.initForm(threadId, request, model);
        ((EditThread) model.get(AbstractNewEditThreadController.COMMAND)).setDataSaved(true);
        return view;
    }

    @RequestMapping(value = EditThreadController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.POST)
    @Transactional
    public String processSubmittedForm(@ModelAttribute(EditThreadController.COMMAND) EditThread command,
            BindingResult result, SessionStatus status, @PathVariable int threadId,
            HttpServletRequest request, ModelMap model) {

        ThreadService threadService = this.getThreadService();
        threadService.canEdit(threadId);

        this.getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViews.EDIT.getViewName();
        }

        Thread originalThread = null;
        try {
            originalThread = threadService.findById(threadId);
            threadService.update(originalThread, command.getThread());

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE,
                    String.format("%sexistujici/%d/ulozeno/", AbstractThreadController.BASE_URL, threadId));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(originalThread, e);
            result.reject(EditThreadController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViews.EDIT.getViewName();
    }
}
