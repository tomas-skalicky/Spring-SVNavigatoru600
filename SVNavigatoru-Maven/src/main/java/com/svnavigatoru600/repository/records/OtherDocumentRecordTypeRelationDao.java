package com.svnavigatoru600.repository.records;

import java.util.Collection;
import java.util.List;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;
import com.svnavigatoru600.repository.MapperInterface;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@MapperInterface
public interface OtherDocumentRecordTypeRelationDao {

    /**
     * Returns all {@link OtherDocumentRecordTypeRelation types} stored in the repository which are associated
     * with the specified {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord}.
     * 
     * @param recordId
     *            The ID of the document record which desired types are associated with.
     */
    List<OtherDocumentRecordTypeRelation> findAll(int recordId);

    /**
     * Stores the given {@link OtherDocumentRecordTypeRelation types} to the repository. If there is already
     * an {@link OtherDocumentRecordTypeRelation type} with the same
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelationId#getRecordId() ID} of
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord} and the same
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelationId#getType() type}, throws an
     * exception.
     */
    void save(Collection<OtherDocumentRecordTypeRelation> types);

    /**
     * Deletes all {@link OtherDocumentRecordTypeRelation types} of the specified
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord}.
     * 
     * @param recordId
     *            The ID of the document record which desired types are associated with.
     */
    void delete(int recordId);
}
