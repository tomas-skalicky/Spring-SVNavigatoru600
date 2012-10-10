package svnavigatoru.service.util;

/**
 * Provides a set of static functions related to usernames.
 * 
 * @author Tomas Skalicky
 */
public class Username {

	private static final String VALID_USERNAME_REGEXP = "^[\\w\\-]{3,}$";

	/**
	 * Indicates whether the given <code>username</code> is valid.
	 */
	public static boolean isValid(String username) {
		return username.matches(Username.VALID_USERNAME_REGEXP);
	}
}
