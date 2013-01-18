package com.svnavigatoru600.web.forum.contributions;

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

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.service.forum.ContributionService;
import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.service.util.UserUtils;
import com.svnavigatoru600.url.forum.ContributionsUrlParts;
import com.svnavigatoru600.viewmodel.forum.contributions.NewContribution;
import com.svnavigatoru600.viewmodel.forum.contributions.validator.NewContributionValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.SendNotificationNewModelFiller;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class NewContributionController extends AbstractNewEditContributionController {

    private static final String BASE_URL = ContributionsUrlParts.BASE_URL + "{threadId}/"
            + ContributionsUrlParts.CONTRIBUTIONS_NEW_EXTENSION;
    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "forum.contributions.adding-failed-due-to-database-error";
    private ThreadService threadService;

    @Inject
    public void setThreadService(ThreadService threadService) {
        this.threadService = threadService;
    }

    /**
     * Constructor.
     */
    @Inject
    public NewContributionController(ContributionService contributionService,
            SendNotificationNewModelFiller sendNotificationModelFiller, NewContributionValidator validator,
            MessageSource messageSource) {
        super(contributionService, sendNotificationModelFiller, validator, messageSource);
    }

    /**
     * Initializes the form.
     */
    @RequestMapping(value = NewContributionController.BASE_URL, method = RequestMethod.GET)
    public String initForm(@PathVariable int threadId, HttpServletRequest request, ModelMap model) {

        NewContribution command = new NewContribution();

        Contribution contribution = new Contribution();
        command.setContribution(contribution);
        command.setThreadId(threadId);

        this.getSendNotificationModelFiller().populateSendNotificationInInitForm(command, request,
                this.getMessageSource());

        model.addAttribute(AbstractNewEditContributionController.COMMAND, command);
        return PageViews.NEW.getViewName();
    }

    /**
     * If values in the form are OK, the new contribution is stored to the repository. Otherwise, returns back
     * to the form.
     * 
     * @return The name of the view which is to be shown.
     */
    @RequestMapping(value = NewContributionController.BASE_URL, method = RequestMethod.POST)
    @Transactional
    public String processSubmittedForm(
            @ModelAttribute(NewContributionController.COMMAND) NewContribution command, BindingResult result,
            SessionStatus status, @PathVariable int threadId, HttpServletRequest request, ModelMap model) {

        MessageSource messageSource = this.getMessageSource();
        this.getSendNotificationModelFiller().populateSendNotificationInSubmitForm(command, request,
                messageSource);

        this.getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViews.NEW.getViewName();
        }

        // Updates the data of the new contribution.
        Contribution newContribution = command.getContribution();
        newContribution.setAuthor(UserUtils.getLoggedUser());

        try {
            newContribution.setThread(this.threadService.findById(threadId));

            // Saves the contribution to the repository.
            this.getContributionService().saveAndNotifyUsers(newContribution,
                    command.getSendNotification().isStatus(), request, messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, String.format("%s%d/%s",
                    ContributionsUrlParts.BASE_URL, threadId,
                    ContributionsUrlParts.CONTRIBUTIONS_CREATED_EXTENSION));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(newContribution, e);
            result.reject(NewContributionController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViews.NEW.getViewName();
    }
}
