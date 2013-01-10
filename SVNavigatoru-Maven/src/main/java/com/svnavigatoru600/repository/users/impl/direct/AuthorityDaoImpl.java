package com.svnavigatoru600.repository.users.impl.direct;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityId;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.users.AuthorityDao;
import com.svnavigatoru600.repository.users.impl.AuthorityField;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class AuthorityDaoImpl extends NamedParameterJdbcDaoSupport implements AuthorityDao {

    /**
     * Database table which provides a persistence of {@link Authority Authorities}.
     */
    private static final String TABLE_NAME = PersistedClass.Authority.getTableName();

    @Override
    public List<Authority> findAll(String username) {
        String usernameColumn = AuthorityField.username.getColumnName();
        String query = String.format("SELECT * FROM %s a WHERE a.%s = :%s", AuthorityDaoImpl.TABLE_NAME,
                usernameColumn, usernameColumn);

        Map<String, String> args = Collections.singletonMap(usernameColumn, username);

        return this.getNamedParameterJdbcTemplate().query(query, args, new AuthorityRowMapper());
    }

    /**
     * Maps properties of the given {@link Authority} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(Authority authority) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        AuthorityId id = authority.getId();
        parameters.put(AuthorityField.username.getColumnName(), id.getUsername());
        parameters.put(AuthorityField.authority.getColumnName(), id.getAuthority());
        return parameters;
    }

    @Override
    public void save(Authority authority) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource()).withTableName(
                AuthorityDaoImpl.TABLE_NAME).usingColumns(AuthorityField.username.getColumnName(),
                AuthorityField.authority.getColumnName());

        insert.execute(this.getNamedParameters(authority));
    }

    @Override
    public void save(Collection<GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            this.save((Authority) authority);
        }
    }

    @Override
    public void delete(String username) {
        String usernameColumn = AuthorityField.username.getColumnName();
        String query = String.format("DELETE FROM %s WHERE %s = :%s", AuthorityDaoImpl.TABLE_NAME,
                usernameColumn, usernameColumn);

        Map<String, String> args = Collections.singletonMap(usernameColumn, username);

        this.getNamedParameterJdbcTemplate().update(query, args);
    }

    @Override
    public void delete(String username, AuthorityType authorityType) {
        String usernameColumn = AuthorityField.username.getColumnName();
        String typeColumn = AuthorityField.authority.getColumnName();
        String query = String.format("DELETE FROM %s WHERE %s = :%s AND %s = :%s",
                AuthorityDaoImpl.TABLE_NAME, usernameColumn, usernameColumn, typeColumn, typeColumn);

        Map<String, String> args = new HashMap<String, String>();
        args.put(usernameColumn, username);
        args.put(typeColumn, authorityType.name());

        this.getNamedParameterJdbcTemplate().update(query, args);
    }
}
