package com.svnavigatoru600.web.forum.threads;

import java.util.Arrays;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.domain.forum.ForumThread;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.service.util.UserUtils;
import com.svnavigatoru600.viewmodel.forum.threads.NewThread;
import com.svnavigatoru600.viewmodel.forum.threads.validator.NewThreadValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.SendNotificationController;
import com.svnavigatoru600.web.SendNotificationModelFiller;
import com.svnavigatoru600.web.SendNotificationNewModelFiller;
import com.svnavigatoru600.web.url.forum.ThreadsUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class NewThreadController extends AbstractNewEditThreadController implements SendNotificationController {

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "forum.threads.adding-failed-due-to-database-error";
    private final SendNotificationModelFiller sendNotificationModelFiller;

    @Inject
    public NewThreadController(final ThreadService threadService,
            final SendNotificationNewModelFiller sendNotificationModelFiller, final NewThreadValidator validator,
            final MessageSource messageSource) {
        super(threadService, validator, messageSource);
        this.sendNotificationModelFiller = sendNotificationModelFiller;
    }

    /**
     * Initializes the form.
     */
    @GetMapping(value = ThreadsUrlParts.NEW_URL)
    public String initForm(final HttpServletRequest request, final ModelMap model) {

        final NewThread command = new NewThread();

        final ForumThread thread = new ForumThread();
        command.setThread(thread);
        final ForumContribution contribution = new ForumContribution();
        command.setContribution(contribution);

        sendNotificationModelFiller.populateSendNotificationInInitForm(command, request, getMessageSource());

        model.addAttribute(AbstractNewEditThreadController.COMMAND, command);
        return PageViewsEnum.NEW.getViewName();
    }

    /**
     * If values in the form are OK, the new thread is stored to the repository. Otherwise, returns back to the form.
     *
     * @return The name of the view which is to be shown.
     */
    @PostMapping(value = ThreadsUrlParts.NEW_URL)
    @Transactional
    public String processSubmittedForm(@ModelAttribute(NewThreadController.COMMAND) final NewThread command,
            final BindingResult result, final SessionStatus status, final HttpServletRequest request,
            final ModelMap model) {

        final MessageSource messageSource = getMessageSource();
        sendNotificationModelFiller.populateSendNotificationInSubmitForm(command, request, messageSource);

        getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViewsEnum.NEW.getViewName();
        }

        // Updates the data of the new thread and its first contribution.
        final ForumThread newThread = command.getThread();
        final User author = UserUtils.getLoggedUser();
        newThread.setAuthor(author);

        final ForumContribution firstContribution = command.getContribution();
        firstContribution.setThread(newThread);
        firstContribution.setAuthor(author);

        newThread.setContributions(Arrays.asList(firstContribution));
        // End of update.

        try {
            // Stores both the thread and the contribution to the repository.
            getThreadService().saveAndNotifyUsers(newThread, command.getSendNotification().isStatus(), request,
                    messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, ThreadsUrlParts.CREATED_URL);
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(newThread, e);
            result.reject(NewThreadController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViewsEnum.NEW.getViewName();
    }
}
