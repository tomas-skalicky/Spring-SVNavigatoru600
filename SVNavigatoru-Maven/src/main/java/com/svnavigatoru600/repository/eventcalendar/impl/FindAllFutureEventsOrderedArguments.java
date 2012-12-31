package com.svnavigatoru600.repository.eventcalendar.impl;

import java.util.Date;

import com.svnavigatoru600.service.util.OrderType;

/**
 * Encapsulates arguments of the
 * {@link com.svnavigatoru600.repository.CalendarEventDao#findFutureEventsOrdered findFutureEventsOrdered}
 * method in order to pass just one argument to the method. It is the requirement of MyBatis.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class FindAllFutureEventsOrderedArguments {

    /**
     * Field of {@link com.svnavigatoru600.domain.eventcalendar.CalendarEvent CalendarEvent} according which
     * the result records will be sorted.
     */
    private static final CalendarEventField SORT_FIELD = CalendarEventField.date;
    /**
     * The earliest {@link Date} which is considered in the result. The latest {@link Date} is not determined
     * here.
     */
    private final Date earliestDate;
    /**
     * The direction of sorting.
     */
    private final OrderType sortDirection;

    public FindAllFutureEventsOrderedArguments(Date earliestDate, OrderType sortDirection) {
        this.earliestDate = earliestDate;
        this.sortDirection = sortDirection;
    }

    public CalendarEventField getSortField() {
        return SORT_FIELD;
    }

    public Date getEarliestDate() {
        return this.earliestDate;
    }

    public OrderType getSortDirection() {
        return this.sortDirection;
    }
}
