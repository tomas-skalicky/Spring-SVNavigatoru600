package com.svnavigatoru600.repository.users.impl.direct;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
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
public class UserDaoImpl extends NamedParameterJdbcDaoSupport implements UserDao {

    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * Database table which provides a persistence of {@link User Users}.
     */
    private static final String TABLE_NAME = PersistedClass.User.getTableName();
    private AuthorityDao authorityDao;

    @Inject
    public void setAuthorityDao(AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }

    /**
     * Populates the <code>authorities</code> property of the given <code>user</code>.
     */
    private void populateAuthorities(User user) {
        List<Authority> authorities = this.authorityDao.findAll(user.getUsername());
        user.setAuthorities(new HashSet<GrantedAuthority>(authorities));
    }

    /**
     * Populates the <code>authorities</code> property of all the given <code>users</code>.
     */
    private void populateAuthorities(List<User> users) {
        for (User user : users) {
            this.populateAuthorities(user);
        }
    }

    @Override
    public User findByUsername(String username) {
        return this.findByUsername(username, false);
    }

    /**
     * @param lazy
     *            If <code>true</code>, {@link Authority authorities} of the desired {@link User} will not be
     *            loaded.
     */
    public User findByUsername(String username, boolean lazy) {
        this.logger.info(String.format("Load an user with the username '%s'", username));

        String usernameColumn = UserField.username.getColumnName();
        String query = String.format("SELECT * FROM %s u WHERE u.%s = :%s", UserDaoImpl.TABLE_NAME,
                usernameColumn, usernameColumn);

        Map<String, String> args = Collections.singletonMap(usernameColumn, username);

        User user = this.getNamedParameterJdbcTemplate().queryForObject(query, args, new UserRowMapper());
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
    public User findByEmail(String email) {
        String lowerCasedEmail = null;
        if (email != null) {
            lowerCasedEmail = email.toLowerCase();
        }

        this.logger.info(String.format("Load an user with the email '%s'", lowerCasedEmail));

        String emailColumn = UserField.email.getColumnName();
        String query = String.format("SELECT * FROM %s u WHERE u.%s = :%s", UserDaoImpl.TABLE_NAME,
                emailColumn, emailColumn);

        Map<String, String> args = Collections.singletonMap(emailColumn, lowerCasedEmail);

        User user = this.getNamedParameterJdbcTemplate().queryForObject(query, args, new UserRowMapper());
        if (user == null) {
            throw new DataRetrievalFailureException(String.format("No user with the email '%s' exists.",
                    lowerCasedEmail));
        }

        this.populateAuthorities(user);
        return user;
    }

    @Override
    public List<User> findAllByAuthority(String authority) {
        this.logger.info(String.format("Load all users with the authority '%s')", authority));

        String authorityColumn = AuthorityField.authority.getColumnName();
        String query = String.format("SELECT u.* FROM %s u INNER JOIN %s a ON a.%s = u.%s WHERE a.%s = :%s",
                UserDaoImpl.TABLE_NAME, PersistedClass.Authority.getTableName(),
                AuthorityField.username.getColumnName(), UserField.username.getColumnName(), authorityColumn,
                authorityColumn);

        Map<String, String> args = Collections.singletonMap(authorityColumn, authority);

        List<User> users = this.getNamedParameterJdbcTemplate().query(query, args, new UserRowMapper());

        this.populateAuthorities(users);
        return users;
    }

    @Override
    public List<User> findAllOrdered(OrderType order, boolean testUsers) {
        this.logger.info(String.format("Load all users ordered %s.", order.name()));

        String isTestUserColumn = UserField.isTestUser.getColumnName();
        String query = String.format("SELECT * FROM %s u WHERE u.%s = :%s ORDER BY u.%s, u.%s %s",
                UserDaoImpl.TABLE_NAME, isTestUserColumn, isTestUserColumn,
                UserField.lastName.getColumnName(), UserField.firstName.getColumnName(),
                order.getDatabaseCode());

        Map<String, Boolean> args = Collections.singletonMap(isTestUserColumn, testUsers);

        List<User> users = this.getNamedParameterJdbcTemplate().query(query, args, new UserRowMapper());

        this.populateAuthorities(users);
        return users;
    }

    /**
     * Maps properties of the given {@link User} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(User user) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(UserField.username.getColumnName(), user.getUsername());
        parameters.put(UserField.password.getColumnName(), user.getPassword());
        parameters.put(UserField.enabled.getColumnName(), user.isEnabled());
        parameters.put(UserField.firstName.getColumnName(), user.getFirstName());
        parameters.put(UserField.lastName.getColumnName(), user.getLastName());
        parameters.put(UserField.phone.getColumnName(), user.getPhone());

        String lowerCasedEmail = null;
        if ((user != null) && (user.getEmail() != null)) {
            lowerCasedEmail = user.getEmail().toLowerCase();
        }
        parameters.put(UserField.email.getColumnName(), lowerCasedEmail);
        return parameters;
    }

    @Override
    public void update(User user) {
        String lowerCasedEmail = null;
        if (user.getEmail() != null) {
            lowerCasedEmail = user.getEmail().toLowerCase();
        }
        this.logger
                .info(String
                        .format("Update an user (username '%s', password '%s', enabled '%b', first_name '%s', last_name '%s', email '%s', phone '%s')",
                                user.getUsername(), user.getPassword(), user.isEnabled(),
                                user.getFirstName(), user.getLastName(), lowerCasedEmail, user.getPhone()));

        // Parameters are those words which begin with ':' in the following query.
        String usernameColumn = UserField.username.getColumnName();
        String passwordColumn = UserField.password.getColumnName();
        String enabledColumn = UserField.enabled.getColumnName();
        String firstNameColumn = UserField.firstName.getColumnName();
        String lastNameColumn = UserField.lastName.getColumnName();
        String emailColumn = UserField.email.getColumnName();
        String phoneColumn = UserField.phone.getColumnName();
        String query = String.format(
                "UPDATE %s SET %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                UserDaoImpl.TABLE_NAME, passwordColumn, passwordColumn, enabledColumn, enabledColumn,
                firstNameColumn, firstNameColumn, lastNameColumn, lastNameColumn, emailColumn, emailColumn,
                phoneColumn, phoneColumn, usernameColumn, usernameColumn);

        this.getNamedParameterJdbcTemplate().update(query, this.getNamedParameters(user));

        // Updates authorities.
        this.authorityDao.delete(user.getUsername());
        this.authorityDao.save(user.getAuthorities());
    }

    @Override
    public void save(User user) {
        String lowerCasedEmail = null;
        if (user.getEmail() != null) {
            lowerCasedEmail = user.getEmail().toLowerCase();
        }

        this.logger
                .info(String
                        .format("Save a new user (username '%s', password '%s', enabled '%b', first_name '%s', last_name '%s', email '%s', phone '%s')",
                                user.getUsername(), user.getPassword(), user.isEnabled(),
                                user.getFirstName(), user.getLastName(), lowerCasedEmail, user.getPhone()));

        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource()).withTableName(
                UserDaoImpl.TABLE_NAME).usingColumns(UserField.username.getColumnName(),
                UserField.password.getColumnName(), UserField.enabled.getColumnName(),
                UserField.firstName.getColumnName(), UserField.lastName.getColumnName(),
                UserField.email.getColumnName(), UserField.phone.getColumnName());

        insert.execute(this.getNamedParameters(user));

        // NOTE: explicit save of the user's authorities.
        this.authorityDao.save(user.getAuthorities());
    }

    @Override
    public void delete(User user) {
        String usernameColumn = UserField.username.getColumnName();
        String query = String.format("DELETE FROM %s WHERE %s = :%s", UserDaoImpl.TABLE_NAME, usernameColumn,
                usernameColumn);

        Map<String, String> args = Collections.singletonMap(usernameColumn, user.getUsername());

        this.getNamedParameterJdbcTemplate().update(query, args);
    }
}
