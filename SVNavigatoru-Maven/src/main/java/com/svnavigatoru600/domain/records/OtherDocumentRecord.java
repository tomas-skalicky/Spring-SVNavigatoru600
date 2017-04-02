package com.svnavigatoru600.domain.records;

import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class OtherDocumentRecord extends AbstractDocumentRecord {

    private static final long serialVersionUID = 1L;

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
     * {@link Date} when this {@link OtherDocumentRecord} has been created. The date is set by automatically by the
     * application.
     */
    @DateTimeFormat(style = "LS")
    private Date creationTime;
    /**
     * {@link Date} when this {@link OtherDocumentRecord} has been last saved to the repository. The date is set by
     * automatically by the application.
     */
    @DateTimeFormat(style = "LS")
    private Date lastSaveTime;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<OtherDocumentRecordTypeRelation> getTypes() {
        return types;
    }

    public void setTypes(final Set<OtherDocumentRecordTypeRelation> types) {
        this.types = types;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastSaveTime() {
        return lastSaveTime;
    }

    public void setLastSaveTime(final Date lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("OtherDocumentRecord [name=");
        builder.append(name);
        builder.append(", description=");
        builder.append(description);
        builder.append(", types=");
        builder.append(types);
        builder.append(", creationTime=");
        builder.append(creationTime);
        builder.append(", lastSaveTime=");
        builder.append(lastSaveTime);
        builder.append(", toString()=");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }
}
