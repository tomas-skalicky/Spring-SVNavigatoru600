package com.svnavigatoru600.repository.records.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

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
    public OtherDocumentRecordTypeRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
        OtherDocumentRecordTypeRelationId id = new OtherDocumentRecordTypeRelationId();
        id.setRecordId(rs.getInt(OtherDocumentRecordTypeRelationField.recordId.getColumnName()));
        id.setType(rs.getString(OtherDocumentRecordTypeRelationField.type.getColumnName()));

        OtherDocumentRecordTypeRelation relation = new OtherDocumentRecordTypeRelation();
        relation.setId(id);
        return relation;
    }
}
