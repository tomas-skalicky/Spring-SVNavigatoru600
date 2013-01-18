package com.svnavigatoru600.url.records.otherdocuments;

import com.svnavigatoru600.url.CommonUrlParts;

/**
 * Contains snippets of URL which concern just web pages with only Remostav
 * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class RemostavDocumentsUrlParts {

    public static final String BASE_URL = "/remostav/dokumentace/";
    public static final String NEW_URL = RemostavDocumentsUrlParts.BASE_URL + CommonUrlParts.NEW_EXTENSION;
    public static final String EXISTING_URL = RemostavDocumentsUrlParts.BASE_URL
            + CommonUrlParts.EXISTING_EXTENSION;
    public static final String CREATED_URL = RemostavDocumentsUrlParts.BASE_URL
            + CommonUrlParts.CREATED_EXTENSION;
    public static final String DELETED_URL = RemostavDocumentsUrlParts.BASE_URL
            + CommonUrlParts.DELETED_EXTENSION;

    private RemostavDocumentsUrlParts() {
    }
}
