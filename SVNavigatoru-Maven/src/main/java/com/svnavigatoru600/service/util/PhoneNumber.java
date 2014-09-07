package com.svnavigatoru600.service.util;

/**
 * Provides a set of static functions related to phone numbers.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class PhoneNumber {

    /**
     * The regular expression which is satisfied by valid phone numbers.
     */
    private static final String VALID_PHONE_NUMBER_REGEXP = "^[+]?(\\d[ ]*){9,}$";
    /**
     * The maximal length of the phone number.
     */
    private static final int PHONE_MAX_LENGTH = 20;

    private PhoneNumber() {
    }

    /**
     * Indicates whether the given <code>phoneNumber</code> is valid.
     */
    public static boolean isValid(String phoneNumber) {
        if (isTooLong(phoneNumber)) {
            return false;
        }
        return phoneNumber.matches(PhoneNumber.VALID_PHONE_NUMBER_REGEXP);
    }

    /**
     * Indicates whether the given phone number is too long to be valid.
     */
    public static boolean isTooLong(String phoneNumber) {
        return phoneNumber.length() > PHONE_MAX_LENGTH;
    }
}
