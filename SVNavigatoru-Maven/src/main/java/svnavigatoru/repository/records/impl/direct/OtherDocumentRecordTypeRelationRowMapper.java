package svnavigatoru.repository.records.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import svnavigatoru.domain.records.OtherDocumentRecordTypeRelation;
import svnavigatoru.domain.records.OtherDocumentRecordTypeRelationId;
import svnavigatoru.repository.users.impl.direct.UserRowMapper;

/**
 * For more information, see {@link UserRowMapper}.
 * 
 * @author Tomas Skalicky
 */
public class OtherDocumentRecordTypeRelationRowMapper implements RowMapper<OtherDocumentRecordTypeRelation> {

	private static final Map<String, String> PROPERTY_COLUMN_MAP;

	static {
		PROPERTY_COLUMN_MAP = new HashMap<String, String>();
		PROPERTY_COLUMN_MAP.put("recordId", "record_id");
		PROPERTY_COLUMN_MAP.put("type", "type");
	}

	public static String getColumn(String propertyName) {
		return OtherDocumentRecordTypeRelationRowMapper.PROPERTY_COLUMN_MAP.get(propertyName);
	}

	public OtherDocumentRecordTypeRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
		OtherDocumentRecordTypeRelationId id = new OtherDocumentRecordTypeRelationId();
		id.setRecordId(rs.getInt(OtherDocumentRecordTypeRelationRowMapper.getColumn("recordId")));
		id.setType(rs.getString(OtherDocumentRecordTypeRelationRowMapper.getColumn("type")));

		OtherDocumentRecordTypeRelation relation = new OtherDocumentRecordTypeRelation();
		relation.setId(id);
		return relation;
	}
}
