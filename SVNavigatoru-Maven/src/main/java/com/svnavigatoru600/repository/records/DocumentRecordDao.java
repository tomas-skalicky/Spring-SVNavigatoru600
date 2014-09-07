package com.svnavigatoru600.repository.records;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public interface DocumentRecordDao {

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which has the given ID.
     * <p>
     * Record's associated {@link AbstractDocumentRecord#getFile() file} is loaded as well.
     */
    AbstractDocumentRecord findById(int recordId);

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which has the given ID.
     * 
     * @param loadFile
     *            If <code>true</code>, the associated {@link AbstractDocumentRecord#getFile() file} will be
     *            loaded as well; otherwise not.
     */
    AbstractDocumentRecord findById(int recordId, boolean loadFile);

    /**
     * Deletes the given {@link AbstractDocumentRecord} together with all its types from the repository.
     */
    void delete(AbstractDocumentRecord record);
}
