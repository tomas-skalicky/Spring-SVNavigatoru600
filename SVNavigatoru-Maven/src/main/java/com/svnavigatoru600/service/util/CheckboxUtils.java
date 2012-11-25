package com.svnavigatoru600.service.util;

/**
 * The utility class which provides a functionality mutual for all sets of
 * checkboxes.
 * 
 * @author Tomas Skalicky
 */
public class CheckboxUtils {

	/**
	 * <p>
	 * Creates an array of indicators which say which checkboxes are checked,
	 * and which checkboxes are not. All indicators in the resulting array are
	 * set to <code>false</code>, i.e. no checkbox is checked.
	 * </p>
	 */
	static boolean[] createArrayOfCheckIndicators(int typeCount) {
		boolean[] indicators = new boolean[typeCount];
		for (int typeNum = 0; typeNum < typeCount; ++typeNum) {
			indicators[typeNum] = false;
		}
		return indicators;
	}

	/**
	 * Compares whether the given arrays of check indicators are same. The
	 * indicators say which checkboxes are selected and which are not.
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