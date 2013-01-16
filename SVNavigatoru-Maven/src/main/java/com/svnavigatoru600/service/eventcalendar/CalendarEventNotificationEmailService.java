package com.svnavigatoru600.service.eventcalendar;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.users.NotificationType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.AbstractNotificationEmailService;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.Url;

/**
 * Provide sending of emails concerning notifications of new {@link CalendarEvent calendar events} and updated
 * ones.
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

    private static final NotificationType NOTIFICATION_TYPE = NotificationType.IN_EVENTS;

    @Override
    protected NotificationType getNotificationType() {
        return CalendarEventNotificationEmailService.NOTIFICATION_TYPE;
    }

    @Override
    public void sendEmailOnCreation(Object newEvent, List<User> usersToNotify, HttpServletRequest request,
            MessageSource messageSource) {
        CalendarEvent event = (CalendarEvent) newEvent;

        String subject = this.getSubject(CalendarEventNotificationEmailService.EVENT_CREATED_SUBJECT_CODE,
                event, request, messageSource);

        String eventName = event.getName();
        String localizedDateLabel = this.getLocalizedDateLabel(request, messageSource);
        String localizedEventDate = this.getLocalizedDate(event, request);
        String localizedDescriptionLabel = this.getLocalizedDescriptionLabel(request, messageSource);
        String eventDescription = Url.convertImageRelativeUrlsToAbsolute(event.getDescription(), request);

        for (User user : usersToNotify) {
            String addressing = this.getLocalizedRecipientAddressing(user, request, messageSource);
            String signature = this.getLocalizedNotificationSignature(user, request, messageSource);
            Object[] messageParams = new Object[] { addressing, eventName, localizedDateLabel,
                    localizedEventDate, localizedDescriptionLabel, eventDescription, signature };
            String messageText = Localization.findLocaleMessage(messageSource, request,
                    CalendarEventNotificationEmailService.EVENT_CREATED_TEXT_CODE, messageParams);

            Email.sendMail(user.getEmail(), subject, messageText);
        }
    }

    @Override
    public void sendEmailOnUpdate(Object updatedEvent, List<User> usersToNotify, HttpServletRequest request,
            MessageSource messageSource) {
        CalendarEvent event = (CalendarEvent) updatedEvent;

        String subject = this.getSubject(CalendarEventNotificationEmailService.EVENT_UPDATED_SUBJECT_CODE,
                event, request, messageSource);

        String eventName = event.getName();
        String localizedEventDate = this.getLocalizedDate(event, request);
        String localizedDescriptionLabel = this.getLocalizedDescriptionLabel(request, messageSource);
        String eventDescription = Url.convertImageRelativeUrlsToAbsolute(event.getDescription(), request);

        for (User user : usersToNotify) {
            String addressing = this.getLocalizedRecipientAddressing(user, request, messageSource);
            String signature = this.getLocalizedNotificationSignature(user, request, messageSource);
            Object[] messageParams = new Object[] { addressing, eventName, localizedEventDate,
                    localizedDescriptionLabel, eventDescription, signature };
            String messageText = Localization.findLocaleMessage(messageSource, request,
                    CalendarEventNotificationEmailService.EVENT_UPDATED_TEXT_CODE, messageParams);

            Email.sendMail(user.getEmail(), subject, messageText);
        }
    }

    /**
     * Gets a localized subject of notification emails.
     * 
     * @param event
     *            Newly posted or updated {@link CalendarEvent}
     */
    private String getSubject(String subjectLocalizationCode, CalendarEvent event,
            HttpServletRequest request, MessageSource messageSource) {
        Object[] messageParams = new Object[] { event.getName(), this.getLocalizedDate(event, request) };

        return Localization.findLocaleMessage(messageSource, request, subjectLocalizationCode, messageParams);
    }

    /**
     * Gets localized {@link CalendarEvent#getDate() date} of the given {@link CalendarEvent}.
     */
    private String getLocalizedDate(CalendarEvent event, HttpServletRequest request) {
        Locale locale = Localization.getLocale(request);
        return DateUtils.format(event.getDate(), DateUtils.MIDDLE_DATE_FORMATS.get(locale), locale);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedDateLabel(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                CalendarEventNotificationEmailService.EVENT_DATE_CODE);
    }

    /**
     * Trivial localization
     */
    private String getLocalizedDescriptionLabel(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request,
                CalendarEventNotificationEmailService.EVENT_DESCRIPTION_CODE);
    }
}
