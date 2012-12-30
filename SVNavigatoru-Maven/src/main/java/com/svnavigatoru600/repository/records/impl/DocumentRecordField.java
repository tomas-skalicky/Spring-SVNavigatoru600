package com.svnavigatoru600.repository.records.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.records.DocumentRecord DocumentRecord} class.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum DocumentRecordField {

    id(DocumentRecordColumn.id), fileName(DocumentRecordColumn.file_name), file(DocumentRecordColumn.file);

    /**
     * The name of a corresponding database column.
     */
    private final DocumentRecordColumn column;

    private DocumentRecordField(DocumentRecordColumn column) {
        this.column = column;
    }

    public String getColumnName() {
        return this.column.name();
    }

    /**
     * Names of the columns of the database table which contains
     * {@link com.svnavigatoru600.domain.records.DocumentRecord document records}.
     */
    private enum DocumentRecordColumn {

        id, file_name, file
    }
}
