package com.svnavigatoru600.repository.news.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.repository.users.impl.direct.UserRowMapper;


/**
 * For more information, see {@link UserRowMapper}.
 * 
 * @author Tomas Skalicky
 */
public class NewsRowMapper implements RowMapper<News> {

	private static final Map<String, String> PROPERTY_COLUMN_MAP;

	static {
		PROPERTY_COLUMN_MAP = new HashMap<String, String>();
		PROPERTY_COLUMN_MAP.put("id", "id");
		PROPERTY_COLUMN_MAP.put("title", "title");
		PROPERTY_COLUMN_MAP.put("text", "text");
		PROPERTY_COLUMN_MAP.put("creationTime", "creation_time");
		PROPERTY_COLUMN_MAP.put("lastSaveTime", "last_save_time");
	}

	public static String getColumn(String propertyName) {
		return NewsRowMapper.PROPERTY_COLUMN_MAP.get(propertyName);
	}

	public News mapRow(ResultSet rs, int rowNum) throws SQLException {
		News news = new News();
		news.setId(rs.getInt(NewsRowMapper.getColumn("id")));
		news.setTitle(rs.getString(NewsRowMapper.getColumn("title")));
		news.setText(rs.getString(NewsRowMapper.getColumn("text")));
		news.setCreationTime(new Date(rs.getTimestamp(NewsRowMapper.getColumn("creationTime")).getTime()));
		news.setLastSaveTime(new Date(rs.getTimestamp(NewsRowMapper.getColumn("lastSaveTime")).getTime()));
		return news;
	}
}
