package com.svnavigatoru600.web.news;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import com.svnavigatoru600.service.news.NewsService;
import com.svnavigatoru600.service.news.validator.AbstractNewsValidator;

/**
 * Parent of controllers which create and edit the {@link com.svnavigatoru600.domain.News News}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractNewEditNewsController extends AbstractNewsController {

    /**
     * Command used in /main-content/news/new-edit-news.jsp.
     */
    public static final String COMMAND = "newEditNewsCommand";
    private Validator validator;

    public AbstractNewEditNewsController(NewsService newsService, AbstractNewsValidator validator,
            MessageSource messageSource) {
        super(newsService, messageSource);
        this.validator = validator;
    }

    /**
     * Trivial getter
     */
    protected Validator getValidator() {
        return this.validator;
    }
}
