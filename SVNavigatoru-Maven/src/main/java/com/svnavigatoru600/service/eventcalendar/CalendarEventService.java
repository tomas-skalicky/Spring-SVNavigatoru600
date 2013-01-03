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
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderType;

/**
 * Provides convenient methods to work with {@link CalendarEvent} objects.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class CalendarEventService {

    /**
     * The object which provides a persistence.
     */
    private final CalendarEventDao eventDao;

    /**
     * Constructor.
     */
    @Inject
    public CalendarEventService(CalendarEventDao eventDao) {
        this.eventDao = eventDao;
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
     *            The persisted {@link CalendarEvent}
     * @param newEvent
     *            The {@link CalendarEvent} which contains new values of properties of
     *            <code>eventToUpdate</code>. These values are copied to the persisted event.
     * @param newPriority
     *            The priority which is to be the new one of the <code>eventToUpdate</code>
     */
    public void update(CalendarEvent eventToUpdate, CalendarEvent newEvent, PriorityType newPriority) {
        eventToUpdate.setName(newEvent.getName());
        eventToUpdate.setDate(newEvent.getDate());
        eventToUpdate.setDescription(newEvent.getDescription());
        eventToUpdate.setPriority(newPriority);
        this.update(eventToUpdate);
    }

    /**
     * Stores the given {@link CalendarEvent} to the repository.
     * 
     * @return The new ID of the given {@link CalendarEvent} generated by the repository
     */
    public int save(CalendarEvent event) {
        return this.eventDao.save(event);
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
     *            The ID of the event
     */
    public void delete(int eventId) {
        CalendarEvent event = this.findById(eventId);
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
