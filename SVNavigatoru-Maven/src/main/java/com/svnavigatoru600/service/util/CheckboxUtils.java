package com.svnavigatoru600.service.util;

/**
 * The utility class which provides a functionality mutual for all sets of checkboxes.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class CheckboxUtils {

    private CheckboxUtils() {
    }

    /**
     * Creates an array of flags which say which checkboxes are checked, and which checkboxes are not. All flags in the
     * resulting array are set to <code>false</code>, i.e. no checkbox is checked.
     */
    static boolean[] createArrayOfCheckIndicators(int typeCount) {
        boolean[] indicators = new boolean[typeCount];
        for (int typeNum = 0; typeNum < typeCount; ++typeNum) {
            indicators[typeNum] = false;
        }
        return indicators;
    }

    /**
     * Compares whether the given arrays of check flags are same. The flags say which checkboxes are selected and which
     * are not.
     */
    public static boolean areSame(boolean[] arrayOfCheckIndicators1, boolean[] arrayOfCheckIndicators2) {
        if (arrayOfCheckIndicators1.length != arrayOfCheckIndicators2.length) {
            throw new RuntimeException("Both arrays must have the same length.");
        }

        for (int indicatorNum = 0; indicatorNum < arrayOfCheckIndicators1.length; ++indicatorNum) {
            if (arrayOfCheckIndicators1[indicatorNum] != arrayOfCheckIndicators2[indicatorNum]) {
                return false;
            }
        }
        return true;
    }
}
