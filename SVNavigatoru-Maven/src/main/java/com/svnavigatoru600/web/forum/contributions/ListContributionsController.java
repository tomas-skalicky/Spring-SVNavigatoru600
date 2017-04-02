package com.svnavigatoru600.web.forum.contributions;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.svnavigatoru600.domain.forum.ForumContribution;
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

    @Inject
    public ListContributionsController(final ContributionService contributionService, final ThreadService threadService,
            final MessageSource messageSource) {
        super(contributionService, messageSource);
        this.threadService = threadService;
    }

    @GetMapping(value = ListContributionsController.BASE_URL + ContributionsUrlParts.CONTRIBUTIONS_EXTENSION)
    public String initPage(@PathVariable final int threadId, final HttpServletRequest request, final ModelMap model) {

        final ShowAllContributions command = new ShowAllContributions();

        final List<ForumContribution> contributions = getContributionService().findAllOrdered(threadId);
        command.setContributions(contributions);
        command.setThread(threadService.findById(threadId));

        // Sets up all auxiliary (but necessary) maps.
        command.setLocalizedDeleteQuestions(
                ContributionService.getLocalizedDeleteQuestions(contributions, request, getMessageSource()));

        model.addAttribute(ListContributionsController.COMMAND, command);
        return PageViewsEnum.LIST.getViewName();
    }

    @GetMapping(value = ListContributionsController.BASE_URL + ContributionsUrlParts.CONTRIBUTIONS_CREATED_EXTENSION)
    public String initPageAfterCreate(@PathVariable final int threadId, final HttpServletRequest request,
            final ModelMap model) {
        final String view = initPage(threadId, request, model);
        ((ShowAllContributions) model.get(ListContributionsController.COMMAND)).setContributionCreated(true);
        return view;
    }

    @GetMapping(value = ListContributionsController.BASE_URL + ContributionsUrlParts.CONTRIBUTIONS_DELETED_EXTENSION)
    public String initPageAfterDelete(@PathVariable final int threadId, final HttpServletRequest request,
            final ModelMap model) {
        final String view = initPage(threadId, request, model);
        ((ShowAllContributions) model.get(ListContributionsController.COMMAND)).setContributionDeleted(true);
        return view;
    }
}
