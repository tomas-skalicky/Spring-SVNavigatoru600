package com.svnavigatoru600.repository.eventcalendar.impl;

import java.util.Date;

import com.svnavigatoru600.service.util.OrderType;

/**
 * Encapsulates arguments of the
 * {@link com.svnavigatoru600.repository.CalendarEventDao#findFutureEventsOrdered findFutureEventsOrdered}
 * method in order to pass just one argument to the method. It is the requirement of MyBatis.
 * 
 * @author Tomas Skalicky
 */
public class FindFutureEventsOrderedArguments {

    /**
     * The table column according which the result records will be sorted.
     */
    private static final String SORT_COLUMN = "date";
    /**
     * The earliest {@link Date} which is considered in the result. The latest {@link Date} is not determined
     * here.
     */
    private final Date earliestDate;
    /**
     * The direction of sorting.
     */
    private final OrderType sortDirection;

    public FindFutureEventsOrderedArguments(Date earliestDate, OrderType sortDirection) {
        this.earliestDate = earliestDate;
        this.sortDirection = sortDirection;
    }

    public String getSortColumn() {
        return SORT_COLUMN;
    }

    public Date getEarliestDate() {
        return this.earliestDate;
    }

    public OrderType getSortDirection() {
        return this.sortDirection;
    }
}
