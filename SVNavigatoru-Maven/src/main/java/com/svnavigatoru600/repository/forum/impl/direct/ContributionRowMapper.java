package com.svnavigatoru600.repository.forum.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.impl.direct.UserRowMapper;


/**
 * For more information, see {@link UserRowMapper}.
 * 
 * @author Tomas Skalicky
 */
public class ContributionRowMapper implements RowMapper<Contribution> {

	private static final Map<String, String> PROPERTY_COLUMN_MAP;

	static {
		PROPERTY_COLUMN_MAP = new HashMap<String, String>();
		PROPERTY_COLUMN_MAP.put("id", "id");
		PROPERTY_COLUMN_MAP.put("threadId", "thread_id");
		PROPERTY_COLUMN_MAP.put("text", "text");
		PROPERTY_COLUMN_MAP.put("creationTime", "creation_time");
		PROPERTY_COLUMN_MAP.put("lastSaveTime", "last_save_time");
		PROPERTY_COLUMN_MAP.put("authorUsername", "author_username");
	}

	public static String getColumn(String propertyName) {
		return ContributionRowMapper.PROPERTY_COLUMN_MAP.get(propertyName);
	}

	public Contribution mapRow(ResultSet rs, int rowNum) throws SQLException {
		Contribution contribution = new Contribution();
		contribution.setId(rs.getInt(ContributionRowMapper.getColumn("id")));
		contribution.setText(rs.getString(ContributionRowMapper.getColumn("text")));
		// NOTE: getTimestamp is used since getDate does not return hours,
		// minutes and so on.
		contribution.setCreationTime(new Date(rs.getTimestamp(ContributionRowMapper.getColumn("creationTime"))
				.getTime()));
		contribution.setLastSaveTime(new Date(rs.getTimestamp(ContributionRowMapper.getColumn("lastSaveTime"))
				.getTime()));

		Thread thread = new Thread();
		thread.setId(rs.getInt(ContributionRowMapper.getColumn("threadId")));
		contribution.setThread(thread);

		User author = new User();
		author.setUsername(rs.getString(ContributionRowMapper.getColumn("authorUsername")));
		contribution.setAuthor(author);
		return contribution;
	}
}
