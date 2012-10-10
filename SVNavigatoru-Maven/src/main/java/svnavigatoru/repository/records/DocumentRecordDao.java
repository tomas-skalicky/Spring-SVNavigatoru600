package svnavigatoru.repository.records;

import svnavigatoru.domain.records.DocumentRecord;

public interface DocumentRecordDao {

	/**
	 * Returns a {@link DocumentRecord} stored in the repository which has the
	 * given <code>ID</code>.
	 */
	public DocumentRecord findById(int documentId);

	/**
	 * Returns a {@link DocumentRecord} stored in the repository which has the
	 * given <code>ID</code>.
	 * 
	 * @param loadFile
	 *            Indicator whether the <b>Blob</b> file will be loaded as well,
	 *            or not.
	 */
	public DocumentRecord findById(int documentId, boolean loadFile);

	/**
	 * Returns a {@link DocumentRecord} stored in the repository which is
	 * associated with a file with the given <code>fileName</code>.
	 */
	public DocumentRecord findByFileName(String fileName);

	/**
	 * Deletes the given <code>document</code> together with all its types from
	 * the repository.
	 */
	public void delete(DocumentRecord document);
}
