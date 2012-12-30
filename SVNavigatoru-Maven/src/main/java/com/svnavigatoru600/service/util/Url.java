package com.svnavigatoru600.service.util;

import javax.servlet.ServletRequest;

/**
 * Provides a set of static functions related to URL.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class Url {

    private Url() {
    }

    /**
     * Gets the path of the current servlet associated with the given <code>request</code>.
     */
    public static String getServletPath(ServletRequest request) {
        return (String) request.getAttribute("javax.servlet.forward.servlet_path");
    }
}
