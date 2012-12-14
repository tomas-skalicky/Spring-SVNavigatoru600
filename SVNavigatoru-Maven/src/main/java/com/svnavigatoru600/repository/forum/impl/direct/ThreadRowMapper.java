package com.svnavigatoru600.repository.forum.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.impl.direct.UserRowMapper;

/**
 * For more information, see {@link UserRowMapper}.
 * 
 * @author Tomas Skalicky
 */
public class ThreadRowMapper implements RowMapper<Thread> {

    private static final Map<String, String> PROPERTY_COLUMN_MAP;

    static {
        PROPERTY_COLUMN_MAP = new HashMap<String, String>();
        PROPERTY_COLUMN_MAP.put("id", "id");
        PROPERTY_COLUMN_MAP.put("name", "name");
        PROPERTY_COLUMN_MAP.put("creationTime", "creation_time");
        PROPERTY_COLUMN_MAP.put("authorUsername", "author_username");
    }

    public static String getColumn(String propertyName) {
        return ThreadRowMapper.PROPERTY_COLUMN_MAP.get(propertyName);
    }

    @Override
    public Thread mapRow(ResultSet rs, int rowNum) throws SQLException {
        Thread thread = new Thread();
        thread.setId(rs.getInt(ThreadRowMapper.getColumn("id")));
        thread.setName(rs.getString(ThreadRowMapper.getColumn("name")));
        thread.setCreationTime(new Date(rs.getTimestamp(ThreadRowMapper.getColumn("creationTime")).getTime()));

        User author = new User();
        author.setUsername(rs.getString(ThreadRowMapper.getColumn("authorUsername")));
        thread.setAuthor(author);
        return thread;
    }
}
