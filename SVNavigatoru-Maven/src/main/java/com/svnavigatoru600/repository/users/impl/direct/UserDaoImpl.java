package com.svnavigatoru600.repository.users.impl.direct;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.svnavigatoru600.common.annotations.LogMethod;
import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.NotificationTypeEnum;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.AbstractDaoImpl;
import com.svnavigatoru600.repository.QueryUtil;
import com.svnavigatoru600.repository.users.AuthorityDao;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.repository.users.impl.AuthorityFieldEnum;
import com.svnavigatoru600.repository.users.impl.UserFieldEnum;
import com.svnavigatoru600.service.util.OrderTypeEnum;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("userDao")
@Transactional
public class UserDaoImpl extends AbstractDaoImpl implements UserDao {

    /**
     * Database table which provides a persistence of {@link User Users}.
     */
    private static final String TABLE_NAME = "users";

    private final AuthorityDao authorityDao;

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public UserDaoImpl(final AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }

    /**
     * Populates the <code>authorities</code> property of the given <code>user</code>.
     */
    private void populateAuthorities(final User user) {
        final List<Authority> authorities = authorityDao.findByUsername(user.getUsername());
        user.setAuthorities(new HashSet<GrantedAuthority>(authorities));
    }

    /**
     * Populates the <code>authorities</code> property of all the given <code>users</code>.
     */
    private void populateAuthorities(final List<User> users) {
        for (final User user : users) {
            this.populateAuthorities(user);
        }
    }

    @Override
    @LogMethod(logReturnValue = false)
    public User findByUsername(final String username) {
        return this.findByUsername(username, false);
    }

    /**
     * @param lazy
     *            If <code>true</code>, {@link Authority authorities} of the desired {@link User} are not populated.
     */
    @LogMethod(logReturnValue = false)
    public User findByUsername(final String username, final boolean lazy) {

        final String usernameColumn = UserFieldEnum.USERNAME.getColumnName();
        final String query = QueryUtil.selectQuery(UserDaoImpl.TABLE_NAME, usernameColumn, usernameColumn);

        final Map<String, String> args = Collections.singletonMap(usernameColumn, username);

        final User user = getNamedParameterJdbcTemplate().queryForObject(query, args, new UserRowMapper());
        if (user == null) {
            throw new DataRetrievalFailureException(String.format("No user with the username '%s' exists.", username));
        }

        if (!lazy) {
            this.populateAuthorities(user);
        }
        return user;
    }

    @Override
    @LogMethod(logReturnValue = false)
    public User findByEmail(final String email) {

        final String lowerCasedEmail = (email != null) ? email.toLowerCase() : null;

        final String emailColumn = UserFieldEnum.EMAIL.getColumnName();
        final String query = QueryUtil.selectQuery(UserDaoImpl.TABLE_NAME, emailColumn, emailColumn);

        final Map<String, String> args = Collections.singletonMap(emailColumn, lowerCasedEmail);

        final User user = getNamedParameterJdbcTemplate().queryForObject(query, args, new UserRowMapper());
        if (user == null) {
            throw new DataRetrievalFailureException(
                    String.format("No user with the email '%s' exists.", lowerCasedEmail));
        }

        this.populateAuthorities(user);
        return user;
    }

    @Override
    @LogMethod(logReturnValue = false)
    public List<User> findAllByAuthority(final String authority) {

        final String authorityColumn = AuthorityFieldEnum.AUTHORITY.getColumnName();
        final String query = String.format("SELECT u.* FROM %s u INNER JOIN %s a ON a.%s = u.%s WHERE a.%s = :%s",
                UserDaoImpl.TABLE_NAME, AuthorityDaoImpl.TABLE_NAME, AuthorityFieldEnum.USERNAME.getColumnName(),
                UserFieldEnum.USERNAME.getColumnName(), authorityColumn, authorityColumn);

        final Map<String, String> args = Collections.singletonMap(authorityColumn, authority);

        final List<User> users = getNamedParameterJdbcTemplate().query(query, args, new UserRowMapper());

        this.populateAuthorities(users);
        return users;
    }

