package com.svnavigatoru600.repository.records.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.records.SessionRecord SessionRecord} class.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum SessionRecordFieldEnum {

    // @formatter:off
    //                   columnName
    ID                  ("id"),
    TYPE                ("type"),
    SESSION_DATE        ("session_date"),
    DISCUSSED_TOPICS    ("discussed_topics"),
    ;
    // @formatter:on

    /**
     * The name of a corresponding database column.
     */
    private final String columnName;

    private SessionRecordFieldEnum(final String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

}
