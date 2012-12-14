package com.svnavigatoru600.service.util;

/**
 * Provides a set of static functions related to last names.
 * 
 * @author Tomas Skalicky
 */
public class LastName {

    static final String VALID_LAST_NAME_REGEXP = FirstName.VALID_FIRST_NAME_REGEXP;

    /**
     * Indicates whether the given <code>lastName</code> is valid.
     */
    public static boolean isValid(String lastName) {
        return lastName.matches(LastName.VALID_LAST_NAME_REGEXP);
    }
}
