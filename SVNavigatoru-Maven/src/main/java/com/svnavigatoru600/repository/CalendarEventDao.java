package com.svnavigatoru600.repository;

import java.util.List;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.repository.eventcalendar.impl.FindAllFutureEventsOrderedArguments;

@MapperInterface
public interface CalendarEventDao {

    /**
     * Returns a {@link CalendarEvent} stored in the repository which has the given <code>ID</code>.
     */
    CalendarEvent findById(int eventId);

    /**
     * Returns all {@link CalendarEvent}s stored in the repository arranged according to their
     * <code>date</code> attributes in the given <code>order</code>. Moreover, only {@link CalendarEvent}s
     * which will take place are returned, the passed ones are not.
     */
    List<CalendarEvent> findAllFutureEventsOrdered(FindAllFutureEventsOrderedArguments arguments);

    /**
     * Updates the given <code>event</code> in the repository. The old version of the <code>event</code>
     * should be already stored there.
     */
    void update(CalendarEvent event);

    /**
     * Stores the given <code>event</code> to the repository.
     * 
     * @return the generated identifier
     */
    int save(CalendarEvent event);

    /**
     * Deletes the given <code>event</code> from the repository.
     */
    void delete(CalendarEvent event);
}
