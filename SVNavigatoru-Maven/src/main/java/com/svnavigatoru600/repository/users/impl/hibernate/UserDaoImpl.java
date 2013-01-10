package com.svnavigatoru600.repository.users.impl.hibernate;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

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
public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

    private final Log logger = LogFactory.getLog(this.getClass());

    private AuthorityDao authorityDao;

    @Inject
    public void setAuthorityDao(AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }

    @Override
    public User findByUsername(String username) {
        this.logger.info(String.format("Load an user with the username '%s'", username));
        return this.getHibernateTemplate().load(User.class, username);
    }

    @Override
    public User findByEmail(String email) {
        String lowerCasedEmail = null;
        if (email != null) {
            lowerCasedEmail = email.toLowerCase();
        }

        this.logger.info(String.format("Load an user with the email '%s'", lowerCasedEmail));

        // Stands for 'where' clause.
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        criteria.add(Restrictions.eq(UserField.email.name(), lowerCasedEmail));

        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) this.getHibernateTemplate().findByCriteria(criteria);
        if (users.size() > 1) {
            throw new DataIntegrityViolationException("Email should be unique.");
        } else if (users.isEmpty()) {
            throw new DataRetrievalFailureException(String.format("No user with the email '%s' exists.",
                    lowerCasedEmail));
        }
        return users.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAllByAuthority(String authority) {
        this.logger.info(String.format("Load all users with the authority '%s')", authority));

        // If we did not explicitly select the user u, Hibernate would return tuples User-(the
        // given)Authority.
        String query = String.format("SELECT u FROM %s u INNER JOIN u.%s a WHERE a.%s = ?",
                PersistedClass.User.name(), UserField.authorities.name(),
                AuthorityField.authority.getFieldChain());
        return (List<User>) this.getHibernateTemplate().find(query, authority);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findAllByAuthorityAndSubscription(String authority, NotificationType notificationType) {
        this.logger.info(String.format("Load all users with the authority '%s' and subscription '%')",
                authority, notificationType.name()));

        String subscriptionColumn = UserField.getSubscriptionField(notificationType).getColumnName();

        // If we did not explicitly select the user u, Hibernate would return tuples User-(the
        // given)Authority.
        String query = String.format("SELECT u FROM %s u INNER JOIN u.%s a WHERE a.%s = ? AND u.%s = ?",
                PersistedClass.User.name(), UserField.authorities.name(),
                AuthorityField.authority.getFieldChain(), subscriptionColumn);
        return (List<User>) this.getHibernateTemplate().find(query, authority, Boolean.TRUE.toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAllOrdered(OrderType order, boolean testUsers) {
        this.logger.info(String.format("Load all users ordered %s.", order.name()));
        String query = String.format("FROM %s u WHERE u.%s = ? ORDER BY u.%s, u.%s %s",
                PersistedClass.User.name(), UserField.isTestUser.name(), UserField.lastName.name(),
                UserField.firstName.name(), order.getDatabaseCode());
        return (List<User>) this.getHibernateTemplate().find(query, testUsers);
    }

    @Override
    public void update(User user) {
        this.logger.info("Update an user " + user);
        this.getHibernateTemplate().update(user);

        // Updates authorities.
        this.authorityDao.delete(user.getUsername());
        this.authorityDao.save(user.getAuthorities());
    }

    @Override
    public void save(User user) {
        this.logger.info("Save a new user " + user);
        this.getHibernateTemplate().save(user);

        // Not necessary to save authorities explicitly. The command above has already done it.
    }

    @Override
    public void delete(User user) {
        this.getHibernateTemplate().delete(user);
    }

    @Override
    public void delete(String username) {
        User user = this.findByUsername(username);
        if (user != null) {
            this.delete(user);
        }
    }
}
