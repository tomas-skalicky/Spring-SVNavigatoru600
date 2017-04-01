package com.svnavigatoru600.url;

/**
 * Contains snippets of URL which concern just web pages with useful links.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class UsefulLinksUrlParts {

    public static final String BASE_URL = "/uzitecne-odkazy/";
    public static final String EDIT_URL = UsefulLinksUrlParts.BASE_URL + WysiwygCommonUrlParts.EDIT_EXTENSION;
    public static final String SAVE_EDIT_URL = UsefulLinksUrlParts.BASE_URL + WysiwygCommonUrlParts.SAVE_EDIT_EXTENSION;
    public static final String SAVE_EDIT_AND_EXIT_URL = UsefulLinksUrlParts.BASE_URL
            + WysiwygCommonUrlParts.SAVE_EDIT_AND_EXIT_EXTENSION;
    public static final String DONT_SAVE_EDIT_AND_EXIT_URL = UsefulLinksUrlParts.BASE_URL
            + WysiwygCommonUrlParts.DONT_SAVE_EDIT_AND_EXIT_EXTENSION;

    private UsefulLinksUrlParts() {
    }
}
