package com.svnavigatoru600.domain.records;

import java.io.Serializable;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class OtherDocumentRecordTypeRelationId implements Serializable {

    private static final long serialVersionUID = 1L;

    private int recordId;
    private OtherDocumentRecordTypeEnum type;

    public OtherDocumentRecordTypeRelationId(final int recordId, final OtherDocumentRecordTypeEnum type) {
        this.recordId = recordId;
        this.type = type;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(final int recordId) {
        this.recordId = recordId;
    }

    /**
     * Different - not "getType" - name of the getter method is necessary. Otherwise, the methods' signatures would be
     * identical.
     */
    public OtherDocumentRecordTypeEnum getTypedType() {
        return type;
    }

    /**
     * This getter is necessary because of Hibernate.
     */
    public String getType() {
        return type.name();
    }

    public void setType(final OtherDocumentRecordTypeEnum type) {
        this.type = type;
    }

    /**
     * This setter is necessary because of Hibernate.
     */
    public void setType(final String type) {
        this.type = OtherDocumentRecordTypeEnum.valueOf(type);
    }

    /**
     * Generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + recordId;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /**
     * Generated
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        final OtherDocumentRecordTypeRelationId other = (OtherDocumentRecordTypeRelationId) obj;
        if (recordId != other.recordId) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("OtherDocumentRecordTypeRelationId [recordId=");
        builder.append(recordId);
        builder.append(", type=");
        builder.append(type);
        builder.append("]");
        return builder.toString();
    }
}
