package com.svnavigatoru600.service;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;

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
     * The directory where all files of all {@link AbstractDocumentRecord} are stored.
     */
    public static final String FILE_STORAGE = "uploaded-files/";
    /**
     * The maximal size of a file which can be uploaded [in bytes]. <small>The current value equals 2
     * MB.</small>
     */
    public static final long MAX_UPLOAD_SIZE = 2097152;

    private Configuration() {
    }
}
