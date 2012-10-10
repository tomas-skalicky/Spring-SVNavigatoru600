package svnavigatoru.service.util;

import javax.servlet.ServletRequest;

/**
 * Provides a set of static functions related to URL.
 * 
 * @author Tomas Skalicky
 */
public class Url {

	/**
	 * Gets the path of the current servlet associated with the given
	 * <code>request</code>.
	 */
	public static String getServletPath(ServletRequest request) {
		return (String) request.getAttribute("javax.servlet.forward.servlet_path");
	}
}
