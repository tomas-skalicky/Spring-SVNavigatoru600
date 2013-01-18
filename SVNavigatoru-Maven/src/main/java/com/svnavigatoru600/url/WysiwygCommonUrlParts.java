package com.svnavigatoru600.url;

/**
 * Contains snippets of URL which are common for all web pages with
 * {@link com.svnavigatoru600.domain.WysiwygSection WysiwygSections}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class WysiwygCommonUrlParts {

    public static final String EDIT_EXTENSION = "editace/";
    public static final String SAVE_EDIT_EXTENSION = WysiwygCommonUrlParts.EDIT_EXTENSION + "ulozit/";
    public static final String SAVE_EDIT_AND_EXIT_EXTENSION = WysiwygCommonUrlParts.EDIT_EXTENSION
            + "ulozit-a-skoncit/";
    public static final String DONT_SAVE_EDIT_AND_EXIT_EXTENSION = WysiwygCommonUrlParts.EDIT_EXTENSION
            + "neukladat-a-skoncit/";

    private WysiwygCommonUrlParts() {
    }
}
