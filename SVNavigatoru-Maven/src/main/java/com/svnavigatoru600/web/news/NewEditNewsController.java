package com.svnavigatoru600.web.news;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.repository.NewsDao;
import com.svnavigatoru600.service.news.validator.NewsValidator;

/**
 * Parent of controllers which create and edit the {@link News}.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class NewEditNewsController extends NewsController {

    /**
     * Command used in /main-content/news/new-edit-news.jsp.
     */
    public static final String COMMAND = "newEditNewsCommand";
    protected Validator validator;

    public NewEditNewsController(NewsDao newsDao, NewsValidator validator, MessageSource messageSource) {
        super(newsDao, messageSource);
        this.validator = validator;
    }
}
