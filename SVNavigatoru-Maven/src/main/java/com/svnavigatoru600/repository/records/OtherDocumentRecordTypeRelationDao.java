package com.svnavigatoru600.repository.records;

import java.util.Collection;
import java.util.List;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public interface OtherDocumentRecordTypeRelationDao {

    /**
     * Returns all {@link OtherDocumentRecordTypeRelation type relations} stored in the repository which are associated
     * with the specified {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord}.
     * 
     * @param recordId
     *            The ID of the document record which desired type relations are associated with.
     */
    List<OtherDocumentRecordTypeRelation> findByRecordId(int recordId);

    /**
     * Stores the given {@link OtherDocumentRecordTypeRelation typeRelations} to the repository. If there is already a
     * {@link OtherDocumentRecordTypeRelation type relation} with the same
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelationId#getRecordId() ID} of
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord} and the same
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelationId#getType() type}, throws an exception.
     */
    void save(Collection<OtherDocumentRecordTypeRelation> typeRelations);

    /**
     * Deletes all {@link OtherDocumentRecordTypeRelation type relations} of the specified
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord}.
     * 
     * @param recordId
     *            The ID of the document record which desired type relations are associated with.
     */
    void delete(int recordId);
}
