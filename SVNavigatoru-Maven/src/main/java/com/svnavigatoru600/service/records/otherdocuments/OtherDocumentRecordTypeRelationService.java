package com.svnavigatoru600.service.records.otherdocuments;

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
}
