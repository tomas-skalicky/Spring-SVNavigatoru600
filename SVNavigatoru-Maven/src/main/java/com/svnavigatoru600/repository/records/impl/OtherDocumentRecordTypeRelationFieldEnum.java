package com.svnavigatoru600.repository.records.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation
 * OtherDocumentRecordTypeRelation} class.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum OtherDocumentRecordTypeRelationFieldEnum {

    // @formatter:off
    //           columnName    fieldChain
    RECORD_ID   ("record_id",  "id.recordId"),
    TYPE        ("type",       "id.type"),
    ;
    // @formatter:on

    /**
     * The name of a corresponding database column.
     */
    private final String columnName;
    /**
     * The chain of fields. Used when the {@link OtherDocumentRecordTypeRelationFieldEnum} is not located directly in the
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation OtherDocumentRecordTypeRelation} class.
     */
    private final String fieldChain;

    private OtherDocumentRecordTypeRelationFieldEnum(final String columnName, final String fieldChain) {
        this.columnName = columnName;
        this.fieldChain = fieldChain;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getFieldChain() {
        return fieldChain;
    }

}
