package com.svnavigatoru600.domain.records;

import java.io.Serializable;

import javax.inject.Inject;

import com.svnavigatoru600.service.records.OtherDocumentRecordTypeRelationService;

/**
 * Helps to map the <code>types</code> array in the {@link OtherDocumentRecordType} class to Hibernate.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class OtherDocumentRecordTypeRelation implements Serializable {

    private static final long serialVersionUID = -490430948638448565L;

    @SuppressWarnings("unused")
    private OtherDocumentRecordTypeRelationService typeService;

    @Inject
    public void setOtherDocumentRecordTypeRelationService(
            final OtherDocumentRecordTypeRelationService typeService) {
        this.typeService = typeService;
    }

    /**
     * Default constructor. Necessary.
     */
    public OtherDocumentRecordTypeRelation() {
    }

    public OtherDocumentRecordTypeRelation(int recordId, OtherDocumentRecordType type) {
        this.id = new OtherDocumentRecordTypeRelationId();
        this.id.setRecordId(recordId);
        this.id.setType(type);
    }

    private OtherDocumentRecordTypeRelationId id;

    public OtherDocumentRecordTypeRelationId getId() {
        return this.id;
    }

    public void setId(OtherDocumentRecordTypeRelationId id) {
        this.id = id;
    }
}
