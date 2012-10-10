package svnavigatoru.repository.records;

import java.util.List;

import svnavigatoru.domain.records.SessionRecord;
import svnavigatoru.domain.records.SessionRecordType;
import svnavigatoru.service.util.OrderType;

public interface SessionRecordDao extends DocumentRecordDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see svnavigatoru.repository.DocumentRecordDao#findById(int)
	 */
	public SessionRecord findById(int recordId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see svnavigatoru.repository.records.DocumentRecordDao#findById(int,
	 * boolean)
	 */
	public SessionRecord findById(int recordId, boolean loadFile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * svnavigatoru.repository.DocumentRecordDao#findByFileName(java.lang.String
	 * )
	 */
	public SessionRecord findByFileName(String fileName);

	/**
	 * Returns all {@link SessionRecord}s stored in the repository arranged
	 * according to their <code>sessionDate</code>s in the given
	 * <code>order</code>.
	 */
	public List<SessionRecord> findOrdered(OrderType order);

	/**
	 * Returns all {@link SessionRecord}s stored in the repository which are of
	 * the given <code>type</code>. The {@link SessionRecord} are arranged
	 * according to their <code>sessionDate</code>s in the given
	 * <code>order</code>.
	 */
	public List<SessionRecord> findOrdered(SessionRecordType type, OrderType order);

	/**
	 * Updates the given <code>record</code> in the repository. The old version
	 * of the <code>record</code> should be already stored there.
	 */
	public void update(SessionRecord record);

	/**
	 * Stores the given <code>record</code> to the repository. If there is
	 * already a {@link SessionRecord} with the same filename, throws an
	 * exception.
	 * 
	 * @return the generated identifier
	 */
	public int save(SessionRecord record);
}