    @Override
    @LogMethod(logReturnValue = false)
    public List<User> findAllByAuthorityAndSubscription(final String authority,
            final NotificationTypeEnum notificationType) {

        final String authorityColumn = AuthorityFieldEnum.AUTHORITY.getColumnName();
        final String subscriptionColumn = UserFieldEnum.getSubscriptionField(notificationType).getColumnName();
        final String query = String.format(
                "SELECT u.* FROM %s u INNER JOIN %s a ON a.%s = u.%s WHERE a.%s = :%s AND u.%s = :%s",
                UserDaoImpl.TABLE_NAME, AuthorityDaoImpl.TABLE_NAME, AuthorityFieldEnum.USERNAME.getColumnName(),
                UserFieldEnum.USERNAME.getColumnName(), authorityColumn, authorityColumn, subscriptionColumn,
                subscriptionColumn);

        final Map<String, Object> args = new HashMap<>();
        args.put(authorityColumn, authority);
        args.put(subscriptionColumn, Boolean.TRUE);

        return getNamedParameterJdbcTemplate().query(query, args, new UserRowMapper());
    }

    @Override
    @LogMethod(logReturnValue = false)
    public List<User> findAllOrdered(final OrderTypeEnum order, final boolean testUsers) {

        final String isTestUserColumn = UserFieldEnum.IS_TEST_USER.getColumnName();
        final String query = String.format("SELECT * FROM %s u WHERE u.%s = :%s ORDER BY u.%s, u.%s %s",
                UserDaoImpl.TABLE_NAME, isTestUserColumn, isTestUserColumn, UserFieldEnum.LAST_NAME.getColumnName(),
                UserFieldEnum.FIRST_NAME.getColumnName(), order.getDatabaseCode());

        final Map<String, Boolean> args = Collections.singletonMap(isTestUserColumn, testUsers);

        final List<User> users = getNamedParameterJdbcTemplate().query(query, args, new UserRowMapper());

        this.populateAuthorities(users);
        return users;
    }

