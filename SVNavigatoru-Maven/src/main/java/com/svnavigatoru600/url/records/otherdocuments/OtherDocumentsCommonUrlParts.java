package com.svnavigatoru600.url.records.otherdocuments;

import javax.servlet.http.HttpServletRequest;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.url.records.RecordsCommonUrlParts;

/**
 * Contains snippets of URL which are common for most web pages with
 * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class OtherDocumentsCommonUrlParts {

    static final String BASE_URL = "/dalsi-dokumenty/";

    private OtherDocumentsCommonUrlParts() {
    }

    /**
     * Composes an absolute URL where the {@link OtherDocumentRecord#getFile() file} attached to the given
     * {@link OtherDocumentRecord record} is available for download.
     */
    public static String getAttachedFileUrl(OtherDocumentRecord record, HttpServletRequest request) {
        return RecordsCommonUrlParts.getAttachedFileUrl(record, OtherDocumentsCommonUrlParts.BASE_URL, request);
    }
}
