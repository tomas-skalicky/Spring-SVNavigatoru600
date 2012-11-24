package com.svnavigatoru600.repository.users.impl.direct;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityId;
import com.svnavigatoru600.repository.users.AuthorityDao;


public class AuthorityDaoImpl extends SimpleJdbcDaoSupport implements AuthorityDao {

	static final String TABLE_NAME = "authorities";

	public List<Authority> find(String username) {
		String query = String.format("SELECT * FROM %s a WHERE a.%s = ?", AuthorityDaoImpl.TABLE_NAME,
				AuthorityRowMapper.getColumn("username"));
		return this.getSimpleJdbcTemplate().query(query, new AuthorityRowMapper(), username);
	}

	/**
	 * Used during the save of the given <code>authority</code>.
	 */
	private Map<String, Object> getNamedParameters(Authority authority) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		AuthorityId id = authority.getId();
		parameters.put(AuthorityRowMapper.getColumn("username"), id.getUsername());
		parameters.put(AuthorityRowMapper.getColumn("authority"), id.getAuthority());
		return parameters;
	}

	/**
	 * Saves the given <code>authority</code> to the repository.
	 */
	public void save(Authority authority) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource()).withTableName(AuthorityDaoImpl.TABLE_NAME)
				.usingColumns(AuthorityRowMapper.getColumn("username"), AuthorityRowMapper.getColumn("authority"));
		insert.execute(this.getNamedParameters(authority));
	}

	public void save(Collection<GrantedAuthority> authorities) {
		for (GrantedAuthority authority : authorities) {
			this.save((Authority) authority);
		}
	}

	public void delete(String username) {
		String query = String.format("DELETE FROM %s WHERE %s = ?", AuthorityDaoImpl.TABLE_NAME,
				AuthorityRowMapper.getColumn("username"));
		this.getSimpleJdbcTemplate().update(query, username);
	}
}
