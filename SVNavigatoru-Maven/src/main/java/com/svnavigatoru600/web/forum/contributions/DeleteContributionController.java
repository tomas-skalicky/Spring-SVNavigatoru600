package com.svnavigatoru600.web.forum.contributions;

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

import com.svnavigatoru600.service.forum.ContributionService;
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.url.forum.ContributionsUrlParts;
import com.svnavigatoru600.web.AbstractMetaController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteContributionController extends AbstractContributionController {

    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "forum.contributions.deletion-failed-due-to-database-error";
    private ListContributionsController listController;

    @Inject
    public void setListController(final ListContributionsController listController) {
        this.listController = listController;
    }

    /**
     * Constructor.
     */
    @Inject
    public DeleteContributionController(final ContributionService contributionService, final MessageSource messageSource) {
        super(contributionService, messageSource);
    }

    @RequestMapping(value = ContributionsUrlParts.BASE_URL + "{threadId}/"
            + ContributionsUrlParts.CONTRIBUTIONS_EXISTING_EXTENSION + "{contributionId}/"
            + CommonUrlParts.DELETE_EXTENSION, method = RequestMethod.GET)
    @Transactional
    public String delete(@PathVariable final int threadId, @PathVariable final int contributionId, final HttpServletRequest request,
            final ModelMap model) {

        // Checks permission.
        getContributionService().canDelete(contributionId);

        try {
            getContributionService().delete(contributionId);

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, String.format("%s%d/%s",
                    ContributionsUrlParts.BASE_URL, threadId, ContributionsUrlParts.CONTRIBUTIONS_DELETED_EXTENSION));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(e);
            final String view = listController.initPage(threadId, request, model);
            model.addAttribute("error", DeleteContributionController.DATABASE_ERROR_MESSAGE_CODE);
            return view;
        }
    }
}
