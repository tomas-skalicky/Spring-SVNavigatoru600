package com.svnavigatoru600.repository.records;

import java.util.List;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.service.util.OrderType;


public interface OtherDocumentRecordDao extends DocumentRecordDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.svnavigatoru600.repository.DocumentRecordDao#findById(int)
	 */
	public OtherDocumentRecord findById(int documentId);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.svnavigatoru600.repository.records.DocumentRecordDao#findById(int,
	 * boolean)
	 */
	public OtherDocumentRecord findById(int documentId, boolean loadFile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.svnavigatoru600.repository.DocumentRecordDao#findByFileName(java.lang.String
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
