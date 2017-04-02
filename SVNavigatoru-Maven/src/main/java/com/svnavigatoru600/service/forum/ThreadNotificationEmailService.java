package com.svnavigatoru600.service.forum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.ForumThread;
import com.svnavigatoru600.domain.users.NotificationTypeEnum;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.AbstractNotificationEmailService;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.Url;

/**
 * Provide sending of emails concerning notifications of new {@link ForumThread threads} and updated ones.
 *
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Service
public class ThreadNotificationEmailService extends AbstractNotificationEmailService {

    private static final String THREAD_AUTHOR_CODE = "forum.contributions.author";
    private static final String FIRST_CONTRIBUTION_CODE = "forum.contributions.first";

    private static final String THREAD_CREATED_SUBJECT_CODE = "notifications.email.thread.subject.thread-created";
    private static final String THREAD_CREATED_TEXT_CODE = "notifications.email.thread.text.thread-created";

    private static final NotificationTypeEnum NOTIFICATION_TYPE = NotificationTypeEnum.IN_FORUM;

    @Override
    protected NotificationTypeEnum getNotificationType() {
        return ThreadNotificationEmailService.NOTIFICATION_TYPE;
    }

    @Override
    public void sendEmailOnCreation(final Object newThread, final List<User> usersToNotify, final HttpServletRequest request,
            final MessageSource messageSource) {
        final ForumThread thread = (ForumThread) newThread;

        final String subject = getSubject(ThreadNotificationEmailService.THREAD_CREATED_SUBJECT_CODE, thread, request,
                messageSource);

        final String threadName = thread.getName();
        final String localizedAuthorLabel = getLocalizedContributionAuthorLabel(request, messageSource);
        final String authorFullName = thread.getAuthor().getFullName();
        final String localizedContributionTextLabel = getLocalizedFirstContributionLabel(request, messageSource);
        final String contributionText = Url.convertImageRelativeUrlsToAbsolute(thread.getContributions().get(0).getText(),
                request);

        for (final User user : usersToNotify) {
            final String addressing = getLocalizedRecipientAddressing(user, request, messageSource);
            final String signature = getLocalizedNotificationSignature(user, request, messageSource);
            final Object[] messageParams = new Object[] { addressing, threadName, localizedAuthorLabel, authorFullName,
                    localizedContributionTextLabel, contributionText, signature };
            final String messageText = Localization.findLocaleMessage(messageSource, request,
                    ThreadNotificationEmailService.THREAD_CREATED_TEXT_CODE, messageParams);

            Email.sendMail(user, subject, messageText);
        }
    }

    @Override
    public void sendEmailOnUpdate(final Object updatedThread, final List<User> usersToNotify, final HttpServletRequest request,
            final MessageSource messageSource) {
        throw new IllegalAccessError("This method is not supported");
    }

    /**
     * Gets a localized subject of notification emails.
     *
     * @param event
     *            Newly created {@link ForumThread}
     */
    private String getSubject(final String subjectLocalizationCode, final ForumThread thread, final HttpServletRequest request,
            final MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request, subjectLocalizationCode, thread.getName());
    }

    /**
     * Trivial localization
     */
    private String getLocalizedContributionAuthorLabel(final HttpServletRequest request, final MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                ThreadNotificationEmailService.THREAD_AUTHOR_CODE);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedFirstContributionLabel(final HttpServletRequest request, final MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                ThreadNotificationEmailService.FIRST_CONTRIBUTION_CODE);
    }

}
