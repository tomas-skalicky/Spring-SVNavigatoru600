package com.svnavigatoru600.repository.users.impl.direct;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityId;
import com.svnavigatoru600.domain.users.AuthorityTypeEnum;
import com.svnavigatoru600.repository.QueryUtil;
import com.svnavigatoru600.repository.users.AuthorityDao;
import com.svnavigatoru600.repository.users.impl.AuthorityFieldEnum;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("authorityDao")
@Transactional
public class AuthorityDaoImpl extends NamedParameterJdbcDaoSupport implements AuthorityDao {

    /**
     * Database table which provides a persistence of {@link Authority Authorities}.
     */
    static final String TABLE_NAME = "authorities";

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public AuthorityDaoImpl(final DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public List<Authority> findByUsername(final String username) {
        final String usernameColumn = AuthorityFieldEnum.USERNAME.getColumnName();
        final String query = QueryUtil.selectQuery(AuthorityDaoImpl.TABLE_NAME, usernameColumn, usernameColumn);

        final Map<String, String> args = Collections.singletonMap(usernameColumn, username);

        return getNamedParameterJdbcTemplate().query(query, args, new AuthorityRowMapper());
    }

    /**
     * Maps properties of the given {@link Authority} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(final Authority authority) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        final AuthorityId id = authority.getId();
        parameters.put(AuthorityFieldEnum.USERNAME.getColumnName(), id.getUsername());
        parameters.put(AuthorityFieldEnum.AUTHORITY.getColumnName(), id.getAuthority());
        return parameters;
    }

    @Override
    public void save(final Authority authority) {
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource()).withTableName(AuthorityDaoImpl.TABLE_NAME)
                .usingColumns(AuthorityFieldEnum.USERNAME.getColumnName(), AuthorityFieldEnum.AUTHORITY.getColumnName());

        insert.execute(getNamedParameters(authority));
    }

    @Override
    public void save(final Collection<GrantedAuthority> authorities) {
        for (final GrantedAuthority authority : authorities) {
            this.save((Authority) authority);
        }
    }

    @Override
    public void delete(final String username) {
        final String usernameColumn = AuthorityFieldEnum.USERNAME.getColumnName();
        final String query = QueryUtil.deleteQuery(AuthorityDaoImpl.TABLE_NAME, usernameColumn, usernameColumn);

        final Map<String, String> args = Collections.singletonMap(usernameColumn, username);

        getNamedParameterJdbcTemplate().update(query, args);
    }

    @Override
    public void delete(final String username, final AuthorityTypeEnum authorityType) {
        final String usernameColumn = AuthorityFieldEnum.USERNAME.getColumnName();
        final String typeColumn = AuthorityFieldEnum.AUTHORITY.getColumnName();
        final String query = String.format("DELETE FROM %s WHERE %s = :%s AND %s = :%s", AuthorityDaoImpl.TABLE_NAME,
                usernameColumn, usernameColumn, typeColumn, typeColumn);

        final Map<String, String> args = new HashMap<String, String>();
        args.put(usernameColumn, username);
        args.put(typeColumn, authorityType.name());

        getNamedParameterJdbcTemplate().update(query, args);
    }
}
