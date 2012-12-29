package com.svnavigatoru600.service.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Provides a set of static functions related to passwords.
 * 
 * @author Tomas Skalicky
 */
public final class Password {

    private static final String VALID_PASSWORD_REGEXP = "^[^ ]{6,}$";
    /**
     * The length of the generated password {@link String}.
     */
    private static final int PASSWORD_LENGTH = 8;

    private Password() {
    }

    /**
     * Indicates whether the given <code>password</code> is valid in terms of its format.
     */
    public static boolean isValid(String password) {
        return password.matches(Password.VALID_PASSWORD_REGEXP);
    }

    /**
     * Gets a new generated password.
     */
    public static String generateNew() {
        return RandomStringUtils.randomAlphanumeric(PASSWORD_LENGTH);
    }
}
