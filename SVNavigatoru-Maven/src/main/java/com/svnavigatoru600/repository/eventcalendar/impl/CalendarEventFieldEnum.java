package com.svnavigatoru600.repository.eventcalendar.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.eventcalendar.CalendarEvent CalendarEvent} class.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum CalendarEventFieldEnum {

    // @formatter:off
    //               columnName
    ID              ("id"),
    NAME            ("name"),
    DATE            ("date"),
    DESCRIPTION     ("description"),
    PRIORITY        ("priority"),
    ;
    // @formatter:on

    /**
     * The name of a corresponding database column.
     */
    private final String columnName;

    private CalendarEventFieldEnum(final String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

}
