package com.svnavigatoru600.service.util;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;

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

    /**
     * Converts all occurrences of relative URLs of images in the given <code>text</code> to corresponding
     * absolute URLs.
     * <p>
     * Sample:
     * 
     * <pre>
     * /tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-undecided.gif
     * 
     * =&gt; www.svnavigatoru600.com/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-undecided.gif
     * </pre>
     * 
     * @param text
     *            Contains relative URLs of images
     * @param serverNameAndPort
     *            E.g. www.svnavigatoru600.com:8080
     * @return Text only with absolute URLs of images
     */
    public static String convertImageRelativeUrlsToAbsolute(String text, String serverNameAndPort) {
        return StringUtils.replace(text, "src=\"/", "src=\"" + serverNameAndPort + "/");
    }
}
