package com.svnavigatoru600.domain.records;

import java.io.Serializable;

import com.svnavigatoru600.repository.records.OtherDocumentRecordTypeRelationDao;


/**
 * Helps to map the <code>types</code> array in the
 * {@link OtherDocumentRecordType} class to Hibernate.
 * 
 * @author Tomas Skalicky
 */
public class OtherDocumentRecordTypeRelation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -490430948638448565L;

	@SuppressWarnings("unused")
	private OtherDocumentRecordTypeRelationDao typeDao;

	public void setOtherDocumentRecordTypeRelationDao(OtherDocumentRecordTypeRelationDao typeDao) {
		this.typeDao = typeDao;
	}

	/**
	 * Default constructor. Necessary.
	 */
	public OtherDocumentRecordTypeRelation() {
	}

	public OtherDocumentRecordTypeRelation(int recordId, OtherDocumentRecordType type) {
		this.id = new OtherDocumentRecordTypeRelationId();
		this.id.setRecordId(recordId);
		this.id.setType(type);
	}

	private OtherDocumentRecordTypeRelationId id;

	public OtherDocumentRecordTypeRelationId getId() {
		return this.id;
	}

	public void setId(OtherDocumentRecordTypeRelationId id) {
		this.id = id;
	}
}
