package com.svnavigatoru600.repository.users.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.users.User;

/**
 * The mapper is helpful when the SimpleJdbcTemplate is used. The class is a very lightweight version of the
 * Hibernate mapping. For more information, see
 * http://www.mkyong.com/spring/spring-simplejdbctemplate-querying-examples/.
 * 
 * @author Tomas Skalicky
 */
public class UserRowMapper implements RowMapper<User> {

    private static final Map<String, String> PROPERTY_COLUMN_MAP;

    static {
        PROPERTY_COLUMN_MAP = new HashMap<String, String>();
        PROPERTY_COLUMN_MAP.put("username", "username");
        PROPERTY_COLUMN_MAP.put("password", "password");
        PROPERTY_COLUMN_MAP.put("enabled", "enabled");
        PROPERTY_COLUMN_MAP.put("firstName", "first_name");
        PROPERTY_COLUMN_MAP.put("lastName", "last_name");
        PROPERTY_COLUMN_MAP.put("email", "email");
        PROPERTY_COLUMN_MAP.put("phone", "phone");
    }

    public static String getColumn(String propertyName) {
        return UserRowMapper.PROPERTY_COLUMN_MAP.get(propertyName);
    }

    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUsername(rs.getString(UserRowMapper.getColumn("username")));
        user.setPassword(rs.getString(UserRowMapper.getColumn("password")));
        user.setEnabled(rs.getBoolean(UserRowMapper.getColumn("enabled")));
        user.setFirstName(rs.getString(UserRowMapper.getColumn("firstName")));
        user.setLastName(rs.getString(UserRowMapper.getColumn("lastName")));
        user.setEmail(rs.getString(UserRowMapper.getColumn("email")));
        user.setPhone(rs.getString(UserRowMapper.getColumn("phone")));
        return user;
    }
}
