package svnavigatoru.repository.users.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import svnavigatoru.domain.users.Authority;
import svnavigatoru.domain.users.AuthorityId;

/**
 * For more information, see {@link UserRowMapper}.
 * 
 * @author Tomas Skalicky
 */
public class AuthorityRowMapper implements RowMapper<Authority> {

	private static final Map<String, String> PROPERTY_COLUMN_MAP;

	static {
		PROPERTY_COLUMN_MAP = new HashMap<String, String>();
		PROPERTY_COLUMN_MAP.put("username", "username");
		PROPERTY_COLUMN_MAP.put("authority", "authority");
	}

	public static String getColumn(String propertyName) {
		return AuthorityRowMapper.PROPERTY_COLUMN_MAP.get(propertyName);
	}

	public Authority mapRow(ResultSet rs, int rowNum) throws SQLException {
		AuthorityId id = new AuthorityId();
		id.setUsername(rs.getString(AuthorityRowMapper.getColumn("username")));
		id.setAuthority(rs.getString(AuthorityRowMapper.getColumn("authority")));

		Authority authority = new Authority();
		authority.setId(id);
		return authority;
	}
}
