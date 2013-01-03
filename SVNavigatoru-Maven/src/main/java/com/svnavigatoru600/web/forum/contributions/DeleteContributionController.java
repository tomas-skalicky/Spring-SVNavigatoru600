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
import com.svnavigatoru600.web.AbstractMetaController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteContributionController extends AbstractContributionController {

    private static final String REQUEST_MAPPING_BASE_URL = DeleteContributionController.BASE_URL
            + "{threadId}/prispevky/existujici/{contributionId}/smazat/";
    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "forum.contributions.deletion-failed-due-to-database-error";
    private ContributionService contributionService;
    private ListContributionsController listController;

    @Override
    @Inject
    public void setContributionService(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @Inject
    public void setListController(ListContributionsController listController) {
        this.listController = listController;
    }

    /**
     * Constructor.
     */
    @Inject
    public DeleteContributionController(ContributionService contributionService, MessageSource messageSource) {
        super(contributionService, messageSource);
    }

    @RequestMapping(value = DeleteContributionController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    @Transactional
    public String delete(@PathVariable int threadId, @PathVariable int contributionId,
            HttpServletRequest request, ModelMap model) {

        // Checks permission.
        this.contributionService.canDelete(contributionId);

        try {
            this.getContributionService().delete(contributionId);

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, String.format(
                    "%s%d/prispevky/smazano/", AbstractContributionController.BASE_URL, threadId));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(e);
            String view = this.listController.initPage(threadId, request, model);
            model.addAttribute("error", DeleteContributionController.DATABASE_ERROR_MESSAGE_CODE);
            return view;
        }
    }
}
