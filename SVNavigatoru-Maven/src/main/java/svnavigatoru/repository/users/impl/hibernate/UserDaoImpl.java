package svnavigatoru.repository.users.impl.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import svnavigatoru.domain.users.User;
import svnavigatoru.repository.users.AuthorityDao;
import svnavigatoru.repository.users.UserDao;
import svnavigatoru.service.util.OrderType;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

	/** Logger for this class and subclasses */
	protected final Log logger = LogFactory.getLog(this.getClass());

	protected AuthorityDao authorityDao;

	@Autowired
	public void setAuthorityDao(AuthorityDao authorityDao) {
		this.authorityDao = authorityDao;
	}

	public User findByUsername(String username) {
		this.logger.info(String.format("Load an user with the username '%s'", username));
		return this.getHibernateTemplate().load(User.class, username);
	}

	public User findByEmail(String email) {
		String lowerCasedEmail = null;
		if (email != null) {
			lowerCasedEmail = email.toLowerCase();
		}

		this.logger.info(String.format("Load an user with the email '%s'", lowerCasedEmail));

		// Stands for 'where' clause.
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("email", lowerCasedEmail));

		@SuppressWarnings("unchecked")
		List<User> users = (List<User>) this.getHibernateTemplate().findByCriteria(criteria);
		if (users.size() > 1) {
			throw new DataIntegrityViolationException("Email should be unique.");
		} else if (users.size() == 0) {
			throw new DataRetrievalFailureException(String.format("No user with the email '%s' exists.",
					lowerCasedEmail));
		}
		return users.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<User> findByAuthority(String authority) {
		this.logger.info(String.format("Load all users with the authority '%s')", authority));

		// If we did not explicitly select the user u, Hibernate would return
		// tuples User-(the given)Authority.
		return (List<User>) this.getHibernateTemplate().find(
				"SELECT u FROM User u INNER JOIN u.authorities a WHERE a.id.authority = ?", authority);
	}

	@SuppressWarnings("unchecked")
	public List<User> loadAllOrdered(OrderType order) {
		this.logger.info(String.format("Load all users ordered %s.", order.name()));
		String query = String.format("FROM User u ORDER BY u.lastName, u.firstName %s", order.getDatabaseCode());
		return (List<User>) this.getHibernateTemplate().find(query);
	}

	public void update(User user) {
		String lowerCasedEmail = null;
		if ((user != null) && (user.getEmail() != null)) {
			lowerCasedEmail = user.getEmail().toLowerCase();
		}

		this.logger
				.info(String
						.format("Update an user (username '%s', password '%s', enabled '%b', first_name '%s', last_name '%s', email '%s', phone '%s')",
								user.getUsername(), user.getPassword(), user.isEnabled(), user.getFirstName(),
								user.getLastName(), lowerCasedEmail, user.getPhone()));
		this.getHibernateTemplate().update(user);

		// Updates authorities.
		this.authorityDao.delete(user.getUsername());
		this.authorityDao.save(user.getAuthorities());
	}

	public void save(User user) {
		String lowerCasedEmail = null;
		if ((user != null) && (user.getEmail() != null)) {
			lowerCasedEmail = user.getEmail().toLowerCase();
		}

		this.logger
				.info(String
						.format("Save a new user (username '%s', password '%s', enabled '%b', first_name '%s', last_name '%s', email '%s', phone '%s')",
								user.getUsername(), user.getPassword(), user.isEnabled(), user.getFirstName(),
								user.getLastName(), lowerCasedEmail, user.getPhone()));

		this.getHibernateTemplate().save(user);

		// Not necessary to save authorities explicitly. The command above has
		// already done it.
	}

	public void delete(User user) {
		this.getHibernateTemplate().delete(user);
	}
}
