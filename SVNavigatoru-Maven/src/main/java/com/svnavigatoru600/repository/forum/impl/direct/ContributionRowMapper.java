package com.svnavigatoru600.repository.forum.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.domain.forum.ForumThread;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.impl.ContributionFieldEnum;

/**
 * For more information, see {@link com.svnavigatoru600.repository.users.impl.direct.UserRowMapper UserRowMapper}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class ContributionRowMapper implements RowMapper<ForumContribution> {

    @Override
    public ForumContribution mapRow(ResultSet rs, int rowNum) throws SQLException {
        ForumContribution contribution = new ForumContribution();
        contribution.setId(rs.getInt(ContributionFieldEnum.ID.getColumnName()));
        contribution.setText(rs.getString(ContributionFieldEnum.TEXT.getColumnName()));
        // NOTE: getTimestamp is used since getDate does not return hours,
        // minutes and so on.
        contribution
                .setCreationTime(new Date(rs.getTimestamp(ContributionFieldEnum.CREATION_TIME.getColumnName()).getTime()));
        contribution
                .setLastSaveTime(new Date(rs.getTimestamp(ContributionFieldEnum.LAST_SAVE_TIME.getColumnName()).getTime()));

        ForumThread thread = new ForumThread();
        thread.setId(rs.getInt(ContributionFieldEnum.THREAD_ID.getColumnName()));
        contribution.setThread(thread);

        User author = new User();
        author.setUsername(rs.getString(ContributionFieldEnum.AUTHOR_USERNAME.getColumnName()));
        contribution.setAuthor(author);
        return contribution;
    }
}
