package com.svnavigatoru600.repository.users.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.impl.UserFieldEnum;

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
        user.setUsername(rs.getString(UserFieldEnum.USERNAME.getColumnName()));
        user.setPassword(rs.getString(UserFieldEnum.PASSWORD.getColumnName()));
        user.setEnabled(rs.getBoolean(UserFieldEnum.ENABLED.getColumnName()));
        user.setFirstName(rs.getString(UserFieldEnum.FIRST_NAME.getColumnName()));
        user.setLastName(rs.getString(UserFieldEnum.LAST_NAME.getColumnName()));
        user.setEmail(rs.getString(UserFieldEnum.EMAIL.getColumnName()));
        user.setPhone(rs.getString(UserFieldEnum.PHONE.getColumnName()));
        user.setTestUser(rs.getBoolean(UserFieldEnum.IS_TEST_USER.getColumnName()));
        user.setSubscribedToNews(rs.getBoolean(UserFieldEnum.SUBSCRIBED_TO_NEWS.getColumnName()));
        user.setSubscribedToEvents(rs.getBoolean(UserFieldEnum.SUBSCRIBED_TO_EVENTS.getColumnName()));
        user.setSubscribedToForum(rs.getBoolean(UserFieldEnum.SUBSCRIBED_TO_FORUM.getColumnName()));
        user.setSubscribedToOtherDocuments(rs.getBoolean(UserFieldEnum.SUBSCRIBED_TO_OTHER_DOCUMENTS.getColumnName()));
        user.setSubscribedToOtherSections(rs.getBoolean(UserFieldEnum.SUBSCRIBED_TO_OTHER_SECTIONS.getColumnName()));
        user.setSmtpPort(rs.getInt(UserFieldEnum.SMTP_PORT.getColumnName()));
        user.setRedirectEmail(rs.getString(UserFieldEnum.REDIRECT_EMAIL.getColumnName()));
        return user;
    }
}
