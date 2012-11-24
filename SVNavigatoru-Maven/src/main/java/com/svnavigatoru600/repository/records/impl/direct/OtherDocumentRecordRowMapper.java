package com.svnavigatoru600.repository.records.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.repository.users.impl.direct.UserRowMapper;


/**
 * For more information, see {@link UserRowMapper}.
 * 
 * @author Tomas Skalicky
 */
public class OtherDocumentRecordRowMapper extends DocumentRecordRowMapper implements RowMapper<OtherDocumentRecord> {

	private static final Map<String, String> PROPERTY_COLUMN_MAP;

	/**
	 * Static constructor.
	 */
	static {
		PROPERTY_COLUMN_MAP = new HashMap<String, String>();
		PROPERTY_COLUMN_MAP.put("id", "id");
		PROPERTY_COLUMN_MAP.put("name", "name");
		PROPERTY_COLUMN_MAP.put("description", "description");
		PROPERTY_COLUMN_MAP.put("creationTime", "creation_time");
		PROPERTY_COLUMN_MAP.put("lastSaveTime", "last_save_time");
	}

	/**
	 * Constructor.
	 */
	public OtherDocumentRecordRowMapper() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param loadFile
	 *            Indicator whether the Blob file will be set up.
	 */
	public OtherDocumentRecordRowMapper(boolean loadFile) {
		super(loadFile);
	}

	public static String getColumn(String propertyName) {
		return OtherDocumentRecordRowMapper.PROPERTY_COLUMN_MAP.get(propertyName);
	}

	public OtherDocumentRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
		OtherDocumentRecord record = new OtherDocumentRecord();
		record.setId(rs.getInt(OtherDocumentRecordRowMapper.getColumn("id")));
		record.setFileName(rs.getString(DocumentRecordRowMapper.getColumn("fileName")));

		if (this.loadFile) {
			record.setFile(rs.getBlob(DocumentRecordRowMapper.getColumn("file")));
		} else {
			record.setFile(null);
		}

		record.setName(rs.getString(OtherDocumentRecordRowMapper.getColumn("name")));
		record.setDescription(rs.getString(OtherDocumentRecordRowMapper.getColumn("description")));
		record.setCreationTime(new Date(rs.getTimestamp(OtherDocumentRecordRowMapper.getColumn("creationTime"))
				.getTime()));
		record.setLastSaveTime(new Date(rs.getTimestamp(OtherDocumentRecordRowMapper.getColumn("lastSaveTime"))
				.getTime()));
		return record;
	}
}
