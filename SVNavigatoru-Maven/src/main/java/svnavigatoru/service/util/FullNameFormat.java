package svnavigatoru.service.util;

/**
 * Enumeration of all possible formats of the full name of the {@link svnavigatoru.domain.users.User}.
 * 
 * @author Tomas Skalicky
 */
public enum FullNameFormat {
	
	/**
	 * For example: "Tomas Skalicky"
	 */
	FIRST_LAST,
	
	/**
	 * For example: "Skalicky Tomas"
	 */
	LAST_FIRST,
	
	/**
	 * For example: "Skalicky, Tomas"
	 */
	LAST_COMMA_FIRST
}
