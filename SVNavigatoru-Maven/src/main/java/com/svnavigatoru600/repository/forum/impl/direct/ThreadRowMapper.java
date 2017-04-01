package com.svnavigatoru600.repository.forum.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.impl.ThreadField;

/**
 * For more information, see {@link com.svnavigatoru600.repository.users.impl.direct.UserRowMapper UserRowMapper}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class ThreadRowMapper implements RowMapper<Thread> {

    @Override
    public Thread mapRow(ResultSet rs, int rowNum) throws SQLException {
        Thread thread = new Thread();
        thread.setId(rs.getInt(ThreadField.id.getColumnName()));
        thread.setName(rs.getString(ThreadField.name.getColumnName()));
        thread.setCreationTime(new Date(rs.getTimestamp(ThreadField.creationTime.getColumnName()).getTime()));

        User author = new User();
        author.setUsername(rs.getString(ThreadField.authorUsername.getColumnName()));
        thread.setAuthor(author);
        return thread;
    }
}
