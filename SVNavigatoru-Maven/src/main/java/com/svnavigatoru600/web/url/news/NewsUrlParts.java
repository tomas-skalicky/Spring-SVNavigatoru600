package com.svnavigatoru600.web.url.news;

import javax.servlet.http.HttpServletRequest;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.service.util.HttpRequestUtils;
import com.svnavigatoru600.web.url.CommonUrlParts;

/**
 * Contains snippets of URL which concern just web pages with only {@link com.svnavigatoru600.domain.News News}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class NewsUrlParts {

    public static final String BASE_URL = "/novinky/";
    public static final String NEW_URL = NewsUrlParts.BASE_URL + CommonUrlParts.NEW_EXTENSION;
    public static final String EXISTING_URL = NewsUrlParts.BASE_URL + CommonUrlParts.EXISTING_EXTENSION;
    public static final String CREATED_URL = NewsUrlParts.BASE_URL + CommonUrlParts.CREATED_EXTENSION;
    public static final String DELETED_URL = NewsUrlParts.BASE_URL + CommonUrlParts.DELETED_EXTENSION;

    private NewsUrlParts() {
    }

    /**
     * Composes an absolute URL which holds details about the given {@link News}.
     */
    public static String getNewsUrl(News news, HttpServletRequest request) {
        return String.format("%s%s#post_%d", HttpRequestUtils.getContextHomeDirectory(request), NewsUrlParts.BASE_URL,
                news.getId());
    }
}
