package com.svnavigatoru600.repository.records;

import com.svnavigatoru600.domain.records.DocumentRecord;

public interface DocumentRecordDao {

    /**
     * Returns a {@link DocumentRecord} stored in the repository which has the given <code>ID</code>.
     */
    DocumentRecord findById(int documentId);

    /**
     * Returns a {@link DocumentRecord} stored in the repository which has the given <code>ID</code>.
     * 
     * @param loadFile
     *            Indicator whether the <b>Blob</b> file will be loaded as well, or not.
     */
    DocumentRecord findById(int documentId, boolean loadFile);

    /**
     * Returns a {@link DocumentRecord} stored in the repository which is associated with a file with the
     * given <code>fileName</code>.
     */
    DocumentRecord findByFileName(String fileName);

    /**
     * Deletes the given <code>document</code> together with all its types from the repository.
     */
    void delete(DocumentRecord document);
}
