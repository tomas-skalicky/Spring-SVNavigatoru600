package svnavigatoru.repository.records;

import java.util.List;

import svnavigatoru.domain.records.OtherDocumentRecord;
import svnavigatoru.domain.records.OtherDocumentRecordType;
import svnavigatoru.service.util.OrderType;

public interface OtherDocumentRecordDao extends DocumentRecordDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see svnavigatoru.repository.DocumentRecordDao#findById(int)
	 */
	public OtherDocumentRecord findById(int documentId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see svnavigatoru.repository.records.DocumentRecordDao#findById(int,
	 * boolean)
	 */
	public OtherDocumentRecord findById(int documentId, boolean loadFile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * svnavigatoru.repository.DocumentRecordDao#findByFileName(java.lang.String
	 * )
	 */
	public OtherDocumentRecord findByFileName(String fileName);

	/**
	 * Returns all {@link OtherDocumentRecord}s stored in the repository
	 * arranged according to their <code>creationTime</code>s in the given
	 * <code>order</code>.
	 */
	public List<OtherDocumentRecord> findOrdered(OrderType order);

	/**
	 * Returns all {@link OtherDocumentRecord}s stored in the repository which
	 * are of the given <code>type</code>. The {@link OtherDocumentRecord} are
	 * arranged according to their <code>creationTime</code>s in the given
	 * <code>order</code>.
	 */
	public List<OtherDocumentRecord> findOrdered(OtherDocumentRecordType type, OrderType order);

	/**
	 * Updates the given <code>document</code> in the repository. The old
	 * version of the <code>document</code> should be already stored there.
	 */
	public void update(OtherDocumentRecord document);

	/**
	 * Stores the given <code>document</code> to the repository. If there is
	 * already a {@link OtherDocumentRecord} with the same filename, throws an
	 * exception.
	 * 
	 * @return the generated identifier
	 */
	public int save(OtherDocumentRecord document);
}
