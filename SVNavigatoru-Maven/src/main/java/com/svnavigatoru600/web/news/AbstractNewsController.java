package com.svnavigatoru600.web.news;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.service.news.NewsService;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * Parent of all controllers which handle all operations upon the {@link com.svnavigatoru600.domain.News News} .
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractNewsController extends AbstractPrivateSectionMetaController {

    private final NewsService newsService;
    private final MessageSource messageSource;

    public AbstractNewsController(final NewsService newsService, final MessageSource messageSource) {
        this.newsService = newsService;
        this.messageSource = messageSource;
    }

    /**
     * Trivial getter
     */
    protected NewsService getNewsService() {
        return newsService;
    }

    /**
     * Trivial getter
     */
    protected MessageSource getMessageSource() {
        return messageSource;
    }
}
