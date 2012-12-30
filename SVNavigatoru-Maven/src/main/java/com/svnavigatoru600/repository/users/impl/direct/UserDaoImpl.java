package com.svnavigatoru600.repository.users.impl.direct;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.users.AuthorityDao;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.repository.users.impl.AuthorityField;
import com.svnavigatoru600.repository.users.impl.UserField;
import com.svnavigatoru600.service.util.OrderType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class UserDaoImpl extends SimpleJdbcDaoSupport implements UserDao {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(this.getClass());

    private static final String TABLE_NAME = PersistedClass.User.getTableName();
    protected AuthorityDao authorityDao;

    @Autowired
    public void setAuthorityDao(AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }

    /**
     * Populates the <code>authorities</code> property of the given <code>user</code>.
     */
    private void populateAuthorities(final User user) {
        final List<Authority> authorities = this.authorityDao.find(user.getUsername());
        user.setAuthorities(new HashSet<GrantedAuthority>(authorities));
    }

    /**
     * Populates the <code>authorities</code> property of all the given <code>users</code>.
     */
    private void populateAuthorities(final List<User> users) {
        for (User user : users) {
            this.populateAuthorities(user);
        }
    }

    @Override
    public User findByUsername(final String username) {
        return this.findByUsername(username, false);
    }

    /**
     * @param lazy
     *            {@link Authority}s of the desired {@link User} will not be loaded.
     */
    public User findByUsername(final String username, final boolean lazy) {
        this.logger.info(String.format("Load an user with the username '%s'", username));

        final String query = String.format("SELECT * FROM %s u WHERE u.%s = ?", UserDaoImpl.TABLE_NAME,
                UserField.username.getColumnName());
        final User user = this.getSimpleJdbcTemplate().queryForObject(query, new UserRowMapper(), username);
        if (user == null) {
            throw new DataRetrievalFailureException(String.format("No user with the username '%s' exists.",
                    username));
        }

        if (!lazy) {
            this.populateAuthorities(user);
        }
        return user;
    }

    @Override
    public User findByEmail(final String email) {
        String lowerCasedEmail = null;
        if (email != null) {
            lowerCasedEmail = email.toLowerCase();
        }

        this.logger.info(String.format("Load an user with the email '%s'", lowerCasedEmail));

        final String query = String.format("SELECT * FROM %s u WHERE u.%s = ?", UserDaoImpl.TABLE_NAME,
                UserField.email.getColumnName());
        final User user = this.getSimpleJdbcTemplate().queryForObject(query, new UserRowMapper(),
                lowerCasedEmail);
        if (user == null) {
            throw new DataRetrievalFailureException(String.format("No user with the email '%s' exists.",
                    lowerCasedEmail));
        }

        this.populateAuthorities(user);
        return user;
    }

    @Override
    public List<User> findByAuthority(final String authority) {
        this.logger.info(String.format("Load all users with the authority '%s')", authority));

        final String query = String.format(
                "SELECT u.* FROM %s u INNER JOIN %s a ON a.%s = u.%s WHERE a.%s = ?", UserDaoImpl.TABLE_NAME,
                PersistedClass.Authority.getTableName(), AuthorityField.username.getColumnName(),
                UserField.username.getColumnName(), AuthorityField.authority.getColumnName());
        final List<User> users = this.getSimpleJdbcTemplate().query(query, new UserRowMapper(), authority);

        this.populateAuthorities(users);
        return users;
    }

    @Override
    public List<User> loadAllOrdered(final OrderType order, final boolean testUsers) {
        this.logger.info(String.format("Load all users ordered %s.", order.name()));

        final String query = String.format("SELECT * FROM %s u WHERE u.%s = %s ORDER BY u.%s, u.%s %s",
                UserDaoImpl.TABLE_NAME, UserField.isTestUser.getColumnName(), testUsers,
                UserField.lastName.getColumnName(), UserField.firstName.getColumnName(),
                order.getDatabaseCode());
        final List<User> users = this.getSimpleJdbcTemplate().query(query, new UserRowMapper());

        this.populateAuthorities(users);
        return users;
    }

    @Override
    public void update(final User user) {
        String lowerCasedEmail = null;
        if ((user != null) && (user.getEmail() != null)) {
            lowerCasedEmail = user.getEmail().toLowerCase();
        }

        this.logger
                .info(String
                        .format("Update an user (username '%s', password '%s', enabled '%b', first_name '%s', last_name '%s', email '%s', phone '%s')",
                                user.getUsername(), user.getPassword(), user.isEnabled(),
                                user.getFirstName(), user.getLastName(), lowerCasedEmail, user.getPhone()));

        // Parameters are those words which begin with ':' in the following
        // query.
        final String query = String.format(
                "UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
                UserDaoImpl.TABLE_NAME, UserField.password.getColumnName(),
                UserField.enabled.getColumnName(), UserField.firstName.getColumnName(),
                UserField.lastName.getColumnName(), UserField.email.getColumnName(),
                UserField.phone.getColumnName(), UserField.username.getColumnName());
        this.getSimpleJdbcTemplate().update(query, user.getPassword(), user.isEnabled(), user.getFirstName(),
                user.getLastName(), lowerCasedEmail, user.getPhone(), user.getUsername());

        // Updates authorities.
        this.authorityDao.delete(user.getUsername());
        this.authorityDao.save(user.getAuthorities());
    }

    /**
     * Used during the save of the given <code>user</code>.
     */
    private Map<String, Object> getNamedParameters(final User user) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(UserField.username.getColumnName(), user.getUsername());
        parameters.put(UserField.password.getColumnName(), user.getPassword());
        parameters.put(UserField.enabled.getColumnName(), user.isEnabled());
        parameters.put(UserField.firstName.getColumnName(), user.getFirstName());
        parameters.put(UserField.lastName.getColumnName(), user.getLastName());

        String lowerCasedEmail = null;
        if ((user != null) && (user.getEmail() != null)) {
            lowerCasedEmail = user.getEmail().toLowerCase();
        }
        parameters.put(UserField.email.getColumnName(), lowerCasedEmail);

        parameters.put(UserField.phone.getColumnName(), user.getPhone());
        return parameters;
    }

    @Override
    public void save(final User user) {
        String lowerCasedEmail = null;
        if ((user != null) && (user.getEmail() != null)) {
            lowerCasedEmail = user.getEmail().toLowerCase();
        }

        this.logger
                .info(String
                        .format("Save a new user (username '%s', password '%s', enabled '%b', first_name '%s', last_name '%s', email '%s', phone '%s')",
                                user.getUsername(), user.getPassword(), user.isEnabled(),
                                user.getFirstName(), user.getLastName(), lowerCasedEmail, user.getPhone()));

        final SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource()).withTableName(
                UserDaoImpl.TABLE_NAME).usingColumns(UserField.username.getColumnName(),
                UserField.password.getColumnName(), UserField.enabled.getColumnName(),
                UserField.firstName.getColumnName(), UserField.lastName.getColumnName(),
                UserField.email.getColumnName(), UserField.phone.getColumnName());
        insert.execute(this.getNamedParameters(user));

        // NOTE: explicit save of the user's authorities.
        this.authorityDao.save(user.getAuthorities());
    }

    @Override
    public void delete(final User user) {
        final String query = String.format("DELETE FROM %s WHERE %s = ?", UserDaoImpl.TABLE_NAME,
                UserField.username.getColumnName());
        this.getSimpleJdbcTemplate().update(query, user.getUsername());
    }
}
