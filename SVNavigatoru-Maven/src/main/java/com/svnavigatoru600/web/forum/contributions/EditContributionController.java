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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.service.forum.ContributionService;
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.url.forum.ContributionsUrlParts;
import com.svnavigatoru600.viewmodel.forum.contributions.EditContribution;
import com.svnavigatoru600.viewmodel.forum.contributions.validator.EditContributionValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.SendNotificationEditModelFiller;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditContributionController extends AbstractNewEditContributionController {

    private static final String BASE_URL = ContributionsUrlParts.BASE_URL + "{threadId}/"
            + ContributionsUrlParts.CONTRIBUTIONS_EXISTING_EXTENSION + "{contributionId}/";
    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";

    @Inject
    public EditContributionController(final ContributionService contributionService,
            final SendNotificationEditModelFiller sendNotificationModelFiller,
            final EditContributionValidator validator, final MessageSource messageSource) {
        super(contributionService, sendNotificationModelFiller, validator, messageSource);
    }

    @GetMapping(value = EditContributionController.BASE_URL)
    public String initForm(@PathVariable final int threadId, @PathVariable final int contributionId,
            final HttpServletRequest request, final ModelMap model) {

        getContributionService().canEdit(contributionId);

        final EditContribution command = new EditContribution();

        final ForumContribution contribution = getContributionService().findById(contributionId);
        command.setContribution(contribution);

        getSendNotificationModelFiller().populateSendNotificationInInitForm(command, request, getMessageSource());

        model.addAttribute(AbstractNewEditContributionController.COMMAND, command);
        return PageViews.EDIT.getViewName();
    }

    @GetMapping(value = EditContributionController.BASE_URL + CommonUrlParts.SAVED_EXTENSION)
    public String initFormAfterSave(@PathVariable final int threadId, @PathVariable final int contributionId,
            final HttpServletRequest request, final ModelMap model) {
        final String view = initForm(threadId, contributionId, request, model);
        ((EditContribution) model.get(AbstractNewEditContributionController.COMMAND)).setDataSaved(true);
        return view;
    }

    @PostMapping(value = EditContributionController.BASE_URL)
    @Transactional
    public String processSubmittedForm(
            @ModelAttribute(EditContributionController.COMMAND) final EditContribution command,
            final BindingResult result, final SessionStatus status, @PathVariable final int threadId,
            @PathVariable final int contributionId, final HttpServletRequest request, final ModelMap model) {

        getContributionService().canEdit(contributionId);

        final MessageSource messageSource = getMessageSource();
        getSendNotificationModelFiller().populateSendNotificationInSubmitForm(command, request, messageSource);

        getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViews.EDIT.getViewName();
        }

        final ContributionService contributionService = getContributionService();
        ForumContribution originalContribution = null;
        try {
            originalContribution = contributionService.findById(contributionId);
            contributionService.updateAndNotifyUsers(originalContribution, command.getContribution(),
                    command.getSendNotification().isStatus(), request, messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE,
                    String.format("%s%d/%s%d/%s", ContributionsUrlParts.BASE_URL, threadId,
                            ContributionsUrlParts.CONTRIBUTIONS_EXISTING_EXTENSION, contributionId,
                            CommonUrlParts.SAVED_EXTENSION));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(originalContribution, e);
            result.reject(EditContributionController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViews.EDIT.getViewName();
    }
}
