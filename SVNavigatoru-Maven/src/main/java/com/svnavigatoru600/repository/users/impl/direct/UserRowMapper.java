package com.svnavigatoru600.repository.users.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.impl.UserField;

/**
 * The mapper is helpful when the SimpleJdbcTemplate is used. The class is a very lightweight version of the Hibernate
 * mapping. For more information, see http://www.mkyong.com/spring/spring-simplejdbctemplate-querying-examples/.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUsername(rs.getString(UserField.username.getColumnName()));
        user.setPassword(rs.getString(UserField.password.getColumnName()));
        user.setEnabled(rs.getBoolean(UserField.enabled.getColumnName()));
        user.setFirstName(rs.getString(UserField.firstName.getColumnName()));
        user.setLastName(rs.getString(UserField.lastName.getColumnName()));
        user.setEmail(rs.getString(UserField.email.getColumnName()));
        user.setPhone(rs.getString(UserField.phone.getColumnName()));
        user.setTestUser(rs.getBoolean(UserField.isTestUser.getColumnName()));
        user.setSubscribedToNews(rs.getBoolean(UserField.subscribedToNews.getColumnName()));
        user.setSubscribedToEvents(rs.getBoolean(UserField.subscribedToEvents.getColumnName()));
        user.setSubscribedToForum(rs.getBoolean(UserField.subscribedToForum.getColumnName()));
        user.setSubscribedToOtherDocuments(rs.getBoolean(UserField.subscribedToOtherDocuments.getColumnName()));
        user.setSubscribedToOtherSections(rs.getBoolean(UserField.subscribedToOtherSections.getColumnName()));
        user.setSmtpPort(rs.getInt(UserField.smtpPort.getColumnName()));
        user.setRedirectEmail(rs.getString(UserField.redirectEmail.getColumnName()));
        return user;
    }
}
