package com.svnavigatoru600.service.forum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.domain.users.NotificationType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.AbstractNotificationEmailService;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.Url;

/**
 * Provide sending of emails concerning notifications of new {@link Thread threads} and updated ones.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Service
public class ThreadNotificationEmailService extends AbstractNotificationEmailService {

    private static final String THREAD_AUTHOR_CODE = "forum.contributions.author";
    private static final String FIRST_CONTRIBUTION_CODE = "forum.contributions.first";

    private static final String THREAD_CREATED_SUBJECT_CODE = "notifications.email.thread.subject.thread-created";
    private static final String THREAD_CREATED_TEXT_CODE = "notifications.email.thread.text.thread-created";

    private static final NotificationType NOTIFICATION_TYPE = NotificationType.IN_FORUM;

    @Override
    protected NotificationType getNotificationType() {
        return ThreadNotificationEmailService.NOTIFICATION_TYPE;
    }

    @Override
    public void sendEmailOnCreation(Object newThread, List<User> usersToNotify, HttpServletRequest request,
            MessageSource messageSource) {
        Thread thread = (Thread) newThread;

        String subject = this.getSubject(ThreadNotificationEmailService.THREAD_CREATED_SUBJECT_CODE, thread,
                request, messageSource);

        String threadName = thread.getName();
        String localizedAuthorLabel = this.getLocalizedContributionAuthorLabel(request, messageSource);
        String authorFullName = thread.getAuthor().getFullName();
        String localizedContributionTextLabel = this.getLocalizedFirstContributionLabel(request,
                messageSource);
        String contributionText = Url.convertImageRelativeUrlsToAbsolute(thread.getContributions().get(0)
                .getText(), request);

        for (User user : usersToNotify) {
            String addressing = this.getLocalizedRecipientAddressing(user, request, messageSource);
            String signature = this.getLocalizedNotificationSignature(user, request, messageSource);
            Object[] messageParams = new Object[] { addressing, threadName, localizedAuthorLabel,
                    authorFullName, localizedContributionTextLabel, contributionText, signature };
            String messageText = Localization.findLocaleMessage(messageSource, request,
                    ThreadNotificationEmailService.THREAD_CREATED_TEXT_CODE, messageParams);

            Email.sendMail(user.getEmail(), subject, messageText);
        }
    }

    @Override
    public void sendEmailOnUpdate(Object updatedThread, List<User> usersToNotify, HttpServletRequest request,
            MessageSource messageSource) {
        throw new IllegalAccessError("This method is not supported");
    }

    /**
     * Gets a localized subject of notification emails.
     * 
     * @param event
     *            Newly created {@link Thread}
     */
    private String getSubject(String subjectLocalizationCode, Thread thread, HttpServletRequest request,
            MessageSource messageSource) {
        String localized = Localization.findLocaleMessage(messageSource, request, subjectLocalizationCode,
                thread.getName());
        return Localization.stripCzechDiacritics(localized);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedContributionAuthorLabel(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                ThreadNotificationEmailService.THREAD_AUTHOR_CODE);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedFirstContributionLabel(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                ThreadNotificationEmailService.FIRST_CONTRIBUTION_CODE);
    }
}
