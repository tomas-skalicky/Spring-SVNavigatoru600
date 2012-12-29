package com.svnavigatoru600.web.forum.contributions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.service.forum.contributions.ContributionService;
import com.svnavigatoru600.web.Configuration;

@Controller
public class DeleteContributionController extends ContributionController {

    private static final String REQUEST_MAPPING_BASE_URL = DeleteContributionController.BASE_URL
            + "{threadId}/prispevky/existujici/{contributionId}/smazat/";
    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "forum.contributions.deletion-failed-due-to-database-error";
    private ContributionService contributionService;
    private ListContributionsController listController;

    @Autowired
    public void setContributionService(final ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @Autowired
    public void setListController(final ListContributionsController listController) {
        this.listController = listController;
    }

    /**
     * Constructor.
     */
    @Autowired
    public DeleteContributionController(final ContributionDao contributionDao,
            final MessageSource messageSource) {
        super(contributionDao, messageSource);
    }

    @RequestMapping(value = DeleteContributionController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    @Transactional
    public String delete(@PathVariable int threadId, @PathVariable int contributionId,
            final HttpServletRequest request, final ModelMap model) {

        // Checks permission.
        this.contributionService.canDelete(contributionId);

        try {
            // Deletes the contribution from the repository.
            final Contribution contribution = this.contributionDao.findById(contributionId);
            this.contributionDao.delete(contribution);

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE,
                    String.format("%s%d/prispevky/smazano/", DeleteContributionController.BASE_URL, threadId));
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(e);
            final String view = this.listController.initPage(threadId, request, model);
            model.addAttribute("error", DeleteContributionController.DATABASE_ERROR_MESSAGE_CODE);
            return view;
        }
    }
}
