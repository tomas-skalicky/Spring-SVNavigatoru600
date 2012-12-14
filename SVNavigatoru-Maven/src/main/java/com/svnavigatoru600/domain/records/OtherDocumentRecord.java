package com.svnavigatoru600.domain.records;

import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;

public class OtherDocumentRecord extends DocumentRecord {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2588978216531929106L;

    @SuppressWarnings("unused")
    private OtherDocumentRecordDao recordDao;

    public void setOtherDocumentRecordDao(OtherDocumentRecordDao recordDao) {
        this.recordDao = recordDao;
    }

    /**
     * The name of this record.
     */
    private String name;
    private String description;
    /**
     * Types of this {@link OtherDocumentRecord}. The record can be of more types at the same time.
     */
    private Set<OtherDocumentRecordTypeRelation> types;
    /**
     * {@link Date} when this {@link OtherDocumentRecord} has been created. The date is set by automatically
     * by the application.
     */
    @DateTimeFormat(style = "LS")
    private Date creationTime;
    /**
     * {@link Date} when this {@link OtherDocumentRecord} has been last saved to the repository. The date is
     * set by automatically by the application.
     */
    @DateTimeFormat(style = "LS")
    private Date lastSaveTime;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<OtherDocumentRecordTypeRelation> getTypes() {
        return this.types;
    }

    public void setTypes(Set<OtherDocumentRecordTypeRelation> types) {
        this.types = types;
    }

    public Date getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastSaveTime() {
        return this.lastSaveTime;
    }

    public void setLastSaveTime(Date lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }
}
