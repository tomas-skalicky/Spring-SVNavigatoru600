package com.svnavigatoru600.service.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provides a set of static functions related to hashing.
 * 
 * @author Tomas Skalicky
 */
public final class Hash {

    private Hash() {
    }

    /**
     * Hashes the given <code>string</code> via the SHA-1 algorithm. The implementation is borrowed from <a
     * href="http://www.jguru.com/faq/view.jsp?EID=3822">jGuru</a>.
     * 
     * @param string
     *            {@link String} which is to be hashed.
     * @return The <code>string</code> hashed via the SHA-1 algorithm.
     */
    public static String doSha1Hashing(String string) {
        byte[] bytesToHash = string.getBytes();

        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        algorithm.reset();
        algorithm.update(bytesToHash);
        byte[] digest = algorithm.digest();
        return TypeConversion.bytesToString(digest);
    }
}
