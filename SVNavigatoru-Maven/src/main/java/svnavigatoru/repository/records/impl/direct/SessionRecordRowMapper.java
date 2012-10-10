package svnavigatoru.repository.records.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import svnavigatoru.domain.records.SessionRecord;
import svnavigatoru.repository.users.impl.direct.UserRowMapper;

/**
 * For more information, see {@link UserRowMapper}.
 * 
 * @author Tomas Skalicky
 */
public class SessionRecordRowMapper extends DocumentRecordRowMapper implements RowMapper<SessionRecord> {

	private static final Map<String, String> PROPERTY_COLUMN_MAP;

	/**
	 * Static constructor.
	 */
	static {
		PROPERTY_COLUMN_MAP = new HashMap<String, String>();
		PROPERTY_COLUMN_MAP.put("id", "id");
		PROPERTY_COLUMN_MAP.put("type", "type");
		PROPERTY_COLUMN_MAP.put("sessionDate", "session_date");
		PROPERTY_COLUMN_MAP.put("discussedTopics", "discussed_topics");
	}

	/**
	 * Constructor.
	 */
	public SessionRecordRowMapper() {
		super();
	}

	/**
	 * Constructor.
	 */
	public SessionRecordRowMapper(boolean loadFile) {
		super(loadFile);
	}

	public static String getColumn(String propertyName) {
		return SessionRecordRowMapper.PROPERTY_COLUMN_MAP.get(propertyName);
	}

	public SessionRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
		SessionRecord record = new SessionRecord();
		record.setId(rs.getInt(SessionRecordRowMapper.getColumn("id")));
		record.setFileName(rs.getString(DocumentRecordRowMapper.getColumn("fileName")));

		if (this.loadFile) {
			record.setFile(rs.getBlob(DocumentRecordRowMapper.getColumn("file")));
		} else {
			record.setFile(null);
		}

		record.setType(rs.getString(SessionRecordRowMapper.getColumn("type")));
		record.setSessionDate(new Date(rs.getTimestamp(SessionRecordRowMapper.getColumn("sessionDate")).getTime()));
		record.setDiscussedTopics(rs.getString(SessionRecordRowMapper.getColumn("discussedTopics")));
		return record;
	}
}
