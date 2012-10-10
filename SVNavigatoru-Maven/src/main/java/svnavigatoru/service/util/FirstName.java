package svnavigatoru.service.util;

/**
 * Provides a set of static functions related to first names.
 * 
 * @author Tomas Skalicky
 */
public class FirstName {

	/**
	 * No digits + no special characters. The minimal length of the first name is 2.
	 */
	static final String VALID_FIRST_NAME_REGEXP =
		"^[^\\d\\~\\`\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\=\\[\\]\\{\\}\\:\\;\"\\|\\\\<\\>\\,\\.\\?\\/\\§\\¨]{2,}$";

	/**
	 * Indicates whether the given <code>firstName</code> is valid.
	 */
	public static boolean isValid(String firstName) {
		return firstName.matches(FirstName.VALID_FIRST_NAME_REGEXP);
	}
}
