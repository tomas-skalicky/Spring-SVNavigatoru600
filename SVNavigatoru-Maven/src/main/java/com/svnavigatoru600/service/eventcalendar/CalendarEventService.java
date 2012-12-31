package com.svnavigatoru600.service.eventcalendar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.repository.eventcalendar.impl.FindAllFutureEventsOrderedArguments;

/**
 * Provides convenient functions to work with {@link CalendarEvent} objects.
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
    @Autowired
    public CalendarEventService(CalendarEventDao calendarEventDao) {
        this.eventDao = calendarEventDao;
    }

    /**
     * Returns a {@link CalendarEvent} stored in the repository which has the given <code>ID</code>.
     */
    public CalendarEvent findById(int eventId) {
        return this.eventDao.findById(eventId);
    }

    /**
     * Returns all {@link CalendarEvent}s stored in the repository arranged according to their
     * <code>date</code> attributes in the given <code>order</code>. Moreover, only {@link CalendarEvent}s
     * which will take place are returned, the passed ones are not.
     */
    public List<CalendarEvent> findAllFutureEventsOrdered(FindAllFutureEventsOrderedArguments arguments) {
        return this.eventDao.findAllFutureEventsOrdered(arguments);
    }

    /**
     * Updates the given <code>event</code> in the repository. The old version of the <code>event</code>
     * should be already stored there.
     */
    public void update(CalendarEvent event) {
        this.eventDao.update(event);
    }

    /**
     * Stores the given <code>event</code> to the repository.
     * 
     * @return the generated identifier
     */
    public int save(CalendarEvent event) {
        return this.eventDao.save(event);
    }

    /**
     * Deletes the given <code>event</code> from the repository.
     */
    public void delete(CalendarEvent event) {
        this.eventDao.delete(event);
    }
}
