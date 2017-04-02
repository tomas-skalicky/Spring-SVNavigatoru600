package com.svnavigatoru600.service.eventcalendar;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.users.NotificationTypeEnum;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.AbstractNotificationEmailService;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.Url;

/**
 * Provide sending of emails concerning notifications of new {@link CalendarEvent calendar events} and updated ones.
 *
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Service
public class CalendarEventNotificationEmailService extends AbstractNotificationEmailService {

    private static final String EVENT_DATE_CODE = "event-calendar.date";
    private static final String EVENT_DESCRIPTION_CODE = "event-calendar.description";

    private static final String EVENT_CREATED_SUBJECT_CODE = "notifications.email.event.subject.event-created";
    private static final String EVENT_CREATED_TEXT_CODE = "notifications.email.event.text.event-created";
    private static final String EVENT_UPDATED_SUBJECT_CODE = "notifications.email.event.subject.event-updated";
    private static final String EVENT_UPDATED_TEXT_CODE = "notifications.email.event.text.event-updated";

    private static final NotificationTypeEnum NOTIFICATION_TYPE = NotificationTypeEnum.IN_EVENTS;

    @Override
    protected NotificationTypeEnum getNotificationType() {
        return CalendarEventNotificationEmailService.NOTIFICATION_TYPE;
    }

    @Override
    public void sendEmailOnCreation(final Object newEvent, final List<User> usersToNotify, final HttpServletRequest request,
            final MessageSource messageSource) {
        final CalendarEvent event = (CalendarEvent) newEvent;

        final String subject = getSubject(CalendarEventNotificationEmailService.EVENT_CREATED_SUBJECT_CODE, event, request,
                messageSource);

        final String eventName = event.getName();
        final String localizedDateLabel = getLocalizedDateLabel(request, messageSource);
        final String localizedEventDate = getLocalizedDate(event, request);
        final String localizedDescriptionLabel = getLocalizedDescriptionLabel(request, messageSource);
        final String eventDescription = Url.convertImageRelativeUrlsToAbsolute(event.getDescription(), request);

        for (final User user : usersToNotify) {
            final String addressing = getLocalizedRecipientAddressing(user, request, messageSource);
            final String signature = getLocalizedNotificationSignature(user, request, messageSource);
            final Object[] messageParams = new Object[] { addressing, eventName, localizedDateLabel, localizedEventDate,
                    localizedDescriptionLabel, eventDescription, signature };
            final String messageText = Localization.findLocaleMessage(messageSource, request,
                    CalendarEventNotificationEmailService.EVENT_CREATED_TEXT_CODE, messageParams);

            Email.sendMail(user, subject, messageText);
        }
    }

    @Override
    public void sendEmailOnUpdate(final Object updatedEvent, final List<User> usersToNotify, final HttpServletRequest request,
            final MessageSource messageSource) {
        final CalendarEvent event = (CalendarEvent) updatedEvent;

        final String subject = getSubject(CalendarEventNotificationEmailService.EVENT_UPDATED_SUBJECT_CODE, event, request,
                messageSource);

        final String eventName = event.getName();
        final String localizedEventDate = getLocalizedDate(event, request);
        final String localizedDescriptionLabel = getLocalizedDescriptionLabel(request, messageSource);
        final String eventDescription = Url.convertImageRelativeUrlsToAbsolute(event.getDescription(), request);

        for (final User user : usersToNotify) {
            final String addressing = getLocalizedRecipientAddressing(user, request, messageSource);
            final String signature = getLocalizedNotificationSignature(user, request, messageSource);
            final Object[] messageParams = new Object[] { addressing, eventName, localizedEventDate,
                    localizedDescriptionLabel, eventDescription, signature };
            final String messageText = Localization.findLocaleMessage(messageSource, request,
                    CalendarEventNotificationEmailService.EVENT_UPDATED_TEXT_CODE, messageParams);

            Email.sendMail(user, subject, messageText);
        }
    }

    /**
     * Gets a localized subject of notification emails.
     *
     * @param event
     *            Newly posted or updated {@link CalendarEvent}
     */
    private String getSubject(final String subjectLocalizationCode, final CalendarEvent event, final HttpServletRequest request,
            final MessageSource messageSource) {
        final Object[] messageParams = new Object[] { event.getName(), getLocalizedDate(event, request) };

        return Localization.findLocaleMessage(messageSource, request, subjectLocalizationCode, messageParams);
    }

    /**
     * Gets localized {@link CalendarEvent#getDate() date} of the given {@link CalendarEvent}.
     */
    private String getLocalizedDate(final CalendarEvent event, final HttpServletRequest request) {
        final Locale locale = Localization.getLocale(request);
        return DateUtils.format(event.getDate(), DateUtils.MIDDLE_DATE_FORMATS.get(locale), locale);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedDateLabel(final HttpServletRequest request, final MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                CalendarEventNotificationEmailService.EVENT_DATE_CODE);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedDescriptionLabel(final HttpServletRequest request, final MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                CalendarEventNotificationEmailService.EVENT_DESCRIPTION_CODE);
    }

}
