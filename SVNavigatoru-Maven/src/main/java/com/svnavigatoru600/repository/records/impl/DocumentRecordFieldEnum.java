package com.svnavigatoru600.repository.records.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.records.AbstractDocumentRecord DocumentRecord} class.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum DocumentRecordFieldEnum {

    // @formatter:off
    //           columnName
    ID          ("id"),
    FILE_NAME   ("file_name"),
    FILE        ("file"),
    ;
    // @formatter:on

    /**
     * The name of a corresponding database column.
     */
    private final String columnName;

    private DocumentRecordFieldEnum(final String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

}
