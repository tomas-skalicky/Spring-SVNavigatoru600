package com.svnavigatoru600.service.news;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.domain.users.NotificationType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.AbstractNotificationEmailService;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.Url;
import com.svnavigatoru600.url.news.NewsUrlParts;

/**
 * Provide sending of emails concerning notifications of new {@link News news} and updated ones.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Service
public class NewsNotificationEmailService extends AbstractNotificationEmailService {

    private static final String NEWS_CREATED_SUBJECT_CODE = "notifications.email.news.subject.news-created";
    private static final String NEWS_CREATED_TEXT_CODE = "notifications.email.news.text.news-created";
    private static final String NEWS_UPDATED_SUBJECT_CODE = "notifications.email.news.subject.news-updated";
    private static final String NEWS_UPDATED_TEXT_CODE = "notifications.email.news.text.news-updated";

    private static final NotificationType NOTIFICATION_TYPE = NotificationType.IN_NEWS;

    @Override
    protected NotificationType getNotificationType() {
        return NewsNotificationEmailService.NOTIFICATION_TYPE;
    }

    @Override
    public void sendEmailOnCreation(Object newNews, List<User> usersToNotify, HttpServletRequest request,
            MessageSource messageSource) {
        this.sendEmail((News) newNews, NewsNotificationEmailService.NEWS_CREATED_SUBJECT_CODE,
                NewsNotificationEmailService.NEWS_CREATED_TEXT_CODE, usersToNotify, request, messageSource);
    }

    @Override
    public void sendEmailOnUpdate(Object updatedNews, List<User> usersToNotify, HttpServletRequest request,
            MessageSource messageSource) {
        this.sendEmail((News) updatedNews, NewsNotificationEmailService.NEWS_UPDATED_SUBJECT_CODE,
                NewsNotificationEmailService.NEWS_UPDATED_TEXT_CODE, usersToNotify, request, messageSource);
    }

    /**
     * Sends emails to the given {@link User Users} with notification of the newly posted or updated
     * {@link News}.
     * 
     * @param news
     *            Newly posted or updated {@link News}
     */
    private void sendEmail(News news, String subjectLocalizationCode, String textLocalizationCode,
            List<User> usersToNotify, HttpServletRequest request, MessageSource messageSource) {

        String subject = this.getSubject(subjectLocalizationCode, news, request, messageSource);

        String newsTitle = news.getTitle();
        String textWithConvertedUrls = Url.convertImageRelativeUrlsToAbsolute(news.getText(), request);
        String wholeTextUrl = NewsUrlParts.getNewsUrl(news, request);
        String croppedText = this.cropTooLongTextAndAddLink(textWithConvertedUrls, wholeTextUrl, request,
                messageSource);

        for (User user : usersToNotify) {
            String addressing = this.getLocalizedRecipientAddressing(user, request, messageSource);
            String signature = this.getLocalizedNotificationSignature(user, request, messageSource);
            Object[] messageParams = new Object[] { addressing, newsTitle, croppedText, signature };
            String messageText = Localization.findLocaleMessage(messageSource, request, textLocalizationCode,
                    messageParams);

            Email.sendMail(user.getEmail(), subject, messageText);
        }
    }

    /**
     * Gets a localized subject of notification emails.
     * 
     * @param news
     *            Newly posted or updated {@link News}
     */
    private String getSubject(String subjectLocalizationCode, News news, HttpServletRequest request,
            MessageSource messageSource) {
        String localized = Localization.findLocaleMessage(messageSource, request, subjectLocalizationCode,
                news.getTitle());
        return Localization.stripCzechDiacritics(localized);
    }
}
