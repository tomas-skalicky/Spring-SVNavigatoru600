package com.svnavigatoru600.web.forum.contributions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.service.forum.contributions.ShowAllContributions;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderType;

@Controller
public class ListContributionsController extends ContributionController {

    private static final String REQUEST_MAPPING_BASE_URL = ListContributionsController.BASE_URL
            + "{threadId}/prispevky/";
    /**
     * Command used in /main-content/forum/contributions/list-contributions.jsp.
     */
    public static final String COMMAND = "showAllContributionsCommand";
    private ThreadDao threadDao;

    /**
     * Constructor.
     */
    @Autowired
    public ListContributionsController(ContributionDao contributionDao, ThreadDao threadDao,
            MessageSource messageSource) {
        super(contributionDao, messageSource);
        this.threadDao = threadDao;
    }

    @RequestMapping(value = ListContributionsController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public String initPage(@PathVariable int threadId, HttpServletRequest request, ModelMap model) {

        ShowAllContributions command = new ShowAllContributions();

        List<Contribution> contributions = this.contributionDao.findOrdered(threadId, "creationTime",
                OrderType.ASCENDING);
        command.setContributions(contributions);
        command.setThread(this.threadDao.findById(threadId));

        // Sets up all auxiliary (but necessary) maps.
        command.setLocalizedDeleteQuestions(this.getLocalizedDeleteQuestions(contributions, request));

        model.addAttribute(ListContributionsController.COMMAND, command);
        return PageViews.LIST.getViewName();
    }

    @RequestMapping(value = ListContributionsController.REQUEST_MAPPING_BASE_URL + "vytvoreno/", method = RequestMethod.GET)
    public String initPageAfterCreate(@PathVariable int threadId, HttpServletRequest request, ModelMap model) {
        String view = this.initPage(threadId, request, model);
        ((ShowAllContributions) model.get(ListContributionsController.COMMAND)).setContributionCreated(true);
        return view;
    }

    @RequestMapping(value = ListContributionsController.REQUEST_MAPPING_BASE_URL + "smazano/", method = RequestMethod.GET)
    public String initPageAfterDelete(@PathVariable int threadId, HttpServletRequest request, ModelMap model) {
        String view = this.initPage(threadId, request, model);
        ((ShowAllContributions) model.get(ListContributionsController.COMMAND)).setContributionDeleted(true);
        return view;
    }

    /**
     * Gets a {@link Map} which for each input {@link Contribution} contains an appropriate localized delete
     * questions.
     */
    private Map<Contribution, String> getLocalizedDeleteQuestions(List<Contribution> contributions,
            HttpServletRequest request) {
        final String messageCode = "forum.contributions.do-you-really-want-to-delete-contribution";
        String question = Localization.findLocaleMessage(this.messageSource, request, messageCode);
        Map<Contribution, String> questions = new HashMap<Contribution, String>();

        for (Contribution contribution : contributions) {
            questions.put(contribution, question);
        }
        return questions;
    }
}
