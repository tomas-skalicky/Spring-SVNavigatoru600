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

    /**
     * Constructor.
     */
    @Inject
    public EditContributionController(ContributionService contributionService,
            SendNotificationEditModelFiller sendNotificationModelFiller, EditContributionValidator validator,
            MessageSource messageSource) {
        super(contributionService, sendNotificationModelFiller, validator, messageSource);
    }

    @RequestMapping(value = EditContributionController.BASE_URL, method = RequestMethod.GET)
    public String initForm(@PathVariable int threadId, @PathVariable int contributionId, HttpServletRequest request,
            ModelMap model) {

        getContributionService().canEdit(contributionId);

        EditContribution command = new EditContribution();

        Contribution contribution = getContributionService().findById(contributionId);
        command.setContribution(contribution);

        getSendNotificationModelFiller().populateSendNotificationInInitForm(command, request, getMessageSource());

        model.addAttribute(AbstractNewEditContributionController.COMMAND, command);
        return PageViews.EDIT.getViewName();
    }

    @RequestMapping(value = EditContributionController.BASE_URL
            + CommonUrlParts.SAVED_EXTENSION, method = RequestMethod.GET)
    public String initFormAfterSave(@PathVariable int threadId, @PathVariable int contributionId,
            HttpServletRequest request, ModelMap model) {
        String view = initForm(threadId, contributionId, request, model);
        ((EditContribution) model.get(AbstractNewEditContributionController.COMMAND)).setDataSaved(true);
        return view;
    }

    @RequestMapping(value = EditContributionController.BASE_URL, method = RequestMethod.POST)
    @Transactional
    public String processSubmittedForm(@ModelAttribute(EditContributionController.COMMAND) EditContribution command,
            BindingResult result, SessionStatus status, @PathVariable int threadId, @PathVariable int contributionId,
            HttpServletRequest request, ModelMap model) {

        getContributionService().canEdit(contributionId);

        MessageSource messageSource = getMessageSource();
        getSendNotificationModelFiller().populateSendNotificationInSubmitForm(command, request, messageSource);

        getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViews.EDIT.getViewName();
        }

        ContributionService contributionService = getContributionService();
        Contribution originalContribution = null;
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

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(originalContribution, e);
            result.reject(EditContributionController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViews.EDIT.getViewName();
    }
}
