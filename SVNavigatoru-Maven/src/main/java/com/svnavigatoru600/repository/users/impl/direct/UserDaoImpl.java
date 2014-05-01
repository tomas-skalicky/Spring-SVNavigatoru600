package com.svnavigatoru600.repository.users.impl.direct;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.NotificationType;
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
@Repository("userDao")
public class UserDaoImpl extends NamedParameterJdbcDaoSupport implements UserDao {

    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * Database table which provides a persistence of {@link User Users}.
     */
    private static final String TABLE_NAME = PersistedClass.User.getTableName();

    @Inject
    private AuthorityDao authorityDao;

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public UserDaoImpl(DataSource dataSource) {
        super();
        setDataSource(dataSource);
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
     *            If <code>true</code>, {@link Authority authorities} of the desired {@link User} are not
     *            populated.
     */
    public User findByUsername(String username, boolean lazy) {
        this.logger.info(String.format("Load an user with the username '%s'", username));

        String usernameColumn = UserField.username.getColumnName();
        String query = String.format("SELECT * FROM %s u WHERE u.%s = :%s", UserDaoImpl.TABLE_NAME,
                usernameColumn, usernameColumn);

        Map<String, String> args = Collections.singletonMap(usernameColumn, username);

        User user = getNamedParameterJdbcTemplate().queryForObject(query, args, new UserRowMapper());
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

        User user = getNamedParameterJdbcTemplate().queryForObject(query, args, new UserRowMapper());
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

        List<User> users = getNamedParameterJdbcTemplate().query(query, args, new UserRowMapper());

        this.populateAuthorities(users);
        return users;
    }

    @Override
    public List<User> findAllByAuthorityAndSubscription(String authority, NotificationType notificationType) {
        this.logger.info(String.format("Load all users with the authority '%s' and subscription '%s')",
                authority, notificationType.name()));

        String authorityColumn = AuthorityField.authority.getColumnName();
        String subscriptionColumn = UserField.getSubscriptionField(notificationType).getColumnName();
        String query = String.format(
                "SELECT u.* FROM %s u INNER JOIN %s a ON a.%s = u.%s WHERE a.%s = :%s AND u.%s = :%s",
                UserDaoImpl.TABLE_NAME, PersistedClass.Authority.getTableName(),
                AuthorityField.username.getColumnName(), UserField.username.getColumnName(), authorityColumn,
                authorityColumn, subscriptionColumn, subscriptionColumn);

        Map<String, Object> args = new HashMap<String, Object>();
        args.put(authorityColumn, authority);
        args.put(subscriptionColumn, Boolean.TRUE);

        return getNamedParameterJdbcTemplate().query(query, args, new UserRowMapper());
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

        List<User> users = getNamedParameterJdbcTemplate().query(query, args, new UserRowMapper());

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
        parameters.put(UserField.email.getColumnName(), user.getLowerCasedEmail());
        parameters.put(UserField.phone.getColumnName(), user.getPhone());
        parameters.put(UserField.isTestUser.getColumnName(), user.isTestUser());
        parameters.put(UserField.subscribedToNews.getColumnName(), user.isSubscribedToNews());
        parameters.put(UserField.subscribedToEvents.getColumnName(), user.isSubscribedToEvents());
        parameters.put(UserField.subscribedToForum.getColumnName(), user.isSubscribedToForum());
        parameters.put(UserField.subscribedToOtherDocuments.getColumnName(),
                user.isSubscribedToOtherDocuments());
        parameters.put(UserField.subscribedToOtherSections.getColumnName(),
                user.isSubscribedToOtherSections());
        parameters.put(UserField.smtpPort.getColumnName(), user.getSmtpPort());
        return parameters;
    }

    @Override
    public void update(User user) {
        this.update(user, true);
    }

    @Override
    public void update(User user, boolean persistAuthorities) {
        this.logger.info("Update an user " + user);

        // Parameters are those words which begin with ':' in the following query.
        String usernameColumn = UserField.username.getColumnName();
        String passwordColumn = UserField.password.getColumnName();
        String enabledColumn = UserField.enabled.getColumnName();
        String firstNameColumn = UserField.firstName.getColumnName();
        String lastNameColumn = UserField.lastName.getColumnName();
        String emailColumn = UserField.email.getColumnName();
        String phoneColumn = UserField.phone.getColumnName();
        String isTestUserColumn = UserField.isTestUser.getColumnName();
        String subscribedToNewsColumn = UserField.subscribedToNews.getColumnName();
        String subscribedToEventsColumn = UserField.subscribedToEvents.getColumnName();
        String subscribedToForumColumn = UserField.subscribedToForum.getColumnName();
        String subscribedToOtherDocumentsColumn = UserField.subscribedToOtherDocuments.getColumnName();
        String subscribedToOtherSectionsColumn = UserField.subscribedToOtherSections.getColumnName();
        String smtpPortColumn = UserField.smtpPort.getColumnName();
        String query = String.format("UPDATE %s SET %s = :%s, %s = :%s, %s = :%s, %s = :%s,"
                + " %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s"
                + " WHERE %s = :%s", UserDaoImpl.TABLE_NAME, passwordColumn, passwordColumn, enabledColumn,
                enabledColumn, firstNameColumn, firstNameColumn, lastNameColumn, lastNameColumn, emailColumn,
                emailColumn, phoneColumn, phoneColumn, isTestUserColumn, isTestUserColumn,
                subscribedToNewsColumn, subscribedToNewsColumn, subscribedToEventsColumn,
                subscribedToEventsColumn, subscribedToForumColumn, subscribedToForumColumn,
                subscribedToOtherDocumentsColumn, subscribedToOtherDocumentsColumn,
                subscribedToOtherSectionsColumn, subscribedToOtherSectionsColumn, smtpPortColumn,
                smtpPortColumn, usernameColumn, usernameColumn);

        getNamedParameterJdbcTemplate().update(query, getNamedParameters(user));

        if (persistAuthorities) {
            // Updates authorities.
            this.authorityDao.delete(user.getUsername());
            this.authorityDao.save(user.getAuthorities());
        }
    }

    @Override
    public void save(User user) {
        this.logger.info("Save a new user " + user);

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource()).withTableName(UserDaoImpl.TABLE_NAME)
                .usingColumns(UserField.username.getColumnName(), UserField.password.getColumnName(),
                        UserField.enabled.getColumnName(), UserField.firstName.getColumnName(),
                        UserField.lastName.getColumnName(), UserField.email.getColumnName(),
                        UserField.phone.getColumnName(), UserField.isTestUser.getColumnName(),
                        UserField.subscribedToNews.getColumnName(),
                        UserField.subscribedToEvents.getColumnName(),
                        UserField.subscribedToForum.getColumnName(),
                        UserField.subscribedToOtherDocuments.getColumnName(),
                        UserField.subscribedToOtherSections.getColumnName(),
                        UserField.smtpPort.getColumnName());

        insert.execute(getNamedParameters(user));

        // NOTE: explicit save of the user's authorities.
        this.authorityDao.save(user.getAuthorities());
    }

    @Override
    public void delete(User user) {
        this.delete(user.getUsername());
    }

    @Override
    public void delete(String username) {
        String usernameColumn = UserField.username.getColumnName();
        String query = String.format("DELETE FROM %s WHERE %s = :%s", UserDaoImpl.TABLE_NAME, usernameColumn,
                usernameColumn);

        Map<String, String> args = Collections.singletonMap(usernameColumn, username);

        getNamedParameterJdbcTemplate().update(query, args);
    }
}
