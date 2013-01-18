package com.svnavigatoru600.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.users.NotificationType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.util.HttpRequestUtils;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.url.users.UserAccountUrlParts;

/**
 * Ancestor of all {@link Service Services} which provide sending of emails concerning notifications of new
 * posts and updates.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractNotificationEmailService extends AbstractEmailService {

    private static final String NOTIFICATIONS_EMAIL_TEXT_SIGNATURE_CODE = "notifications.email.text.signature";
    private static final String NOTIFICATIONS_EMAIL_TEXT_UNSUBSCRIPTION_LINK_TEXT_CODE = "notifications.email.text.unsubscription-link-text";

    /**
     * Trivial getter
     * 
     * @return the NOTIFICATION_TYPE
     */
    protected abstract NotificationType getNotificationType();

    /**
     * Gets URL which unsubscribes the currently logged user from receiving notifications of this
     * {@link #getNotificationType() type} if the user clicks on it. Moreover, controller associated with the
     * URL redirects the user to the settings page of the user account.
     */
    private String getLinkForUnsubscription(User user, HttpServletRequest request) {
        return String.format("%s%s%s/%s%d/", HttpRequestUtils.getContextHomeDirectory(request),
                UserAccountUrlParts.BASE_URL, user.getUsername(), UserAccountUrlParts.UNSUBSCRIBE_EXTENSION,
                this.getNotificationType().ordinal());
    }

    /**
     * Gets a localized signature of emails which serve as notifications of news and changes.
     * 
     * @param user
     *            {@link User} which is to be unsubscribed.
     */
    protected String getLocalizedNotificationSignature(User user, HttpServletRequest request,
            MessageSource messageSource) {

        String sectionWhichIsToBeUnsubscribed = Localization.findLocaleMessage(messageSource, request, this
                .getNotificationType().getTitleLocalizationCode());
        String linkText = Localization.findLocaleMessage(messageSource, request,
                AbstractNotificationEmailService.NOTIFICATIONS_EMAIL_TEXT_UNSUBSCRIPTION_LINK_TEXT_CODE);
        String hereClickToUnsubscribe = String.format("<a href='%s'>%s</a>",
                this.getLinkForUnsubscription(user, request), linkText);

        Object[] messageParams = new Object[] { Configuration.DOMAIN, sectionWhichIsToBeUnsubscribed,
                hereClickToUnsubscribe };
        return Localization.findLocaleMessage(messageSource, request,
                AbstractNotificationEmailService.NOTIFICATIONS_EMAIL_TEXT_SIGNATURE_CODE, messageParams);
    }

    /**
     * Sends emails to the given {@link User Users} with notification of the newly posted {@link Object}.
     * 
     * @param newObject
     *            Newly posted {@link com.svnavigatoru600.domain.News News},
     *            {@link com.svnavigatoru600.domain.eventcalendar.CalendarEvent CalendarEvent} or something
     *            else.
     */
    public abstract void sendEmailOnCreation(Object newObject, List<User> usersToNotify,
            HttpServletRequest request, MessageSource messageSource);

    /**
     * Sends emails to the given {@link User Users} with notification of changes in the given {@link Object}.
     * 
     * @param updatedObject
     *            Updated {@link com.svnavigatoru600.domain.News News},
     *            {@link com.svnavigatoru600.domain.eventcalendar.CalendarEvent CalendarEvent} or something
     *            else.
     */
    public abstract void sendEmailOnUpdate(Object updatedObject, List<User> usersToNotify,
            HttpServletRequest request, MessageSource messageSource);
}
