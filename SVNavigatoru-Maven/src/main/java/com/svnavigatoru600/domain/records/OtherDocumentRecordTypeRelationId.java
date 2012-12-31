package com.svnavigatoru600.domain.records;

import java.io.Serializable;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class OtherDocumentRecordTypeRelationId implements Serializable {

    private static final long serialVersionUID = -1879445826891635320L;

    private int recordId;
    private OtherDocumentRecordType type;

    public int getRecordId() {
        return this.recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    /**
     * Different - not "getType" - name of the getter method is necessary. Otherwise, the methods' signatures
     * would be identical.
     */
    public OtherDocumentRecordType getTypedType() {
        return this.type;
    }

    /**
     * This getter is necessary because of Hibernate.
     */
    public String getType() {
        return this.type.name();
    }

    public void setType(OtherDocumentRecordType type) {
        this.type = type;
    }

    /**
     * This setter is necessary because of Hibernate.
     */
    public void setType(String type) {
        this.type = OtherDocumentRecordType.valueOf(type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.recordId;
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        OtherDocumentRecordTypeRelationId other = (OtherDocumentRecordTypeRelationId) obj;
        if (this.recordId != other.recordId) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }
}
