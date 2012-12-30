package com.svnavigatoru600.repository.users.impl.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.users.AuthorityDao;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.repository.users.impl.UserField;
import com.svnavigatoru600.service.util.OrderType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(this.getClass());

    protected AuthorityDao authorityDao;

    @Autowired
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
    public List<User> findByAuthority(String authority) {
        this.logger.info(String.format("Load all users with the authority '%s')", authority));

        // If we did not explicitly select the user u, Hibernate would return
        // tuples User-(the given)Authority.
        String query = String.format("SELECT u FROM %s u INNER JOIN u.%s a WHERE a.id.authority = ?",
                PersistedClass.User.name(), UserField.authorities.name());
        return (List<User>) this.getHibernateTemplate().find(query, authority);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> loadAllOrdered(OrderType order, boolean testUsers) {
        this.logger.info(String.format("Load all users ordered %s.", order.name()));
        String query = String.format("FROM %s u WHERE u.%s = %s ORDER BY u.%s, u.%s %s",
                PersistedClass.User.name(), UserField.isTestUser.getColumnName(), testUsers,
                UserField.lastName.name(), UserField.firstName.name(), order.getDatabaseCode());
        return (List<User>) this.getHibernateTemplate().find(query);
    }

    @Override
    public void update(User user) {
        String lowerCasedEmail = null;
        if ((user != null) && (user.getEmail() != null)) {
            lowerCasedEmail = user.getEmail().toLowerCase();
        }

        this.logger
                .info(String
                        .format("Update an user (username '%s', password '%s', enabled '%b', first_name '%s', last_name '%s', email '%s', phone '%s')",
                                user.getUsername(), user.getPassword(), user.isEnabled(),
                                user.getFirstName(), user.getLastName(), lowerCasedEmail, user.getPhone()));
        this.getHibernateTemplate().update(user);

        // Updates authorities.
        this.authorityDao.delete(user.getUsername());
        this.authorityDao.save(user.getAuthorities());
    }

    @Override
    public void save(User user) {
        String lowerCasedEmail = null;
        if ((user != null) && (user.getEmail() != null)) {
            lowerCasedEmail = user.getEmail().toLowerCase();
        }

        this.logger
                .info(String
                        .format("Save a new user (username '%s', password '%s', enabled '%b', first_name '%s', last_name '%s', email '%s', phone '%s')",
                                user.getUsername(), user.getPassword(), user.isEnabled(),
                                user.getFirstName(), user.getLastName(), lowerCasedEmail, user.getPhone()));

        this.getHibernateTemplate().save(user);

        // Not necessary to save authorities explicitly. The command above has
        // already done it.
    }

    @Override
    public void delete(User user) {
        this.getHibernateTemplate().delete(user);
    }
}
