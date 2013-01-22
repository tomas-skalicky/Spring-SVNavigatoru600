package com.svnavigatoru600.service.records;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.users.NotificationType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.AbstractNotificationEmailService;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.Url;
import com.svnavigatoru600.url.records.otherdocuments.OtherDocumentsCommonUrlParts;

/**
 * Provide sending of emails concerning notifications of new {@link OtherDocumentRecord other document
 * records} and updated ones.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Service
public class OtherDocumentRecordNotificationEmailService extends AbstractNotificationEmailService {

    private static final String DOCUMENT_NAME_CODE = "other-documents.document-name";
    private static final String DOCUMENT_DESCRIPTION_CODE = "other-documents.document-description";

    private static final String RECORD_CREATED_SUBJECT_CODE = "notifications.email.other-document.subject.record-created";
    private static final String RECORD_CREATED_TEXT_CODE = "notifications.email.other-document.text.record-created";
    private static final String RECORD_UPDATED_SUBJECT_CODE = "notifications.email.other-document.subject.record-updated";
    private static final String RECORD_UPDATED_TEXT_CODE = "notifications.email.other-document.text.record-updated";

    private static final NotificationType NOTIFICATION_TYPE = NotificationType.IN_OTHER_DOCUMENTS;

    @Override
    protected NotificationType getNotificationType() {
        return OtherDocumentRecordNotificationEmailService.NOTIFICATION_TYPE;
    }

    @Override
    public void sendEmailOnCreation(Object newRecord, List<User> usersToNotify, HttpServletRequest request,
            MessageSource messageSource) {
        this.sendEmail((OtherDocumentRecord) newRecord,
                OtherDocumentRecordNotificationEmailService.RECORD_CREATED_SUBJECT_CODE,
                OtherDocumentRecordNotificationEmailService.RECORD_CREATED_TEXT_CODE, usersToNotify, request,
                messageSource);
    }

    @Override
    public void sendEmailOnUpdate(Object updatedRecord, List<User> usersToNotify, HttpServletRequest request,
            MessageSource messageSource) {
        this.sendEmail((OtherDocumentRecord) updatedRecord,
                OtherDocumentRecordNotificationEmailService.RECORD_UPDATED_SUBJECT_CODE,
                OtherDocumentRecordNotificationEmailService.RECORD_UPDATED_TEXT_CODE, usersToNotify, request,
                messageSource);
    }

    /**
     * Sends emails to the given {@link User Users} with notification of the newly posted or updated
     * {@link OtherDocumentRecord}.
     * 
     * @param record
     *            Newly posted or updated {@link OtherDocumentRecord}
     */
    private void sendEmail(OtherDocumentRecord record, String subjectLocalizationCode,
            String textLocalizationCode, List<User> usersToNotify, HttpServletRequest request,
            MessageSource messageSource) {

        String subject = this.getSubject(subjectLocalizationCode, request, messageSource);

        String localizedDocumentNameLabel = this.getLocalizedDocumentNameLabel(request, messageSource);
        String documentName = record.getName();
        String localizedDocumentDescriptionLabel = this.getLocalizedDocumentDescriptionLabel(request,
                messageSource);
        String documentDescription = Url.convertImageRelativeUrlsToAbsolute(record.getDescription(), request);
        String localizedAttachedFileLabel = this.getLocalizedAttachedFile(request, messageSource);
        String fileUrl = this.getAttachedFileUrlHtml(
                OtherDocumentsCommonUrlParts.getAttachedFileUrl(record, request), request, messageSource);

        for (User user : usersToNotify) {
            String addressing = this.getLocalizedRecipientAddressing(user, request, messageSource);
            String signature = this.getLocalizedNotificationSignature(user, request, messageSource);
            Object[] messageParams = new Object[] { addressing, localizedDocumentNameLabel, documentName,
                    localizedDocumentDescriptionLabel, documentDescription, localizedAttachedFileLabel,
                    fileUrl, signature };
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
        String localized = Localization.findLocaleMessage(messageSource, request, subjectLocalizationCode);
        return Localization.stripCzechDiacritics(localized);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedDocumentNameLabel(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                OtherDocumentRecordNotificationEmailService.DOCUMENT_NAME_CODE);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedDocumentDescriptionLabel(HttpServletRequest request,
            MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                OtherDocumentRecordNotificationEmailService.DOCUMENT_DESCRIPTION_CODE);
    }
}
