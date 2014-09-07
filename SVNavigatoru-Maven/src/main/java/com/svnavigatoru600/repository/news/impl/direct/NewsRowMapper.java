package com.svnavigatoru600.repository.news.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.repository.news.impl.NewsField;

/**
 * For more information, see {@link com.svnavigatoru600.repository.users.impl.direct.UserRowMapper
 * UserRowMapper}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class NewsRowMapper implements RowMapper<News> {

    @Override
    public News mapRow(ResultSet rs, int rowNum) throws SQLException {
        News news = new News();
        news.setId(rs.getInt(NewsField.id.getColumnName()));
        news.setTitle(rs.getString(NewsField.title.getColumnName()));
        news.setText(rs.getString(NewsField.text.getColumnName()));
        news.setCreationTime(new Date(rs.getTimestamp(NewsField.creationTime.getColumnName()).getTime()));
        news.setLastSaveTime(new Date(rs.getTimestamp(NewsField.lastSaveTime.getColumnName()).getTime()));
        return news;
    }
}
