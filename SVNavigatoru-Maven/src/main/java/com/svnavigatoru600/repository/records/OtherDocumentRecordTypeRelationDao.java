package com.svnavigatoru600.repository.records;

import java.util.Collection;
import java.util.List;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;
import com.svnavigatoru600.repository.MapperInterface;

@MapperInterface
public interface OtherDocumentRecordTypeRelationDao {

    /**
     * Returns all {@link OtherDocumentRecordTypeRelation}s stored in the repository which are associated with
     * the {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord} with the given
     * <code>recordId</code>.
     */
    List<OtherDocumentRecordTypeRelation> find(int recordId);

    /**
     * Stores the given <code>types</code> to the repository. If there is already an
     * {@link OtherDocumentRecordTypeRelation} with the same ID of
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord} and the same type,
     * throws an exception.
     */
    void save(Collection<OtherDocumentRecordTypeRelation> types);

    /**
     * Deletes all {@link OtherDocumentRecordTypeRelation} of
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord} with the given
     * <code>recordId</code> from the repository.
     */
    void delete(int recordId);
}
