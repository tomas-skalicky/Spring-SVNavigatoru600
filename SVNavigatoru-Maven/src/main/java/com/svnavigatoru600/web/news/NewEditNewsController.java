package com.svnavigatoru600.web.news;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import com.svnavigatoru600.service.news.NewsService;
import com.svnavigatoru600.service.news.validator.NewsValidator;

/**
 * Parent of controllers which create and edit the {@link com.svnavigatoru600.domain.News News}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class NewEditNewsController extends NewsController {

    /**
     * Command used in /main-content/news/new-edit-news.jsp.
     */
    public static final String COMMAND = "newEditNewsCommand";
    protected Validator validator;

    public NewEditNewsController(NewsService newsService, NewsValidator validator, MessageSource messageSource) {
        super(newsService, messageSource);
        this.validator = validator;
    }
}
