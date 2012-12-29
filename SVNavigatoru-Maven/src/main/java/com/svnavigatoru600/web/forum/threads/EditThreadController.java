package com.svnavigatoru600.web.forum.threads;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.service.forum.threads.EditThread;
import com.svnavigatoru600.service.forum.threads.validator.EditThreadValidator;
import com.svnavigatoru600.web.Configuration;

@Controller
public class EditThreadController extends NewEditThreadController {

    private static final String REQUEST_MAPPING_BASE_URL = EditThreadController.BASE_URL
            + "existujici/{threadId}/";
    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";
    private EditThread editThread;

    @Autowired
    public void setEditThread(final EditThread editThread) {
        this.editThread = editThread;
    }

    /**
     * Constructor.
     */
    @Autowired
    public EditThreadController(final ThreadDao threadDao, final EditThreadValidator validator,
            final MessageSource messageSource) {
        super(threadDao, validator, messageSource);
    }

    @RequestMapping(value = EditThreadController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public String initForm(@PathVariable int threadId, final HttpServletRequest request, final ModelMap model) {

        this.editThread.canEdit(threadId);

        final EditThread command = new EditThread();

        final Thread thread = this.threadDao.findById(threadId);
        command.setThread(thread);

        model.addAttribute(EditThreadController.COMMAND, command);
        return PageViews.EDIT.getViewName();
    }

    @RequestMapping(value = EditThreadController.REQUEST_MAPPING_BASE_URL + "ulozeno/", method = RequestMethod.GET)
    public String initFormAfterSave(@PathVariable int threadId, final HttpServletRequest request,
            final ModelMap model) {
        final String view = this.initForm(threadId, request, model);
        ((EditThread) model.get(EditThreadController.COMMAND)).setDataSaved(true);
        return view;
    }

    @RequestMapping(value = EditThreadController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.POST)
    @Transactional
    public String processSubmittedForm(@ModelAttribute(EditThreadController.COMMAND) EditThread command,
            final BindingResult result, final SessionStatus status, @PathVariable int threadId,
            final HttpServletRequest request, final ModelMap model) {

        this.editThread.canEdit(threadId);

        this.validator.validate(command, result);
        if (result.hasErrors()) {
            return PageViews.EDIT.getViewName();
        }

        // Updates the original data.
        final Thread originalThread = this.threadDao.findById(threadId);
        final Thread newThread = command.getThread();
        originalThread.setName(newThread.getName());

        try {
            // Updates the thread in the repository.
            this.threadDao.update(originalThread);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE,
                    String.format("%sexistujici/%d/ulozeno/", EditThreadController.BASE_URL, threadId));
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(originalThread, e);
            result.reject(EditThreadController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViews.EDIT.getViewName();
    }
}
