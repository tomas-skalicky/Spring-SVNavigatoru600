package com.svnavigatoru600.repository.records;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.repository.MapperInterface;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@MapperInterface
public interface DocumentRecordDao {

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which has the given <code>ID</code>.
     */
    AbstractDocumentRecord findById(int documentId);

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which has the given <code>ID</code>.
     * 
     * @param loadFile
     *            Indicator whether the <b>Blob</b> file will be loaded as well, or not.
     */
    AbstractDocumentRecord findById(int documentId, boolean loadFile);

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which is associated with a file with the
     * given <code>fileName</code>.
     */
    AbstractDocumentRecord findByFileName(String fileName);

    /**
     * Deletes the given <code>document</code> together with all its types from the repository.
     */
    void delete(AbstractDocumentRecord document);
}
