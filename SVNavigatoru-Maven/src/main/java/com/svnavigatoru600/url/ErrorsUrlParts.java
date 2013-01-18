package com.svnavigatoru600.url;

/**
 * Contains snippets of URL which concern just error web pages.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class ErrorsUrlParts {

    public static final String BASE_URL = "/chyby/";
    public static final String ERROR_403 = ErrorsUrlParts.BASE_URL + "403/";
    public static final String ERROR_404 = ErrorsUrlParts.BASE_URL + "404/";

    private ErrorsUrlParts() {
    }
}
