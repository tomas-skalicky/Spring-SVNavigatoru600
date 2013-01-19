package com.svnavigatoru600.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.users.NotificationType;
import com.svnavigatoru600.domain.users.User;
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

    private static final String ATTACHED_FILE_CODE = "attached-file";
    private static final String LINK_TO_DOWNLOAD_PAGE_CODE = "notifications.email.record.link-to-download-page";
    private static final String NOTIFICATIONS_EMAIL_TEXT_SIGNATURE_CODE = "notifications.email.text.signature";
    private static final String NOTIFICATIONS_EMAIL_TEXT_UNSUBSCRIPTION_LINK_TEXT_CODE = "notifications.email.text.unsubscription-link-text";

    /**
     * Trivial getter
     * 
     * @return the NOTIFICATION_TYPE
     */
    protected abstract NotificationType getNotificationType();

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
        String unsubscriptionUrl = UserAccountUrlParts.getUrlForUnsubscription(user,
                this.getNotificationType(), request);
        String hereClickToUnsubscribe = String.format("<a href='%s'>%s</a>", unsubscriptionUrl, linkText);

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

    /**
     * Trivial localization
     */
    protected String getLocalizedAttachedFile(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                AbstractNotificationEmailService.ATTACHED_FILE_CODE);
    }

    /**
     * Trivial localization
     */
    protected String getLocalizedLinkToDownloadPage(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                AbstractNotificationEmailService.LINK_TO_DOWNLOAD_PAGE_CODE);
    }

    /**
     * Composes an HTML code which provides the user with the given <code>fileUrl</code> (i.e. the locator
     * where the user can download a file) when he clicks on the localized {@link #LINK_TO_DOWNLOAD_PAGE_CODE}
     * .
     */
    protected String getAttachedFileUrlHtml(String fileUrl, HttpServletRequest request,
            MessageSource messageSource) {
        String linkText = this.getLocalizedLinkToDownloadPage(request, messageSource);
        return this.getFileUrlHtml(fileUrl, linkText, request, messageSource);
    }

    /**
     * Composes an HTML code which provides the user with the given <code>fileUrl</code> (i.e. the locator
     * where the user can download a file) when he clicks on the given <code>linkText</code>.
     */
    protected String getFileUrlHtml(String fileUrl, String linkText, HttpServletRequest request,
            MessageSource messageSource) {
        return String.format("<a href\"%s\">%s</a>", fileUrl, linkText);
    }
}