    /**
     * Maps properties of the given {@link User} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(final User user) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put(UserFieldEnum.USERNAME.getColumnName(), user.getUsername());
        parameters.put(UserFieldEnum.PASSWORD.getColumnName(), user.getPassword());
        parameters.put(UserFieldEnum.ENABLED.getColumnName(), user.isEnabled());
        parameters.put(UserFieldEnum.FIRST_NAME.getColumnName(), user.getFirstName());
        parameters.put(UserFieldEnum.LAST_NAME.getColumnName(), user.getLastName());
        parameters.put(UserFieldEnum.EMAIL.getColumnName(), user.getLowerCasedEmail());
        parameters.put(UserFieldEnum.PHONE.getColumnName(), user.getPhone());
        parameters.put(UserFieldEnum.IS_TEST_USER.getColumnName(), user.isTestUser());
        parameters.put(UserFieldEnum.SUBSCRIBED_TO_NEWS.getColumnName(), user.isSubscribedToNews());
        parameters.put(UserFieldEnum.SUBSCRIBED_TO_EVENTS.getColumnName(), user.isSubscribedToEvents());
        parameters.put(UserFieldEnum.SUBSCRIBED_TO_FORUM.getColumnName(), user.isSubscribedToForum());
        parameters.put(UserFieldEnum.SUBSCRIBED_TO_OTHER_DOCUMENTS.getColumnName(),
                user.isSubscribedToOtherDocuments());
        parameters.put(UserFieldEnum.SUBSCRIBED_TO_OTHER_SECTIONS.getColumnName(), user.isSubscribedToOtherSections());
        parameters.put(UserFieldEnum.SMTP_PORT.getColumnName(), user.getSmtpPort());
        parameters.put(UserFieldEnum.REDIRECT_EMAIL.getColumnName(), user.getRedirectEmail());
        return parameters;
    }

    @Override
    @LogMethod
    public void update(final User user) {
        this.update(user, true);
    }

    @Override
    @LogMethod
    public void update(final User user, final boolean persistAuthorities) {

        // Parameters are those words which begin with ':' in the following query.
        final String usernameColumn = UserFieldEnum.USERNAME.getColumnName();
        final String passwordColumn = UserFieldEnum.PASSWORD.getColumnName();
        final String enabledColumn = UserFieldEnum.ENABLED.getColumnName();
        final String firstNameColumn = UserFieldEnum.FIRST_NAME.getColumnName();
        final String lastNameColumn = UserFieldEnum.LAST_NAME.getColumnName();
        final String emailColumn = UserFieldEnum.EMAIL.getColumnName();
        final String phoneColumn = UserFieldEnum.PHONE.getColumnName();
        final String isTestUserColumn = UserFieldEnum.IS_TEST_USER.getColumnName();
        final String subscribedToNewsColumn = UserFieldEnum.SUBSCRIBED_TO_NEWS.getColumnName();
        final String subscribedToEventsColumn = UserFieldEnum.SUBSCRIBED_TO_EVENTS.getColumnName();
        final String subscribedToForumColumn = UserFieldEnum.SUBSCRIBED_TO_FORUM.getColumnName();
        final String subscribedToOtherDocumentsColumn = UserFieldEnum.SUBSCRIBED_TO_OTHER_DOCUMENTS.getColumnName();
        final String subscribedToOtherSectionsColumn = UserFieldEnum.SUBSCRIBED_TO_OTHER_SECTIONS.getColumnName();
        final String smtpPortColumn = UserFieldEnum.SMTP_PORT.getColumnName();
        final String redirectEmailColumn = UserFieldEnum.REDIRECT_EMAIL.getColumnName();
        final String query = String.format(
                "UPDATE %s SET %s = :%s, %s = :%s, %s = :%s, %s = :%s,"
                        + " %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s"
                        + " WHERE %s = :%s",
                UserDaoImpl.TABLE_NAME, passwordColumn, passwordColumn, enabledColumn, enabledColumn, firstNameColumn,
                firstNameColumn, lastNameColumn, lastNameColumn, emailColumn, emailColumn, phoneColumn, phoneColumn,
                isTestUserColumn, isTestUserColumn, subscribedToNewsColumn, subscribedToNewsColumn,
                subscribedToEventsColumn, subscribedToEventsColumn, subscribedToForumColumn, subscribedToForumColumn,
                subscribedToOtherDocumentsColumn, subscribedToOtherDocumentsColumn, subscribedToOtherSectionsColumn,
                subscribedToOtherSectionsColumn, smtpPortColumn, smtpPortColumn, redirectEmailColumn,
                redirectEmailColumn, usernameColumn, usernameColumn);

        doUpdate(query, getNamedParameters(user));

        if (persistAuthorities) {
            // Updates authorities.
            authorityDao.delete(user.getUsername());
            authorityDao.save(user.getAuthorities());
        }
    }

    @Override
    @LogMethod
    public void save(final User user) {

        final SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource()).withTableName(UserDaoImpl.TABLE_NAME)
                .usingColumns(UserFieldEnum.USERNAME.getColumnName(), UserFieldEnum.PASSWORD.getColumnName(),
                        UserFieldEnum.ENABLED.getColumnName(), UserFieldEnum.FIRST_NAME.getColumnName(),
                        UserFieldEnum.LAST_NAME.getColumnName(), UserFieldEnum.EMAIL.getColumnName(),
                        UserFieldEnum.PHONE.getColumnName(), UserFieldEnum.IS_TEST_USER.getColumnName(),
                        UserFieldEnum.SUBSCRIBED_TO_NEWS.getColumnName(),
                        UserFieldEnum.SUBSCRIBED_TO_EVENTS.getColumnName(),
                        UserFieldEnum.SUBSCRIBED_TO_FORUM.getColumnName(),
                        UserFieldEnum.SUBSCRIBED_TO_OTHER_DOCUMENTS.getColumnName(),
                        UserFieldEnum.SUBSCRIBED_TO_OTHER_SECTIONS.getColumnName(),
                        UserFieldEnum.REDIRECT_EMAIL.getColumnName());

        if (user.getSmtpPort() != null) {
            insert.usingColumns(UserFieldEnum.SMTP_PORT.getColumnName());
        }

        insert.execute(getNamedParameters(user));

        // NOTE: explicit save of the user's authorities.
        authorityDao.save(user.getAuthorities());
    }

    @Override
    public void delete(final User user) {
        this.delete(user.getUsername());
    }

    @Override
    public void delete(final String username) {
        final String usernameColumn = UserFieldEnum.USERNAME.getColumnName();
        final String query = QueryUtil.deleteQuery(UserDaoImpl.TABLE_NAME, usernameColumn, usernameColumn);

        final Map<String, String> args = Collections.singletonMap(usernameColumn, username);

        doUpdate(query, args);
    }
}
