package com.svnavigatoru600.url.records.session;

import javax.servlet.http.HttpServletRequest;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.url.records.RecordsCommonUrlParts;

/**
 * Contains snippets of URL which are common for most web pages with
 * {@link com.svnavigatoru600.domain.records.SessionRecord SessionRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class SessionsCommonUrlParts {

    static final String BASE_URL = "/zapisy-z-jednani/";

    private SessionsCommonUrlParts() {
    }

    /**
     * Composes an absolute URL where the {@link SessionRecord#getFile() file} attached to the given
     * {@link SessionRecord record} is available for download.
     */
    public static String getAttachedFileUrl(SessionRecord record, HttpServletRequest request) {
        return RecordsCommonUrlParts.getAttachedFileUrl(record, SessionsCommonUrlParts.BASE_URL, request);
    }
}
