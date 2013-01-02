package com.svnavigatoru600.service.records.otherdocuments;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;

/**
 * Provides convenient methods to work with {@link OtherDocumentRecord} objects.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class OtherDocumentRecordService {

    /**
     * The object which provides a persistence.
     */
    private final OtherDocumentRecordDao recordDao;

    /**
     * Constructor.
     */
    @Inject
    public OtherDocumentRecordService(OtherDocumentRecordDao recordDao) {
        this.recordDao = recordDao;
    }
}
