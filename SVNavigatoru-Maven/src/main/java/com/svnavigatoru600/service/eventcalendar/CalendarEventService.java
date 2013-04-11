package com.svnavigatoru600.service.eventcalendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.eventcalendar.PriorityType;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.service.SubjectOfNotificationService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderType;

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
    private UserService userService;
    /**
     * Assembles notification emails and sends them to authorized {@link User users}.
     */
    private CalendarEventNotificationEmailService emailService;

    /**
     * Constructor.
     */
    @Inject
    public CalendarEventService(CalendarEventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Inject
    public void setEmailService(CalendarEventNotificationEmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Returns a {@link CalendarEvent} stored in the repository which has the given ID.
     */
    public CalendarEvent findById(int eventId) {
        return this.eventDao.findById(eventId);
    }

    /**
     * Returns all {@link CalendarEvent CalendarEvents} stored in the repository arranged according to their
     * {@link CalendarEvent#getDate() date} ascending.
     * <p>
     * Moreover, only {@link CalendarEvent CalendarEvents} which will take place are returned, the passed ones
     * are not.
     */
    public List<CalendarEvent> findAllFutureEventsOrdered() {
        return this.eventDao.findAllFutureEventsOrdered(DateUtils.getToday(), OrderType.ASCENDING);
    }

    /**
     * Updates the given {@link CalendarEvent} in the repository. The old version of this event should be
     * already stored there.
     */
    public void update(CalendarEvent event) {
        this.eventDao.update(event);
    }

    /**
     * Updates properties of the given <code>eventToUpdate</code> and persists this {@link CalendarEvent} into
     * the repository. The old version of this event should be already stored there.
     * 
     * @param eventToUpdate
     *            Persisted {@link CalendarEvent}
     * @param newEvent
     *            {@link CalendarEvent} which contains new values of properties of <code>eventToUpdate</code>.
     *            These values are copied to the persisted event.
     * @param newPriority
     *            Priority which is to be the new one of the <code>eventToUpdate</code>
     */
    public void update(CalendarEvent eventToUpdate, CalendarEvent newEvent, PriorityType newPriority) {
        eventToUpdate.setName(newEvent.getName());
        eventToUpdate.setDate(newEvent.getDate());
        eventToUpdate.setDescription(newEvent.getDescription());
        eventToUpdate.setPriority(newPriority);
        this.update(eventToUpdate);
    }

    /**
     * Updates properties of the given <code>eventToUpdate</code> and persists this {@link CalendarEvent} into
     * the repository. The old version of this event should be already stored there.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about changes in the event.
     * 
     * @param eventToUpdate
     *            Persisted {@link CalendarEvent}
     * @param newEvent
     *            {@link CalendarEvent} which contains new values of properties of <code>eventToUpdate</code>.
     *            These values are copied to the persisted event.
     * @param newPriority
     *            Priority which is to be the new one of the <code>eventToUpdate</code>
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void updateAndNotifyUsers(CalendarEvent eventToUpdate, CalendarEvent newEvent,
            PriorityType newPriority, boolean sendNotification, HttpServletRequest request,
            MessageSource messageSource) {
        this.update(eventToUpdate, newEvent, newPriority);

        if (sendNotification) {
            notifyUsersOfUpdate(eventToUpdate, request, messageSource);
        }
    }

    @Override
    public List<User> gainUsersToNotify() {
        return this.userService.findAllWithEmailByAuthorityAndSubscription(AuthorityType.ROLE_MEMBER_OF_SV,
                this.emailService.getNotificationType());
    }

    @Override
    public void notifyUsersOfUpdate(Object updatedEvent, HttpServletRequest request,
            MessageSource messageSource) {
        this.emailService.sendEmailOnUpdate(updatedEvent, gainUsersToNotify(), request, messageSource);
    }

    /**
     * Stores the given {@link CalendarEvent} to the repository.
     * 
     * @return New ID of the given {@link CalendarEvent} generated by the repository
     */
    public int save(CalendarEvent event) {
        return this.eventDao.save(event);
    }

    /**
     * Updates corresponding {@link java.util.Date Date} fields of the given new {@link CalendarEvent} (if
     * there are any appropriate) and stores the event to the repository.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about a creation of the event.
     * 
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void saveAndNotifyUsers(CalendarEvent newEvent, boolean sendNotification,
            HttpServletRequest request, MessageSource messageSource) {
        save(newEvent);

        if (sendNotification) {
            notifyUsersOfCreation(newEvent, request, messageSource);
        }
    }

    @Override
    public void notifyUsersOfCreation(Object newEvent, HttpServletRequest request, MessageSource messageSource) {
        this.emailService.sendEmailOnCreation(newEvent, gainUsersToNotify(), request, messageSource);
    }

    /**
     * Deletes the given {@link CalendarEvent} from the repository.
     */
    public void delete(CalendarEvent event) {
        this.eventDao.delete(event);
    }

    /**
     * Deletes the specified {@link CalendarEvent} from the repository.
     * 
     * @param eventId
     *            ID of the event
     */
    public void delete(int eventId) {
        CalendarEvent event = findById(eventId);
        this.eventDao.delete(event);
    }

    /**
     * Gets a {@link Map} which for each input {@link CalendarEvent} contains a corresponding localized delete
     * question which is asked before deletion of that event.
     */
    public static Map<CalendarEvent, String> getLocalizedDeleteQuestions(List<CalendarEvent> events,
            HttpServletRequest request, MessageSource messageSource) {
        String messageCode = "event-calendar.do-you-really-want-to-delete-event";
        Map<CalendarEvent, String> questions = new HashMap<CalendarEvent, String>();

        for (CalendarEvent event : events) {
            Object[] messageParams = new Object[] { event.getName() };
            questions.put(event,
                    Localization.findLocaleMessage(messageSource, request, messageCode, messageParams));
        }
        return questions;
    }
}
