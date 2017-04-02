package com.svnavigatoru600.domain.records;

import java.io.Serializable;

/**
 * Helps to map the <code>types</code> array in the {@link OtherDocumentRecordTypeEnum} class to Hibernate.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class OtherDocumentRecordTypeRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    private OtherDocumentRecordTypeRelationId id;

    public static OtherDocumentRecordTypeRelation createFrom(final int recordId, final OtherDocumentRecordTypeEnum type) {
        final OtherDocumentRecordTypeRelation relation = new OtherDocumentRecordTypeRelation();

        relation.id = new OtherDocumentRecordTypeRelationId(recordId, type);
        return relation;
    }

    public OtherDocumentRecordTypeRelationId getId() {
        return id;
    }

    public void setId(final OtherDocumentRecordTypeRelationId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("OtherDocumentRecordTypeRelation [id=");
        builder.append(id);
        builder.append("]");
        return builder.toString();
    }
}
