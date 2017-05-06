package com.svnavigatoru600.service.forum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.domain.users.NotificationTypeEnum;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.AbstractNotificationEmailService;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.Url;
import com.svnavigatoru600.web.url.forum.ContributionsUrlParts;

/**
 * Provide sending of emails concerning notifications of new {@link ForumContribution contributions} and updated ones.
 *
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Service
public class ContributionNotificationEmailService extends AbstractNotificationEmailService {

    private static final String CONTRIBUTION_AUTHOR_CODE = "forum.contributions.author";
    private static final String CONTRIBUTION_TEXT_CODE = "forum.contributions.text";

    private static final String CONTRIBUTION_CREATED_SUBJECT_CODE = "notifications.email.contribution.subject.contribution-created";
    private static final String CONTRIBUTION_CREATED_TEXT_CODE = "notifications.email.contribution.text.contribution-created";
    private static final String CONTRIBUTION_UPDATED_SUBJECT_CODE = "notifications.email.contribution.subject.contribution-updated";
    private static final String CONTRIBUTION_UPDATED_TEXT_CODE = "notifications.email.contribution.text.contribution-updated";

    private static final NotificationTypeEnum NOTIFICATION_TYPE = NotificationTypeEnum.IN_FORUM;

    @Override
    protected NotificationTypeEnum getNotificationType() {
        return ContributionNotificationEmailService.NOTIFICATION_TYPE;
    }

    @Override
    public void sendEmailOnCreation(final Object newContribution, final List<User> usersToNotify, final HttpServletRequest request,
            final MessageSource messageSource) {
        sendEmail((ForumContribution) newContribution,
                ContributionNotificationEmailService.CONTRIBUTION_CREATED_SUBJECT_CODE,
                ContributionNotificationEmailService.CONTRIBUTION_CREATED_TEXT_CODE, usersToNotify, request,
                messageSource);
    }

    @Override
    public void sendEmailOnUpdate(final Object updatedContribution, final List<User> usersToNotify, final HttpServletRequest request,
            final MessageSource messageSource) {
        sendEmail((ForumContribution) updatedContribution,
                ContributionNotificationEmailService.CONTRIBUTION_UPDATED_SUBJECT_CODE,
                ContributionNotificationEmailService.CONTRIBUTION_UPDATED_TEXT_CODE, usersToNotify, request,
                messageSource);
    }

    /**
     * Sends emails to the given {@link User Users} with notification of the newly posted or updated
     * {@link ForumContribution}.
     *
     * @param contribution
     *            Newly posted or updated {@link ForumContribution}
     */
    private void sendEmail(final ForumContribution contribution, final String subjectLocalizationCode, final String textLocalizationCode,
            final List<User> usersToNotify, final HttpServletRequest request, final MessageSource messageSource) {

        final String subject = getSubject(subjectLocalizationCode, contribution, request, messageSource);

        final String threadName = contribution.getThread().getName();
        final String localizedAuthorLabel = getLocalizedContributionAuthorLabel(request, messageSource);
        final String authorFullName = contribution.getAuthor().getFullName();
        final String localizedContributionTextLabel = getLocalizedContributionTextLabel(request, messageSource);
        final String textWithConvertedUrls = Url.convertImageRelativeUrlsToAbsolute(contribution.getText(), request);
        final String wholeTextUrl = ContributionsUrlParts.getAbsoluteContributionUrl(contribution, request);
        final String croppedText = cropTooLongTextAndAddLink(textWithConvertedUrls, wholeTextUrl, request, messageSource);

        for (final User user : usersToNotify) {
            final String addressing = getLocalizedRecipientAddressing(user, request, messageSource);
            final String signature = getLocalizedNotificationSignature(user, request, messageSource);
            final Object[] messageParams = new Object[] { addressing, threadName, localizedAuthorLabel, authorFullName,
                    localizedContributionTextLabel, croppedText, signature };
            final String messageText = Localization.findLocaleMessage(messageSource, request, textLocalizationCode,
                    messageParams);

            Email.sendMail(user, subject, messageText);
        }
    }

    /**
     * Gets a localized subject of notification emails.
     *
     * @param contribution
     *            Newly posted or updated {@link ForumContribution}
     */
    private String getSubject(final String subjectLocalizationCode, final ForumContribution contribution,
            final HttpServletRequest request, final MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request, subjectLocalizationCode,
                contribution.getThread().getName());
    }

    /**
     * Trivial localization
     */
    private String getLocalizedContributionAuthorLabel(final HttpServletRequest request, final MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                ContributionNotificationEmailService.CONTRIBUTION_AUTHOR_CODE);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedContributionTextLabel(final HttpServletRequest request, final MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                ContributionNotificationEmailService.CONTRIBUTION_TEXT_CODE);
    }

}
