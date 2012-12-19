package com.svnavigatoru600.service.util;

/**
 * Provides a set of static functions related to conversions of types.
 * 
 * @author Tomas Skalicky
 */
public final class TypeConversion {
    
    private TypeConversion() {
    }

    /**
     * Converts the given array of <code>bytes</code> to {@link String}.
     */
    public static String bytesToString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append("0");
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
