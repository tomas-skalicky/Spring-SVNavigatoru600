package com.svnavigatoru600.repository.forum.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.forum.ForumThread;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.impl.ThreadFieldEnum;

/**
 * For more information, see {@link com.svnavigatoru600.repository.users.impl.direct.UserRowMapper UserRowMapper}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class ThreadRowMapper implements RowMapper<ForumThread> {

    @Override
    public ForumThread mapRow(ResultSet rs, int rowNum) throws SQLException {
        ForumThread thread = new ForumThread();
        thread.setId(rs.getInt(ThreadFieldEnum.ID.getColumnName()));
        thread.setName(rs.getString(ThreadFieldEnum.NAME.getColumnName()));
        thread.setCreationTime(new Date(rs.getTimestamp(ThreadFieldEnum.CREATION_TIME.getColumnName()).getTime()));

        User author = new User();
        author.setUsername(rs.getString(ThreadFieldEnum.AUTHOR_USERNAME.getColumnName()));
        thread.setAuthor(author);
        return thread;
    }
}
