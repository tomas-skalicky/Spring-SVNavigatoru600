package com.svnavigatoru600.url.records.session;

import com.svnavigatoru600.url.CommonUrlParts;

/**
 * Contains snippets of URL which concern just web pages with only board
 * {@link com.svnavigatoru600.domain.records.SessionRecord SessionRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class BoardSessionsUrlParts {

    public static final String BASE_URL = SessionsCommonUrlParts.BASE_URL + "vybor/";
    public static final String NEW_URL = BoardSessionsUrlParts.BASE_URL + CommonUrlParts.NEW_EXTENSION;
    public static final String EXISTING_URL = BoardSessionsUrlParts.BASE_URL + CommonUrlParts.EXISTING_EXTENSION;
    public static final String CREATED_URL = BoardSessionsUrlParts.BASE_URL + CommonUrlParts.CREATED_EXTENSION;
    public static final String DELETED_URL = BoardSessionsUrlParts.BASE_URL + CommonUrlParts.DELETED_EXTENSION;

    private BoardSessionsUrlParts() {
    }
}
