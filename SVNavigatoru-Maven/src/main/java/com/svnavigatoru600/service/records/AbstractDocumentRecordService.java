package com.svnavigatoru600.service.records;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.repository.records.DocumentRecordDao;

/**
 * Provides convenient methods to work with {@link AbstractDocumentRecord} objects.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractDocumentRecordService {

    /**
     * The object which provides a persistence.
     */
    private DocumentRecordDao recordDao;

    @Inject
    public void setDocumentDao(DocumentRecordDao documentDao) {
        this.recordDao = documentDao;
    }

    /**
     * Deletes the given {@link AbstractDocumentRecord} together with all its types from the repository.
     */
    public void delete(AbstractDocumentRecord document) {
        this.recordDao.delete(document);
    }
}
