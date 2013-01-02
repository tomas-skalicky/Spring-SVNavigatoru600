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
import com.svnavigatoru600.service.forum.contributions.ContributionService;
import com.svnavigatoru600.service.forum.contributions.validator.EditContributionValidator;
import com.svnavigatoru600.viewmodel.forum.contributions.EditContribution;
import com.svnavigatoru600.web.Configuration;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditContributionController extends AbstractNewEditContributionController {

    private static final String REQUEST_MAPPING_BASE_URL = EditContributionController.BASE_URL
            + "{threadId}/prispevky/existujici/{contributionId}/";
    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";
    private ContributionService contributionService;

    @Inject
    public void setContributionService(final ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    /**
     * Constructor.
     */
    @Inject
    public EditContributionController(final ContributionService contributionService,
            final EditContributionValidator validator, final MessageSource messageSource) {
        super(contributionService, validator, messageSource);
    }

    @RequestMapping(value = EditContributionController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public String initForm(@PathVariable int threadId, @PathVariable int contributionId,
            final HttpServletRequest request, final ModelMap model) {

        this.contributionService.canEdit(contributionId);

        final EditContribution command = new EditContribution();

        final Contribution contribution = this.getContributionService().findById(contributionId);
        command.setContribution(contribution);
        command.setThreadId(threadId);

        model.addAttribute(AbstractNewEditContributionController.COMMAND, command);
        return PageViews.EDIT.getViewName();
    }

    @RequestMapping(value = EditContributionController.REQUEST_MAPPING_BASE_URL + "ulozeno/", method = RequestMethod.GET)
    public String initFormAfterSave(@PathVariable int threadId, @PathVariable int contributionId,
            final HttpServletRequest request, final ModelMap model) {
        final String view = this.initForm(threadId, contributionId, request, model);
        ((EditContribution) model.get(AbstractNewEditContributionController.COMMAND)).setDataSaved(true);
        return view;
    }

    @RequestMapping(value = EditContributionController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.POST)
    @Transactional
    public String processSubmittedForm(
            @ModelAttribute(EditContributionController.COMMAND) EditContribution command,
            BindingResult result, SessionStatus status, @PathVariable int threadId,
            @PathVariable int contributionId, final HttpServletRequest request, final ModelMap model) {

        this.contributionService.canEdit(contributionId);

        this.getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViews.EDIT.getViewName();
        }

        final ContributionService contributionService = this.getContributionService();
        Contribution originalContribution = null;
        try {
            originalContribution = contributionService.findById(contributionId);
            contributionService.update(originalContribution, command.getContribution());

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE, String.format(
                    "%s%d/prispevky/existujici/%d/ulozeno/", AbstractContributionController.BASE_URL,
                    threadId, contributionId));
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(originalContribution, e);
            result.reject(EditContributionController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViews.EDIT.getViewName();
    }
}
