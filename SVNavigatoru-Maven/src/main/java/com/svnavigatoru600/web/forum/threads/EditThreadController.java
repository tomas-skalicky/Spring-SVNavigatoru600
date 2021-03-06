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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.forum.ForumThread;
import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.viewmodel.forum.threads.EditThread;
import com.svnavigatoru600.viewmodel.forum.threads.validator.EditThreadValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.url.CommonUrlParts;
import com.svnavigatoru600.web.url.forum.ThreadsUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditThreadController extends AbstractNewEditThreadController {

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";

    @Inject
    public EditThreadController(final ThreadService threadService, final EditThreadValidator validator,
            final MessageSource messageSource) {
        super(threadService, validator, messageSource);
    }

    @GetMapping(value = ThreadsUrlParts.EXISTING_URL + "{threadId}/")
    public String initForm(@PathVariable final int threadId, final HttpServletRequest request, final ModelMap model) {

        final ThreadService threadService = getThreadService();
        threadService.canEdit(threadId);

        final EditThread command = new EditThread();

        final ForumThread thread = threadService.findById(threadId);
        command.setThread(thread);

        model.addAttribute(AbstractNewEditThreadController.COMMAND, command);
        return PageViewsEnum.EDIT.getViewName();
    }

    @GetMapping(value = ThreadsUrlParts.EXISTING_URL + "{threadId}/" + CommonUrlParts.SAVED_EXTENSION)
    public String initFormAfterSave(@PathVariable final int threadId, final HttpServletRequest request,
            final ModelMap model) {
        final String view = initForm(threadId, request, model);
        ((EditThread) model.get(AbstractNewEditThreadController.COMMAND)).setDataSaved(true);
        return view;
    }

    @PostMapping(value = ThreadsUrlParts.EXISTING_URL + "{threadId}/")
    @Transactional
    public String processSubmittedForm(@ModelAttribute(EditThreadController.COMMAND) final EditThread command,
            final BindingResult result, final SessionStatus status, @PathVariable final int threadId,
            final HttpServletRequest request, final ModelMap model) {

        final ThreadService threadService = getThreadService();
        threadService.canEdit(threadId);

        getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViewsEnum.EDIT.getViewName();
        }

        ForumThread originalThread = null;
        try {
            originalThread = threadService.findById(threadId);
            threadService.update(originalThread, command.getThread());

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE,
                    String.format("%s%d/%s", ThreadsUrlParts.EXISTING_URL, threadId, CommonUrlParts.SAVED_EXTENSION));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(originalThread, e);
            result.reject(EditThreadController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViewsEnum.EDIT.getViewName();
    }
}
