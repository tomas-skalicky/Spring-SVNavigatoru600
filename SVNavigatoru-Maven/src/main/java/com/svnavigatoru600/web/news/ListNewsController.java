package com.svnavigatoru600.web.news;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.service.news.NewsService;
import com.svnavigatoru600.url.news.NewsUrlParts;
import com.svnavigatoru600.viewmodel.news.ShowAllNews;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListNewsController extends AbstractNewsController {

    /**
     * Command used in /main-content/news/list-news.jsp.
     */
    public static final String COMMAND = "showAllNewsCommand";

    private NewNewsController newNewsController;

    @Inject
    public void setNewNewsController(final NewNewsController controller) {
        newNewsController = controller;
    }

    @Inject
    public ListNewsController(final NewsService newsService, final MessageSource messageSource) {
        super(newsService, messageSource);
    }

    @GetMapping(value = NewsUrlParts.BASE_URL)
    public String initPage(final HttpServletRequest request, final ModelMap model) {

        final ShowAllNews command = new ShowAllNews();

        final List<News> news = getNewsService().findAllOrdered();
        command.setNews(news);

        // Sets up all auxiliary (but necessary) maps.
        command.setLocalizedDeleteQuestions(NewsService.getLocalizedDeleteQuestions(news, request, getMessageSource()));

        model.addAttribute(ListNewsController.COMMAND, command);

        newNewsController.initForm(request, model);

        return PageViews.LIST.getViewName();
    }
}
