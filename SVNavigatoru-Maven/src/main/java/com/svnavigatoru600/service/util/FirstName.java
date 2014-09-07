package com.svnavigatoru600.service.util;

/**
 * Provides a set of static functions related to first names.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class FirstName {

    /**
     * No digits + no special characters. The minimal length of the first name is 2.
     */
    static final String VALID_FIRST_NAME_REGEXP = "^[^\\d\\~\\`\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\=\\[\\]\\{\\}\\:\\;\"\\|\\\\<\\>\\,\\.\\?\\/\\§\\¨]{2,}$";

    private FirstName() {
    }

    /**
     * Indicates whether the given <code>firstName</code> is valid.
     */
    public static boolean isValid(String firstName) {
        return firstName.matches(FirstName.VALID_FIRST_NAME_REGEXP);
    }
}
