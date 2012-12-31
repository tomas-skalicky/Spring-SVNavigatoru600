package com.svnavigatoru600.web.news;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.service.news.NewsService;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.viewmodel.news.ShowAllNews;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListNewsController extends AbstractNewsController {

    private static final String REQUEST_MAPPING_BASE_URL = ListNewsController.BASE_URL;
    /**
     * Command used in /main-content/news/list-news.jsp.
     */
    public static final String COMMAND = "showAllNewsCommand";

    private NewNewsController newNewsController;

    @Inject
    public void setNewNewsController(NewNewsController controller) {
        this.newNewsController = controller;
    }

    /**
     * Constructor.
     */
    @Inject
    public ListNewsController(NewsService newsService, MessageSource messageSource) {
        super(newsService, messageSource);
    }

    @RequestMapping(value = ListNewsController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public String initPage(HttpServletRequest request, ModelMap model) {

        ShowAllNews command = new ShowAllNews();

        List<News> news = this.newsService.findAllOrdered();
        command.setNews(news);

        // Sets up all auxiliary (but necessary) maps.
        command.setLocalizedDeleteQuestions(this.getLocalizedDeleteQuestions(news, request));

        model.addAttribute(ListNewsController.COMMAND, command);

        this.newNewsController.initForm(request, model);

        return PageViews.LIST.getViewName();
    }

    /**
     * Gets a {@link Map} which for each input {@link News} contains an appropriate localized delete
     * questions.
     */
    private Map<News, String> getLocalizedDeleteQuestions(List<News> newss, HttpServletRequest request) {
        Map<News, String> questions = new HashMap<News, String>();
        for (News news : newss) {
            questions.put(news, getLocalizedDeleteQuestion(news, this.messageSource, request));
        }
        return questions;
    }

    /**
     * For the given {@link News}, gets an appropriate localized delete questions.
     */
    static String getLocalizedDeleteQuestion(News news, MessageSource messageSource,
            HttpServletRequest request) {
        final String messageCode = "news.do-you-really-want-to-delete-news";
        Object[] messageParams = new Object[] { news.getTitle() };
        return Localization.findLocaleMessage(messageSource, request, messageCode, messageParams);
    }
}
