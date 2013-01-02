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
     * The <em>Blob</em> file is loaded as well.
     */
    AbstractDocumentRecord findById(int documentId);

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which has the given ID.
     * 
     * @param loadFile
     *            If <code>true</code>, the <em>Blob</em> file will be loaded as well; otherwise not.
     */
    AbstractDocumentRecord findById(int documentId, boolean loadFile);

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which is associated with a file with
     * the given <code>fileName</code>.
     */
    AbstractDocumentRecord findByFileName(String fileName);

    /**
     * Deletes the given {@link AbstractDocumentRecord} together with all its types from the repository.
     */
    void delete(AbstractDocumentRecord document);
}
