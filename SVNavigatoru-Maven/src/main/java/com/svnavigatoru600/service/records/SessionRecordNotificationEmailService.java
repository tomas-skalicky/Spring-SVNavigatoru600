package com.svnavigatoru600.service.records;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.users.NotificationType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.AbstractNotificationEmailService;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.Url;

/**
 * Provide sending of emails concerning notifications of new {@link SessionRecord session records} and updated
 * ones.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Service
public class SessionRecordNotificationEmailService extends AbstractNotificationEmailService {

    private static final String SESSION_TYPE_CODE = "session-records.type-of-session";
    private static final String SESSION_DATE_CODE = "session-records.session-date";
    private static final String SESSION_DISCUSSED_TOPICS_CODE = "session-records.discussed-topics";

    private static final String RECORD_CREATED_SUBJECT_CODE = "notifications.email.session-record.subject.record-created";
    private static final String RECORD_CREATED_TEXT_CODE = "notifications.email.session-record.text.record-created";
    private static final String RECORD_UPDATED_SUBJECT_CODE = "notifications.email.session-record.subject.record-updated";
    private static final String RECORD_UPDATED_TEXT_CODE = "notifications.email.session-record.text.record-updated";

    private static final NotificationType NOTIFICATION_TYPE = NotificationType.IN_OTHER_SECTIONS;

    @Override
    protected NotificationType getNotificationType() {
        return SessionRecordNotificationEmailService.NOTIFICATION_TYPE;
    }

    @Override
    public void sendEmailOnCreation(Object newRecord, List<User> usersToNotify, HttpServletRequest request,
            MessageSource messageSource) {
        this.sendEmail((SessionRecord) newRecord,
                SessionRecordNotificationEmailService.RECORD_CREATED_SUBJECT_CODE,
                SessionRecordNotificationEmailService.RECORD_CREATED_TEXT_CODE, usersToNotify, request,
                messageSource);
    }

    @Override
    public void sendEmailOnUpdate(Object updatedRecord, List<User> usersToNotify, HttpServletRequest request,
            MessageSource messageSource) {
        this.sendEmail((SessionRecord) updatedRecord,
                SessionRecordNotificationEmailService.RECORD_UPDATED_SUBJECT_CODE,
                SessionRecordNotificationEmailService.RECORD_UPDATED_TEXT_CODE, usersToNotify, request,
                messageSource);
    }

    /**
     * Sends emails to the given {@link User Users} with notification of the newly posted or updated
     * {@link SessionRecord}.
     * 
     * @param record
     *            Newly posted or updated {@link SessionRecord}
     */
    private void sendEmail(SessionRecord record, String subjectLocalizationCode, String textLocalizationCode,
            List<User> usersToNotify, HttpServletRequest request, MessageSource messageSource) {

        String subject = this.getSubject(subjectLocalizationCode, request, messageSource);

        String localizedSessionTypeLabel = this.getLocalizedSessionTypeLabel(request, messageSource);
        String localizedSessionType = Localization.findLocaleMessage(messageSource, request, record
                .getTypedType().getLocalizationCode());
        String localizedSessionDateLabel = this.getLocalizedSessionDateLabel(request, messageSource);
        String localizedSessionDate = this.getLocalizedSessionDate(record, request);
        String localizedDiscussedTopicsLabel = this.getLocalizedDiscussedTopicsLabel(request, messageSource);
        String discussedTopics = Url.convertImageRelativeUrlsToAbsolute(record.getDiscussedTopics(), request);

        for (User user : usersToNotify) {
            String addressing = this.getLocalizedRecipientAddressing(user, request, messageSource);
            String signature = this.getLocalizedNotificationSignature(user, request, messageSource);
            Object[] messageParams = new Object[] { addressing, localizedSessionTypeLabel,
                    localizedSessionType, localizedSessionDateLabel, localizedSessionDate,
                    localizedDiscussedTopicsLabel, discussedTopics, signature };
            String messageText = Localization.findLocaleMessage(messageSource, request, textLocalizationCode,
                    messageParams);

            Email.sendMail(user.getEmail(), subject, messageText);
        }
    }

    /**
     * Gets a localized subject of notification emails.
     */
    private String getSubject(String subjectLocalizationCode, HttpServletRequest request,
            MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request, subjectLocalizationCode);
    }

    /**
     * Gets localized {@link SessionRecord#getSessionDate() session date} of the given {@link SessionRecord}.
     */
    private String getLocalizedSessionDate(SessionRecord record, HttpServletRequest request) {
        Locale locale = Localization.getLocale(request);
        return DateUtils.format(record.getSessionDate(), DateUtils.MIDDLE_DATE_FORMATS.get(locale), locale);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedSessionTypeLabel(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                SessionRecordNotificationEmailService.SESSION_TYPE_CODE);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedSessionDateLabel(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                SessionRecordNotificationEmailService.SESSION_DATE_CODE);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedDiscussedTopicsLabel(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                SessionRecordNotificationEmailService.SESSION_DISCUSSED_TOPICS_CODE);
    }
}