package com.svnavigatoru600.repository.records;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.repository.MapperInterface;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@MapperInterface
public interface DocumentRecordDao {

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which has the given ID.
     * <p>
     * The {@link java.sql.Blob Blob} file is loaded as well.
     */
    AbstractDocumentRecord findById(int recordId);

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which has the given ID.
     * 
     * @param loadFile
     *            If <code>true</code>, the {@link java.sql.Blob Blob} file will be loaded as well; otherwise
     *            not.
     */
    AbstractDocumentRecord findById(int recordId, boolean loadFile);

    /**
     * Deletes the given {@link AbstractDocumentRecord} together with all its types from the repository.
     */
    void delete(AbstractDocumentRecord record);
}
