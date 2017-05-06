package com.svnavigatoru600.web.url;

/**
 * Contains snippets of URL which concern just web pages with board of the owner union.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class BoardInfoUrlParts {

    public static final String BASE_URL = "/vybor/";
    public static final String EDIT_URL = BoardInfoUrlParts.BASE_URL + WysiwygCommonUrlParts.EDIT_EXTENSION;
    public static final String SAVE_EDIT_URL = BoardInfoUrlParts.BASE_URL + WysiwygCommonUrlParts.SAVE_EDIT_EXTENSION;
    public static final String SAVE_EDIT_AND_EXIT_URL = BoardInfoUrlParts.BASE_URL
            + WysiwygCommonUrlParts.SAVE_EDIT_AND_EXIT_EXTENSION;
    public static final String DONT_SAVE_EDIT_AND_EXIT_URL = BoardInfoUrlParts.BASE_URL
            + WysiwygCommonUrlParts.DONT_SAVE_EDIT_AND_EXIT_EXTENSION;

    private BoardInfoUrlParts() {
    }
}
