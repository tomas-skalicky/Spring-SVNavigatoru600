package svnavigatoru.util;

/**
 * Contains convenient methods for working with ports.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a> &lt;skalicky.tomas@gmail.com&gt;
 */
public final class Port {

	/**
	 * Default a port where the application should run.
	 */
	private static final int DEFAULT_PORT = 8081;//9080;

	/**
	 * Name of the property of a virtual machine. The property determines a port where the application should run. If
	 * the property is not set up, the {@link #DEFAULT_PORT} value is used instead.
	 */
	private static final String PORT_PROPERTY_NAME = "tests.selenium.port";

	public static int getPort() {
		final String customPort = System.getProperty(PORT_PROPERTY_NAME);
		if (customPort != null) {
			return Integer.valueOf(customPort).intValue();
		} else {
			return DEFAULT_PORT;
		}
	}
}
