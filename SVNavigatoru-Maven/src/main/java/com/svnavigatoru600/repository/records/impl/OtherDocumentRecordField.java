package com.svnavigatoru600.repository.records.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord} class.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum OtherDocumentRecordField {

    id(OtherDocumentRecordColumn.id), name(OtherDocumentRecordColumn.name), description(
            OtherDocumentRecordColumn.description), creationTime(
                    OtherDocumentRecordColumn.creation_time), lastSaveTime(OtherDocumentRecordColumn.last_save_time);

    /**
     * The name of a corresponding database column.
     */
    private final OtherDocumentRecordColumn column;

    private OtherDocumentRecordField(OtherDocumentRecordColumn column) {
        this.column = column;
    }

    public String getColumnName() {
        return this.column.name();
    }

    /**
     * Names of the columns of the database table which contains
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecords}.
     */
    private enum OtherDocumentRecordColumn {

        id, name, description, creation_time, last_save_time
    }
}
