package com.svnavigatoru600.web.url.records.session;

import com.svnavigatoru600.web.url.CommonUrlParts;

/**
 * Contains snippets of URL which concern just web pages with only "sv"
 * {@link com.svnavigatoru600.domain.records.SessionRecord SessionRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class SvUrlParts {

    public static final String BASE_URL = SessionsCommonUrlParts.BASE_URL + "sv/";
    public static final String NEW_URL = SvUrlParts.BASE_URL + CommonUrlParts.NEW_EXTENSION;
    public static final String EXISTING_URL = SvUrlParts.BASE_URL + CommonUrlParts.EXISTING_EXTENSION;
    public static final String CREATED_URL = SvUrlParts.BASE_URL + CommonUrlParts.CREATED_EXTENSION;
    public static final String DELETED_URL = SvUrlParts.BASE_URL + CommonUrlParts.DELETED_EXTENSION;

    private SvUrlParts() {
    }
}
