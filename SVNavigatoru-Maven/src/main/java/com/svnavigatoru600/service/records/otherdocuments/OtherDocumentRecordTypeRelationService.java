package com.svnavigatoru600.service.records.otherdocuments;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;
import com.svnavigatoru600.repository.records.OtherDocumentRecordTypeRelationDao;

/**
 * Provides convenient methods to work with {@link OtherDocumentRecordTypeRelation} objects.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class OtherDocumentRecordTypeRelationService {

    /**
     * The object which provides a persistence.
     */
    private final OtherDocumentRecordTypeRelationDao typeRelationDao;

    /**
     * Constructor.
     */
    @Inject
    public OtherDocumentRecordTypeRelationService(OtherDocumentRecordTypeRelationDao typeRelationDao) {
        this.typeRelationDao = typeRelationDao;
    }

    /**
     * Returns all {@link OtherDocumentRecordTypeRelation type relations} stored in the repository which are
     * associated with the specified {@link com.svnavigatoru600.domain.records.OtherDocumentRecord
     * OtherDocumentRecord}.
     * 
     * @param recordId
     *            The ID of the document record which desired type relations are associated with.
     */
    public List<OtherDocumentRecordTypeRelation> findAll(int recordId) {
        return this.typeRelationDao.findAll(recordId);
    }

    /**
     * Stores the given {@link OtherDocumentRecordTypeRelation typeRelations} to the repository. If there is
     * already a {@link OtherDocumentRecordTypeRelation type relation} with the same
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelationId#getRecordId() ID} of
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord} and the same
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelationId#getType() type}, throws an
     * exception.
     */
    public void save(Collection<OtherDocumentRecordTypeRelation> typeRelations) {
        this.typeRelationDao.save(typeRelations);
    }

    /**
     * Deletes all {@link OtherDocumentRecordTypeRelation type relations} of the specified
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord}.
     * 
     * @param recordId
     *            The ID of the document record which desired type relations are associated with.
     */
    public void delete(int recordId) {
        this.typeRelationDao.delete(recordId);
    }
}
