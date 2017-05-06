package com.svnavigatoru600.common.constants;

import java.nio.charset.Charset;

import org.apache.commons.io.Charsets;

public final class CommonConstants {

    /**
     * Default encoding for everything.
     */
    @SuppressWarnings("deprecation")
    public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;
    public static final String DEFAULT_CHARSET_VALUE = DEFAULT_CHARSET.name();

    public static final String NEW_LINE = "\n";

    public static final String CSS_FILE_EXTENSION_WITH_DOT = ".css";
    public static final String JAVASCRIPT_FILE_EXTENSION_WITH_DOT = ".js";
    public static final String PNG_FILE_EXTENSION_WITH_DOT = ".png";
    public static final String JPG_FILE_EXTENSION_WITH_DOT = ".jpg";
    public static final String JPEG_FILE_EXTENSION_WITH_DOT = ".jpeg";
    public static final String JSP_FILE_EXTENSION_WITH_DOT = ".jsp";

    public static final boolean USE_CGLIB_PROXY = true;

    /**
     * The maximal size of a file which can be uploaded (in bytes). <small>The current value equals 6 MB.</small>
     */
    public static final long MAX_UPLOAD_SIZE = 6 * 1024 * 1024;

    /**
     * The directory where all files of all {@link com.svnavigatoru600.domain.records.AbstractDocumentRecord
     * AbstractDocumentRecord} are stored.
     */
    public static final String FILE_STORAGE = "uploaded-files/";

    /**
     * The default protocol used during browsing on the web page.
     */
    public static final String DEFAULT_PROTOCOL = "http://";

    /**
     * The web page domain.
     */
    public static final String DOMAIN = "www.svnavigatoru600.com";

    private CommonConstants() {
    }
}
