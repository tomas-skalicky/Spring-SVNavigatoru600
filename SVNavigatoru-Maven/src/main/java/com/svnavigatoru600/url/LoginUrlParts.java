package com.svnavigatoru600.url;

/**
 * Contains snippets of URL which concern just login web pages.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class LoginUrlParts {

    public static final String BASE_URL = "/prihlaseni/";
    public static final String FAIL_URL = LoginUrlParts.BASE_URL + "neuspech/";
    public static final String FORGOTTEN_PASSWORD_URL = LoginUrlParts.BASE_URL + "zapomenute-heslo/";
    public static final String SEND_NEW_PASSWORD_URL = LoginUrlParts.BASE_URL + "poslat-nove-heslo/";

    private LoginUrlParts() {
    }
}
