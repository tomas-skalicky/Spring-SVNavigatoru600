package com.svnavigatoru600.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.users.NotificationType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.util.HtmlUtils;
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
    private static final String DO_DOWNLOAD_CODE = "notifications.email.record.do-download";
    private static final String SHOW_WHOLE_TEXT_CODE = "notifications.email.show-whole-text";
    private static final String NOTIFICATIONS_EMAIL_TEXT_SIGNATURE_CODE = "notifications.email.text.signature";
    private static final String NOTIFICATIONS_EMAIL_TEXT_UNSUBSCRIPTION_LINK_TEXT_CODE = "notifications.email.text.unsubscription-link-text";
    private static final int TEXT_MAX_LENGTH = 100;

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

        String sectionWhichIsToBeUnsubscribed = Localization.findLocaleMessage(messageSource, request,
                getNotificationType().getTitleLocalizationCode());
        String linkText = Localization.findLocaleMessage(messageSource, request,
                AbstractNotificationEmailService.NOTIFICATIONS_EMAIL_TEXT_UNSUBSCRIPTION_LINK_TEXT_CODE);
        String unsubscriptionUrl = UserAccountUrlParts.getUrlForUnsubscription(user, getNotificationType(),
                request);
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
     * Composes an HTML code which provides the user with the given <code>fileUrl</code> (i.e. the locator of
     * the desired file) when he clicks on the localized {@link #DO_DOWNLOAD_CODE}.
     */
    protected String getAttachedFileUrlHtml(String fileUrl, HttpServletRequest request,
            MessageSource messageSource) {
        String linkText = getLocalizedDoDownload(request, messageSource);
        return getFileUrlHtml(fileUrl, linkText, request, messageSource);
    }

    /**
     * Composes an HTML code which provides the user with the given <code>fileUrl</code> (i.e. the locator of
     * the desired file) when he clicks on the given <code>linkText</code>.
     */
    protected String getFileUrlHtml(String fileUrl, String linkText, HttpServletRequest request,
            MessageSource messageSource) {
        return String.format("<a href=\"%s\">%s</a>", fileUrl, linkText);
    }

    /**
     * Crops the given <code>text</code> in a way that the new length is not beyond the upper bound determined
     * by the {@link #TEXT_MAX_LENGTH} constant. If the text is cropped, adds an link at the end of the text
     * with the <code>wholeTextUrl</code>. If the text is not too long, adds nothing.
     * <p>
     * If the text is an HTML code, the cropping is done carefully in order to prevent breaking code
     * well-formed-ness (all tags correctly closed). Values of attributes are not count to the length of the
     * text which decide whether to crop it, or not.
     * 
     * @param text
     *            Either a plain text or an HTML code.
     * @param wholeTextUrl
     *            URL which represents a web page with the whole <code>text</code>
     * @return Cropped <code>text</code> with a link to the whole contents if necessary; otherwise the
     *         original <code>text</code>.
     */
    protected String cropTooLongTextAndAddLink(String text, String wholeTextUrl, HttpServletRequest request,
            MessageSource messageSource) {
        String croppedText = HtmlUtils.cropEscaped(text, AbstractNotificationEmailService.TEXT_MAX_LENGTH);
        if (!croppedText.equals(text)) {
            return String.format("%s... <a href=\"%s\">%s</a>", croppedText, wholeTextUrl,
                    getLocalizedShowWholeText(request, messageSource));
        } else {
            return text;
        }
    }

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
    protected String getLocalizedDoDownload(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                AbstractNotificationEmailService.DO_DOWNLOAD_CODE);
    }

    /**
     * Trivial localization
     */
    protected String getLocalizedShowWholeText(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                AbstractNotificationEmailService.SHOW_WHOLE_TEXT_CODE);
    }

    /**
     * Trivial getter
     * 
     * @return the TEXT_MAX_LENGTH
     */
    public static int getTextMaxLength() {
        return AbstractNotificationEmailService.TEXT_MAX_LENGTH;
    }
}
