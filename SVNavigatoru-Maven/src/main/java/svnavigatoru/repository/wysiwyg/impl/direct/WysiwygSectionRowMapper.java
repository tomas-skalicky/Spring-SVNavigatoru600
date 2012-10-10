package svnavigatoru.repository.wysiwyg.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import svnavigatoru.domain.WysiwygSection;
import svnavigatoru.repository.users.impl.direct.UserRowMapper;

/**
 * For more information, see {@link UserRowMapper}.
 * 
 * @author Tomas Skalicky
 */
public class WysiwygSectionRowMapper implements RowMapper<WysiwygSection> {

	private static final Map<String, String> PROPERTY_COLUMN_MAP;

	static {
		PROPERTY_COLUMN_MAP = new HashMap<String, String>();
		PROPERTY_COLUMN_MAP.put("name", "name");
		PROPERTY_COLUMN_MAP.put("lastSaveTime", "last_save_time");
		PROPERTY_COLUMN_MAP.put("sourceCode", "source_code");
	}

	public static String getColumn(String propertyName) {
		return WysiwygSectionRowMapper.PROPERTY_COLUMN_MAP.get(propertyName);
	}

	public WysiwygSection mapRow(ResultSet rs, int rowNum) throws SQLException {
		WysiwygSection section = new WysiwygSection();
		section.setName(rs.getString(WysiwygSectionRowMapper.getColumn("name")));
		section.setLastSaveTime(new Date(rs.getTimestamp(WysiwygSectionRowMapper.getColumn("lastSaveTime")).getTime()));
		section.setSourceCode(rs.getString(WysiwygSectionRowMapper.getColumn("sourceCode")));
		return section;
	}
}
