package com.svnavigatoru600.service.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides a set of static functions related to {@link HttpServletRequest HttpServletRequests}.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class HttpRequestUtils {

    private static final int DEFAULT_HTTP_PORT = 80;

    private HttpRequestUtils() {
    }

    /**
     * Extracts the server name and port from the given {@link HttpServletRequest request}.
     * <p>
     * NOTE: Both of these parts (name and port) are always present.
     *
     * @return For instance <code>localhost:9980</code> or <code>www.svnavigatoru600.com:80</code>.
     */
    public static String getServerNameAndPort(HttpServletRequest request) {
        String schema = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        if (serverPort == HttpRequestUtils.DEFAULT_HTTP_PORT) {
            return String.format("%s://%s", schema, serverName);
        } else {
            return String.format("%s://%s:%d", schema, serverName, serverPort);
        }
    }

    /**
     * Gets a context home.
     * <p>
     * Example:
     * <ul>
     * <li>The app is running in the production environment <code>www.svnavigatoru600.com</code> in the default (root)
     * context =&gt; its context home is <code><b>www.svnavigatoru600:80</b></code></li>
     * <li>The app is running in the development environment <code>localhost:9980</code> in the
     * <code>svnavigatoru600</code> context =&gt; its context home is <code><b>localhost:9980/svnavigatoru600</b></code>
     * </li>
     * </ul>
     * <p>
     * NOTE: The returned context home does never end up with "/".
     */
    public static String getContextHomeDirectory(HttpServletRequest request) {
        return HttpRequestUtils.getServerNameAndPort(request) + request.getContextPath();
    }
}
