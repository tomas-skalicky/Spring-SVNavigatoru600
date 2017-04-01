package com.svnavigatoru600.repository.records.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation
 * OtherDocumentRecordTypeRelation} class.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum OtherDocumentRecordTypeRelationField {

    recordId(OtherDocumentRecordTypeRelationColumn.record_id,
            "id.recordId"), type(OtherDocumentRecordTypeRelationColumn.type, "id.type");

    /**
     * The name of a corresponding database column.
     */
    private final OtherDocumentRecordTypeRelationColumn column;
    /**
     * The chain of fields. Used when the {@link OtherDocumentRecordTypeRelationField} is not located directly in the
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation OtherDocumentRecordTypeRelation} class.
     */
    private final String fieldChain;

    private OtherDocumentRecordTypeRelationField(OtherDocumentRecordTypeRelationColumn column) {
        this(column, null);
    }

    private OtherDocumentRecordTypeRelationField(OtherDocumentRecordTypeRelationColumn column, String fieldChain) {
        this.column = column;
        this.fieldChain = fieldChain;
    }

    public String getColumnName() {
        return this.column.name();
    }

    public String getFieldChain() {
        return this.fieldChain;
    }

    /**
     * Names of the columns of the database table which contains
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation record type relations}.
     */
    private enum OtherDocumentRecordTypeRelationColumn {

        record_id, type
    }
}
