package com.svnavigatoru600.web.url.records;

import javax.servlet.http.HttpServletRequest;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.service.util.HttpRequestUtils;
import com.svnavigatoru600.web.url.CommonUrlParts;

/**
 * Contains snippets of URL which are common for most web pages with
 * {@link com.svnavigatoru600.domain.records.AbstractDocumentRecord document records}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class RecordsCommonUrlParts {

    public static final String DOWNLOAD_EXTENSION = "stahnout/";

    private RecordsCommonUrlParts() {
    }

    /**
     * Composes an absolute URL where the {@link AbstractDocumentRecord#getFile() file} attached to the given
     * {@link AbstractDocumentRecord record} is available for download.
     * 
     * @param sectionBaseUrl
     *            For instance "/dalsi-dokumenty/"
     */
    public static String getAttachedFileUrl(AbstractDocumentRecord record, String sectionBaseUrl,
            HttpServletRequest request) {
        return String.format("%s%s%s%d/%s", HttpRequestUtils.getContextHomeDirectory(request), sectionBaseUrl,
                CommonUrlParts.EXISTING_EXTENSION, record.getId(), RecordsCommonUrlParts.DOWNLOAD_EXTENSION);
    }
}
