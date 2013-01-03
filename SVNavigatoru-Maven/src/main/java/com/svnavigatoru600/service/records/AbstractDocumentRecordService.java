package com.svnavigatoru600.service.records;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.repository.records.DocumentRecordDao;

/**
 * Provides convenient methods to work with {@link AbstractDocumentRecord} objects.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractDocumentRecordService {

    /**
     * The object which provides a persistence.
     */
    private DocumentRecordDao documentDao;

    public AbstractDocumentRecordService(DocumentRecordDao documentDao) {
        this.documentDao = documentDao;
    }

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which has the given ID.
     * <p>
     * The <em>Blob</em> file is loaded as well.
     */
    public abstract AbstractDocumentRecord findById(int recordId);

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which has the given ID.
     * 
     * @param loadFile
     *            If <code>true</code>, the <em>Blob</em> file will be loaded as well; otherwise not.
     */
    public abstract AbstractDocumentRecord findById(int recordId, boolean loadFile);

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which is associated with a file with
     * the given <code>fileName</code>.
     */
    public abstract AbstractDocumentRecord findByFileName(String fileName);

    /**
     * Deletes the given {@link AbstractDocumentRecord} together with all its types from the repository.
     */
    public void delete(AbstractDocumentRecord document) {
        this.documentDao.delete(document);
    }
}
