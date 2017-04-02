package com.svnavigatoru600.url.forum;

import com.svnavigatoru600.url.CommonUrlParts;

/**
 * Contains snippets of URL which concern just web pages with only {@link com.svnavigatoru600.domain.forum.ForumThread
 * Threads}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class ThreadsUrlParts {

    public static final String BASE_URL = "/forum/temata/";
    public static final String NEW_URL = ThreadsUrlParts.BASE_URL + CommonUrlParts.NEW_EXTENSION;
    public static final String EXISTING_URL = ThreadsUrlParts.BASE_URL + CommonUrlParts.EXISTING_EXTENSION;
    public static final String CREATED_URL = ThreadsUrlParts.BASE_URL + CommonUrlParts.CREATED_EXTENSION;
    public static final String DELETED_URL = ThreadsUrlParts.BASE_URL + CommonUrlParts.DELETED_EXTENSION;

    private ThreadsUrlParts() {
    }
}
