package com.svnavigatoru600.web.url.users;

import javax.servlet.http.HttpServletRequest;

import com.svnavigatoru600.domain.users.NotificationTypeEnum;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.util.HttpRequestUtils;
import com.svnavigatoru600.web.url.CommonUrlParts;

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
    public static final String UNSUBSCRIBE_FAILED_FOREIGN_ACCOUNT_URL = UserAccountUrlParts.UNSUBSCRIBE_URL
            + CommonUrlParts.FAIL_EXTENSION + "cizi-ucet/";

    private UserAccountUrlParts() {
    }

    /**
     * Gets URL which unsubscribes the given {@link User user} from receiving notifications of the given
     * {@link NotificationTypeEnum notificationType} if the user clicks on it. Moreover, controller associated with the URL
     * redirects the user to the user account settings page.
     */
    public static String getUrlForUnsubscription(User user, NotificationTypeEnum notificationType,
            HttpServletRequest request) {
        return String.format("%s%s%s/%s%d/", HttpRequestUtils.getContextHomeDirectory(request),
                UserAccountUrlParts.BASE_URL, user.getUsername(), UserAccountUrlParts.UNSUBSCRIBE_EXTENSION,
                notificationType.ordinal());
    }
}
