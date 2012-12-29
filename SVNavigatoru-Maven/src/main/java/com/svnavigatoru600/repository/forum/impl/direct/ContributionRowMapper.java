package com.svnavigatoru600.repository.forum.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.impl.ContributionField;

/**
 * For more information, see {@link com.svnavigatoru600.repository.users.impl.direct.UserRowMapper
 * UserRowMapper}.
 * 
 * @author Tomas Skalicky
 */
public class ContributionRowMapper implements RowMapper<Contribution> {

    @Override
    public Contribution mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Contribution contribution = new Contribution();
        contribution.setId(rs.getInt(ContributionField.id.getColumnName()));
        contribution.setText(rs.getString(ContributionField.text.getColumnName()));
        // NOTE: getTimestamp is used since getDate does not return hours,
        // minutes and so on.
        contribution.setCreationTime(new Date(rs.getTimestamp(ContributionField.creationTime.getColumnName())
                .getTime()));
        contribution.setLastSaveTime(new Date(rs.getTimestamp(ContributionField.lastSaveTime.getColumnName())
                .getTime()));

        final Thread thread = new Thread();
        thread.setId(rs.getInt(ContributionField.threadId.getColumnName()));
        contribution.setThread(thread);

        final User author = new User();
        author.setUsername(rs.getString(ContributionField.authorUsername.getColumnName()));
        contribution.setAuthor(author);
        return contribution;
    }
}
