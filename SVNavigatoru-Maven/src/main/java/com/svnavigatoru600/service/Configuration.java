package com.svnavigatoru600.service;

/**
 * This class is a set of constants which configure the whole application.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class Configuration {

    /**
     * The web page domain.
     */
    public static final String DOMAIN = "www.svnavigatoru600.com";
    /**
     * The default protocol used during browsing on the web page.
     */
    public static final String DEFAULT_PROTOCOL = "http://";
    /**
     * The directory where all files of all {@link com.svnavigatoru600.domain.records.AbstractDocumentRecord
     * AbstractDocumentRecord} are stored.
     */
    public static final String FILE_STORAGE = "uploaded-files/";
    /**
     * The maximal size of a file which can be uploaded [in bytes]. <small>The current value equals 4
     * MB.</small>
     */
    public static final long MAX_UPLOAD_SIZE = 4194304;
    /**
     * Default encoding for everything.
     */
    public static final String DEFAULT_ENCODING = "UTF-8";

    private Configuration() {
    }
}
