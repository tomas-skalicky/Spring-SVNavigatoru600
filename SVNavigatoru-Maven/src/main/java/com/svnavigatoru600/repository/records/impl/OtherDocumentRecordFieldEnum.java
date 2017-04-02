package com.svnavigatoru600.repository.records.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord} class.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum OtherDocumentRecordFieldEnum {

    // @formatter:off
    //               columnName
    ID              ("id"),
    NAME            ("name"),
    DESCRIPTION     ("description"),
    CREATION_TIME   ("creation_time"),
    LAST_SAVE_TIME  ("last_save_time"),
    ;
    // @formatter:on

    /**
     * The name of a corresponding database column.
     */
    private final String columnName;

    private OtherDocumentRecordFieldEnum(final String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

}
