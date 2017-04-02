package com.svnavigatoru600.repository.records.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelationId;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordTypeRelationField;

/**
 * For more information, see {@link com.svnavigatoru600.repository.users.impl.direct.UserRowMapper UserRowMapper}.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class OtherDocumentRecordTypeRelationRowMapper implements RowMapper<OtherDocumentRecordTypeRelation> {

    @Override
    public OtherDocumentRecordTypeRelation mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final int recordId = rs.getInt(OtherDocumentRecordTypeRelationField.recordId.getColumnName());
        final String typeString = rs.getString(OtherDocumentRecordTypeRelationField.type.getColumnName());
        final OtherDocumentRecordType type = OtherDocumentRecordType.valueOf(typeString);
        final OtherDocumentRecordTypeRelationId id = new OtherDocumentRecordTypeRelationId(recordId, type);

        final OtherDocumentRecordTypeRelation relation = new OtherDocumentRecordTypeRelation();
        relation.setId(id);
        return relation;
    }
}
