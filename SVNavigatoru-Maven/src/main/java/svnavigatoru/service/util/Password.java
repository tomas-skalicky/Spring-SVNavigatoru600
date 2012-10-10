package svnavigatoru.service.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Provides a set of static functions related to passwords.
 * 
 * @author Tomas Skalicky
 */
public class Password {

	private static final String VALID_PASSWORD_REGEXP = "^[^ ]{6,}$";

	/**
	 * Indicates whether the given <code>password</code> is valid in terms of
	 * its format.
	 */
	public static boolean isValid(String password) {
		return password.matches(Password.VALID_PASSWORD_REGEXP);
	}

	/**
	 * Gets a new generated password.
	 */
	public static String generateNew() {
		return RandomStringUtils.randomAlphanumeric(8);
	}
}
