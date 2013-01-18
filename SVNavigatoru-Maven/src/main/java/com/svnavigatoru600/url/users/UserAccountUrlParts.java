package com.svnavigatoru600.url.users;

import com.svnavigatoru600.url.CommonUrlParts;

/**
 * Contains snippets of URL which concern just user account web pages.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class UserAccountUrlParts {

    public static final String BASE_URL = "/uzivatelsky-ucet/";
    public static final String SAVED_URL = UserAccountUrlParts.BASE_URL + CommonUrlParts.SAVED_EXTENSION;
    public static final String UNSUBSCRIBE_EXTENSION = "odhlasit-notifikace/";
    public static final String UNSUBSCRIBE_URL = UserAccountUrlParts.BASE_URL
            + UserAccountUrlParts.UNSUBSCRIBE_EXTENSION;
    public static final String UNSUBSCRIBED_EXTENSION = "odhlaseno/";

    private UserAccountUrlParts() {
    }
}
