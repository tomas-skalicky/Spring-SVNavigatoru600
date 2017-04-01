package com.svnavigatoru600.url;

/**
 * Contains snippets of URL which concern just web pages with Remostav contacts.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class RemostavContactsUrlParts {

    public static final String BASE_URL = "/remostav/kontakt/";
    public static final String EDIT_URL = RemostavContactsUrlParts.BASE_URL + WysiwygCommonUrlParts.EDIT_EXTENSION;
    public static final String SAVE_EDIT_URL = RemostavContactsUrlParts.BASE_URL
            + WysiwygCommonUrlParts.SAVE_EDIT_EXTENSION;
    public static final String SAVE_EDIT_AND_EXIT_URL = RemostavContactsUrlParts.BASE_URL
            + WysiwygCommonUrlParts.SAVE_EDIT_AND_EXIT_EXTENSION;
    public static final String DONT_SAVE_EDIT_AND_EXIT_URL = RemostavContactsUrlParts.BASE_URL
            + WysiwygCommonUrlParts.DONT_SAVE_EDIT_AND_EXIT_EXTENSION;

    private RemostavContactsUrlParts() {
    }
}
