package com.svnavigatoru600.service.forum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.users.NotificationType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.AbstractNotificationEmailService;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.Url;
import com.svnavigatoru600.url.forum.ContributionsUrlParts;

/**
 * Provide sending of emails concerning notifications of new {@link Contribution contributions} and updated
 * ones.
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

    private static final NotificationType NOTIFICATION_TYPE = NotificationType.IN_FORUM;

    @Override
    protected NotificationType getNotificationType() {
        return ContributionNotificationEmailService.NOTIFICATION_TYPE;
    }

    @Override
    public void sendEmailOnCreation(Object newContribution, List<User> usersToNotify,
            HttpServletRequest request, MessageSource messageSource) {
        sendEmail((Contribution) newContribution,
                ContributionNotificationEmailService.CONTRIBUTION_CREATED_SUBJECT_CODE,
                ContributionNotificationEmailService.CONTRIBUTION_CREATED_TEXT_CODE, usersToNotify, request,
                messageSource);
    }

    @Override
    public void sendEmailOnUpdate(Object updatedContribution, List<User> usersToNotify,
            HttpServletRequest request, MessageSource messageSource) {
        sendEmail((Contribution) updatedContribution,
                ContributionNotificationEmailService.CONTRIBUTION_UPDATED_SUBJECT_CODE,
                ContributionNotificationEmailService.CONTRIBUTION_UPDATED_TEXT_CODE, usersToNotify, request,
                messageSource);
    }

    /**
     * Sends emails to the given {@link User Users} with notification of the newly posted or updated
     * {@link Contribution}.
     * 
     * @param contribution
     *            Newly posted or updated {@link Contribution}
     */
    private void sendEmail(Contribution contribution, String subjectLocalizationCode,
            String textLocalizationCode, List<User> usersToNotify, HttpServletRequest request,
            MessageSource messageSource) {

        String subject = getSubject(subjectLocalizationCode, contribution, request, messageSource);

        String threadName = contribution.getThread().getName();
        String localizedAuthorLabel = getLocalizedContributionAuthorLabel(request, messageSource);
        String authorFullName = contribution.getAuthor().getFullName();
        String localizedContributionTextLabel = getLocalizedContributionTextLabel(request, messageSource);
        String textWithConvertedUrls = Url
                .convertImageRelativeUrlsToAbsolute(contribution.getText(), request);
        String wholeTextUrl = ContributionsUrlParts.getAbsoluteContributionUrl(contribution, request);
        String croppedText = cropTooLongTextAndAddLink(textWithConvertedUrls, wholeTextUrl, request,
                messageSource);

        for (User user : usersToNotify) {
            String addressing = getLocalizedRecipientAddressing(user, request, messageSource);
            String signature = getLocalizedNotificationSignature(user, request, messageSource);
            Object[] messageParams = new Object[] { addressing, threadName, localizedAuthorLabel,
                    authorFullName, localizedContributionTextLabel, croppedText, signature };
            String messageText = Localization.findLocaleMessage(messageSource, request, textLocalizationCode,
                    messageParams);

            Email.sendMail(user, subject, messageText);
        }
    }

    /**
     * Gets a localized subject of notification emails.
     * 
     * @param contribution
     *            Newly posted or updated {@link Contribution}
     */
    private String getSubject(String subjectLocalizationCode, Contribution contribution,
            HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request, subjectLocalizationCode, contribution
                .getThread().getName());
    }

    /**
     * Trivial localization
     */
    private String getLocalizedContributionAuthorLabel(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                ContributionNotificationEmailService.CONTRIBUTION_AUTHOR_CODE);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedContributionTextLabel(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                ContributionNotificationEmailService.CONTRIBUTION_TEXT_CODE);
    }
}
