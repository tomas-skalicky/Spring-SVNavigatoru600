package com.svnavigatoru600.web.forum.contributions;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.service.forum.ContributionService;
import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.url.forum.ContributionsUrlParts;
import com.svnavigatoru600.viewmodel.forum.contributions.ShowAllContributions;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListContributionsController extends AbstractContributionController {

    private static final String BASE_URL = ContributionsUrlParts.BASE_URL + "{threadId}/";
    /**
     * Command used in /main-content/forum/contributions/list-contributions.jsp.
     */
    public static final String COMMAND = "showAllContributionsCommand";
    private final ThreadService threadService;

    /**
     * Constructor.
     */
    @Inject
    public ListContributionsController(ContributionService contributionService, ThreadService threadService,
            MessageSource messageSource) {
        super(contributionService, messageSource);
        this.threadService = threadService;
    }

    @RequestMapping(value = ListContributionsController.BASE_URL
            + ContributionsUrlParts.CONTRIBUTIONS_EXTENSION, method = RequestMethod.GET)
    public String initPage(@PathVariable int threadId, HttpServletRequest request, ModelMap model) {

        ShowAllContributions command = new ShowAllContributions();

        List<Contribution> contributions = this.getContributionService().findAllOrdered(threadId);
        command.setContributions(contributions);
        command.setThread(this.threadService.findById(threadId));

        // Sets up all auxiliary (but necessary) maps.
        command.setLocalizedDeleteQuestions(ContributionService.getLocalizedDeleteQuestions(contributions,
                request, this.getMessageSource()));

        model.addAttribute(ListContributionsController.COMMAND, command);
        return PageViews.LIST.getViewName();
    }

    @RequestMapping(value = ListContributionsController.BASE_URL
            + ContributionsUrlParts.CONTRIBUTIONS_CREATED_EXTENSION, method = RequestMethod.GET)
    public String initPageAfterCreate(@PathVariable int threadId, HttpServletRequest request, ModelMap model) {
        String view = this.initPage(threadId, request, model);
        ((ShowAllContributions) model.get(ListContributionsController.COMMAND)).setContributionCreated(true);
        return view;
    }

    @RequestMapping(value = ListContributionsController.BASE_URL
            + ContributionsUrlParts.CONTRIBUTIONS_DELETED_EXTENSION, method = RequestMethod.GET)
    public String initPageAfterDelete(@PathVariable int threadId, HttpServletRequest request, ModelMap model) {
        String view = this.initPage(threadId, request, model);
        ((ShowAllContributions) model.get(ListContributionsController.COMMAND)).setContributionDeleted(true);
        return view;
    }
}
