package com.svnavigatoru600.web.news;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import com.svnavigatoru600.service.news.NewsService;
import com.svnavigatoru600.web.PrivateSectionMetaController;

/**
 * Parent of all controllers which handle all operations upon the {@link com.svnavigatoru600.domain.News News}
 * .
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class NewsController extends PrivateSectionMetaController {

    protected static final String BASE_URL = "/novinky/";
    protected NewsService newsService;
    protected MessageSource messageSource;

    public NewsController(NewsService newsService, MessageSource messageSource) {
        this.newsService = newsService;
        this.messageSource = messageSource;
    }
}
