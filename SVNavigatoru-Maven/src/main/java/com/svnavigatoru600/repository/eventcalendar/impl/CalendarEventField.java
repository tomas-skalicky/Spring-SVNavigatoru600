package com.svnavigatoru600.repository.eventcalendar.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.eventcalendar.CalendarEvent CalendarEvent}
 * class.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum CalendarEventField {

    id(CalendarEventColumn.id), name(CalendarEventColumn.name), date(CalendarEventColumn.date), description(
            CalendarEventColumn.description), priority(CalendarEventColumn.priority);

    /**
     * The name of a corresponding database column.
     */
    private final CalendarEventColumn column;

    private CalendarEventField(CalendarEventColumn column) {
        this.column = column;
    }

    public String getColumnName() {
        return this.column.name();
    }

    /**
     * Names of the columns of the database table which contains
     * {@link com.svnavigatoru600.domain.eventcalendar.CalendarEvent calendar events}.
     */
    private enum CalendarEventColumn {

        id, name, date, description, priority
    }
}
