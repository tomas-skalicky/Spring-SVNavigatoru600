package com.svnavigatoru600.web.news;

import org.springframework.context.MessageSource;
import org.springframework.validation.Validator;

import com.svnavigatoru600.service.news.NewsService;
import com.svnavigatoru600.viewmodel.news.validator.AbstractNewsValidator;
import com.svnavigatoru600.web.SendNotificationController;

/**
 * Parent of controllers which create and edit the {@link com.svnavigatoru600.domain.News News}.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractNewEditNewsController extends AbstractNewsController
        implements SendNotificationController {

    /**
     * Command used in /main-content/news/new-edit-news.jsp.
     */
    public static final String COMMAND = "newEditNewsCommand";
    private final Validator validator;

    public AbstractNewEditNewsController(final NewsService newsService, final AbstractNewsValidator validator,
            final MessageSource messageSource) {
        super(newsService, messageSource);
        this.validator = validator;
    }

    /**
     * Trivial getter
     */
    protected Validator getValidator() {
        return validator;
    }
}
