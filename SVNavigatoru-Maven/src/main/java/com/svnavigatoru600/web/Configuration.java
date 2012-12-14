package com.svnavigatoru600.web;

import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.DocumentRecord;

/**
 * This class is a set of constants which configure the whole application.
 * 
 * @author Tomas Skalicky
 */
public class Configuration {

    /**
     * The web page domain.
     */
    public static final String DOMAIN = "www.svnavigatoru600.com";
    /**
     * The JSP page which helps to clear the {@link ModelMap}.
     */
    public static final String REDIRECTION_PAGE = "page-for-redirection";
    /**
     * The attribute which holds the destination URL of the redirection done by <code>REDIRECTION_PAGE</code>.
     */
    public static final String REDIRECTION_ATTRIBUTE = "redirectTo";
    /**
     * The directory where all files of all {@link DocumentRecord} are stored.
     */
    public static final String FILE_STORAGE = "uploaded-files/";
    /**
     * The maximal size of a file which can be uploaded [in bytes]. <small>The current value equals 2
     * MB.</small>
     */
    public static final long MAX_UPLOAD_SIZE = 2097152;
}
