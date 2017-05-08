package com.svnavigatoru600.service.eventcalendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.eventcalendar.PriorityTypeEnum;
import com.svnavigatoru600.domain.users.AuthorityTypeEnum;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.service.SubjectOfNotificationService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderTypeEnum;

/**
 * Provides convenient methods to work with {@link CalendarEvent} objects.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class CalendarEventService implements SubjectOfNotificationService {

    /**
     * The object which provides a persistence.
     */
    private final CalendarEventDao eventDao;
    /**
     * Does the work which concerns mainly notification of {@link User users}.
     */
    private final UserService userService;
    /**
     * Assembles notification emails and sends them to authorized {@link User users}.
     */
    private final CalendarEventNotificationEmailService emailService;

    @Inject
    public CalendarEventService(final CalendarEventDao eventDao, final UserService userService,
            final CalendarEventNotificationEmailService emailService) {
        this.eventDao = eventDao;
        this.userService = userService;
        this.emailService = emailService;
    }

    /**
     * Returns a {@link CalendarEvent} stored in the repository which has the given ID.
     */
    public CalendarEvent findById(final int eventId) {
        return eventDao.findById(eventId);
    }

    /**
     * Returns all {@link CalendarEvent CalendarEvents} stored in the repository arranged according to their
     * {@link CalendarEvent#getDate() date} ascending.
     * <p>
     * Moreover, only {@link CalendarEvent CalendarEvents} which will take place are returned, the passed ones are not.
     */
    public List<CalendarEvent> findAllFutureEventsOrdered() {
        return eventDao.findAllFutureEventsOrdered(DateUtils.getToday(), OrderTypeEnum.ASCENDING);
    }

    /**
     * Updates the given {@link CalendarEvent} in the repository. The old version of this event should be already stored
     * there.
     */
    public void update(final CalendarEvent event) {
        eventDao.update(event);
    }

    /**
     * Updates properties of the given <code>eventToUpdate</code> and persists this {@link CalendarEvent} into the
     * repository. The old version of this event should be already stored there.
     *
     * @param eventToUpdate
     *            Persisted {@link CalendarEvent}
     * @param newEvent
     *            {@link CalendarEvent} which contains new values of properties of <code>eventToUpdate</code>. These
     *            values are copied to the persisted event.
     * @param newPriority
     *            Priority which is to be the new one of the <code>eventToUpdate</code>
     */
    public void update(final CalendarEvent eventToUpdate, final CalendarEvent newEvent,
            final PriorityTypeEnum newPriority) {
        eventToUpdate.setName(newEvent.getName());
        eventToUpdate.setDate(newEvent.getDate());
        eventToUpdate.setDescription(newEvent.getDescription());
        eventToUpdate.setPriority(newPriority);
        this.update(eventToUpdate);
    }

    /**
     * Updates properties of the given <code>eventToUpdate</code> and persists this {@link CalendarEvent} into the
     * repository. The old version of this event should be already stored there.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about changes in the event.
     *
     * @param eventToUpdate
     *            Persisted {@link CalendarEvent}
     * @param newEvent
     *            {@link CalendarEvent} which contains new values of properties of <code>eventToUpdate</code>. These
     *            values are copied to the persisted event.
     * @param newPriority
     *            Priority which is to be the new one of the <code>eventToUpdate</code>
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void updateAndNotifyUsers(final CalendarEvent eventToUpdate, final CalendarEvent newEvent,
            final PriorityTypeEnum newPriority, final boolean sendNotification, final HttpServletRequest request,
            final MessageSource messageSource) {
        this.update(eventToUpdate, newEvent, newPriority);

        if (sendNotification) {
            notifyUsersOfUpdate(eventToUpdate, request, messageSource);
        }
    }

    @Override
    public List<User> gainUsersToNotify() {
        return userService.findAllWithEmailByAuthorityAndSubscription(AuthorityTypeEnum.ROLE_MEMBER_OF_SV,
                emailService.getNotificationType());
    }

    @Override
    public void notifyUsersOfUpdate(final Object updatedEvent, final HttpServletRequest request,
            final MessageSource messageSource) {
        emailService.sendEmailOnUpdate(updatedEvent, gainUsersToNotify(), request, messageSource);
    }

    /**
     * Stores the given {@link CalendarEvent} to the repository.
     *
     * @return New ID of the given {@link CalendarEvent} generated by the repository
     */
    public int save(final CalendarEvent event) {
        return eventDao.save(event);
    }

    /**
     * Updates corresponding {@link java.util.Date Date} fields of the given new {@link CalendarEvent} (if there are any
     * appropriate) and stores the event to the repository.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about a creation of the event.
     *
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void saveAndNotifyUsers(final CalendarEvent newEvent, final boolean sendNotification,
            final HttpServletRequest request, final MessageSource messageSource) {
        save(newEvent);

        if (sendNotification) {
            notifyUsersOfCreation(newEvent, request, messageSource);
        }
    }

    @Override
    public void notifyUsersOfCreation(final Object newEvent, final HttpServletRequest request,
            final MessageSource messageSource) {
        emailService.sendEmailOnCreation(newEvent, gainUsersToNotify(), request, messageSource);
    }

    /**
     * Deletes the given {@link CalendarEvent} from the repository.
     */
    public void delete(final CalendarEvent event) {
        eventDao.delete(event);
    }

    /**
     * Deletes the specified {@link CalendarEvent} from the repository.
     *
     * @param eventId
     *            ID of the event
     */
    public void delete(final int eventId) {
        final CalendarEvent event = findById(eventId);
        eventDao.delete(event);
    }

    /**
     * Gets a {@link Map} which for each input {@link CalendarEvent} contains a corresponding localized delete question
     * which is asked before deletion of that event.
     */
    public static Map<CalendarEvent, String> getLocalizedDeleteQuestions(final List<CalendarEvent> events,
            final HttpServletRequest request, final MessageSource messageSource) {
        final String messageCode = "event-calendar.do-you-really-want-to-delete-event";
        final Map<CalendarEvent, String> questions = new HashMap<>();

        for (final CalendarEvent event : events) {
            final Object[] messageParams = new Object[] { event.getName() };
            questions.put(event, Localization.findLocaleMessage(messageSource, request, messageCode, messageParams));
        }
        return questions;
    }

}
