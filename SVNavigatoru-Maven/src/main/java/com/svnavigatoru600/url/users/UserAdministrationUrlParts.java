package com.svnavigatoru600.url.users;

import com.svnavigatoru600.url.CommonUrlParts;

/**
 * Contains snippets of URL which concern just user administration web pages.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class UserAdministrationUrlParts {

    public static final String BASE_URL = "/administrace-uzivatelu/";
    public static final String NEW_URL = UserAdministrationUrlParts.BASE_URL + CommonUrlParts.NEW_EXTENSION;
    public static final String EXISTING_URL = UserAdministrationUrlParts.BASE_URL + CommonUrlParts.EXISTING_EXTENSION;
    public static final String CREATED_URL = UserAdministrationUrlParts.BASE_URL + CommonUrlParts.CREATED_EXTENSION;
    public static final String DELETED_URL = UserAdministrationUrlParts.BASE_URL + CommonUrlParts.DELETED_EXTENSION;

    private UserAdministrationUrlParts() {
    }
}
