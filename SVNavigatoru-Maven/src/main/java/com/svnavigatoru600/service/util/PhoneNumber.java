package com.svnavigatoru600.service.util;

/**
 * Provides a set of static functions related to phone numbers.
 * 
 * @author Tomas Skalicky
 */
public final class PhoneNumber {

    private static final String VALID_PHONE_NUMBER_REGEXP = "^[+]?(\\d[ ]*){9,}$";
    
    private PhoneNumber() {
    }

    /**
     * Indicates whether the given <code>phoneNumber</code> is valid.
     */
    public static boolean isValid(String phoneNumber) {
        return phoneNumber.matches(PhoneNumber.VALID_PHONE_NUMBER_REGEXP);
    }
}
