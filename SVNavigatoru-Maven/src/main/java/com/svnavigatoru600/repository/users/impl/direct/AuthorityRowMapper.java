package com.svnavigatoru600.repository.users.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityId;
import com.svnavigatoru600.repository.users.impl.AuthorityFieldEnum;

/**
 * For more information, see {@link UserRowMapper}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class AuthorityRowMapper implements RowMapper<Authority> {

    @Override
    public Authority mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorityId id = new AuthorityId();
        id.setUsername(rs.getString(AuthorityFieldEnum.USERNAME.getColumnName()));
        id.setAuthority(rs.getString(AuthorityFieldEnum.AUTHORITY.getColumnName()));

        Authority authority = new Authority();
        authority.setId(id);
        return authority;
    }
}
