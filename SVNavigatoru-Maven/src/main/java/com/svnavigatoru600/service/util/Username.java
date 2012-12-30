package com.svnavigatoru600.service.util;

/**
 * Provides a set of static functions related to usernames.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class Username {

    private static final String VALID_USERNAME_REGEXP = "^[\\w\\-]{3,}$";

    private Username() {
    }

    /**
     * Indicates whether the given <code>username</code> is valid.
     */
    public static boolean isValid(String username) {
        return username.matches(Username.VALID_USERNAME_REGEXP);
    }
}
