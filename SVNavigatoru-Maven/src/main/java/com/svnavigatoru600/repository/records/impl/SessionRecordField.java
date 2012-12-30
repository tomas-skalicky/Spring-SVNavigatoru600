package com.svnavigatoru600.repository.records.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.records.SessionRecord SessionRecord} class.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum SessionRecordField {

    id(SessionRecordColumn.id), type(SessionRecordColumn.type), sessionDate(SessionRecordColumn.session_date), discussedTopics(
            SessionRecordColumn.discussed_topics);

    /**
     * The name of a corresponding database column.
     */
    private final SessionRecordColumn column;

    private SessionRecordField(SessionRecordColumn column) {
        this.column = column;
    }

    public String getColumnName() {
        return this.column.name();
    }

    /**
     * Names of the columns of the database table which contains
     * {@link com.svnavigatoru600.domain.records.SessionRecord SessionRecords}.
     */
    private enum SessionRecordColumn {

        id, type, session_date, discussed_topics
    }
}
