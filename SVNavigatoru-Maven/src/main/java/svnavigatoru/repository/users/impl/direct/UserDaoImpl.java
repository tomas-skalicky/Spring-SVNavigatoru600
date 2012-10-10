package svnavigatoru.repository.users.impl.direct;

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

import svnavigatoru.domain.users.Authority;
import svnavigatoru.domain.users.User;
import svnavigatoru.repository.users.AuthorityDao;
import svnavigatoru.repository.users.UserDao;
import svnavigatoru.service.util.OrderType;

public class UserDaoImpl extends SimpleJdbcDaoSupport implements UserDao {

	/** Logger for this class and subclasses */
	protected final Log logger = LogFactory.getLog(this.getClass());

	private static final String TABLE_NAME = "users";
	protected AuthorityDao authorityDao;

	@Autowired
	public void setAuthorityDao(AuthorityDao authorityDao) {
		this.authorityDao = authorityDao;
	}

	/**
	 * Populates the <code>authorities</code> property of the given
	 * <code>user</code>.
	 */
	private void populateAuthorities(User user) {
		List<Authority> authorities = this.authorityDao.find(user.getUsername());
		user.setAuthorities(new HashSet<GrantedAuthority>(authorities));
	}

	/**
	 * Populates the <code>authorities</code> property of all the given
	 * <code>users</code>.
	 */
	private void populateAuthorities(List<User> users) {
		for (User user : users) {
			this.populateAuthorities(user);
		}
	}

	public User findByUsername(String username) {
		return this.findByUsername(username, false);
	}

	/**
	 * @param lazy
	 *            {@link Authority}s of the desired {@link User} will not be
	 *            loaded.
	 */
	public User findByUsername(String username, boolean lazy) {
		this.logger.info(String.format("Load an user with the username '%s'", username));

		String query = String.format("SELECT * FROM %s u WHERE u.%s = ?", UserDaoImpl.TABLE_NAME,
				UserRowMapper.getColumn("username"));
		User user = this.getSimpleJdbcTemplate().queryForObject(query, new UserRowMapper(), username);
		if (user == null) {
			throw new DataRetrievalFailureException(String.format("No user with the username '%s' exists.", username));
		}

		if (!lazy) {
			this.populateAuthorities(user);
		}
		return user;
	}

	public User findByEmail(String email) {
		String lowerCasedEmail = null;
		if (email != null) {
			lowerCasedEmail = email.toLowerCase();
		}

		this.logger.info(String.format("Load an user with the email '%s'", lowerCasedEmail));

		String query = String.format("SELECT * FROM %s u WHERE u.%s = ?", UserDaoImpl.TABLE_NAME,
				UserRowMapper.getColumn("email"));
		User user = this.getSimpleJdbcTemplate().queryForObject(query, new UserRowMapper(), lowerCasedEmail);
		if (user == null) {
			throw new DataRetrievalFailureException(String.format("No user with the email '%s' exists.",
					lowerCasedEmail));
		}

		this.populateAuthorities(user);
		return user;
	}

	public List<User> findByAuthority(String authority) {
		this.logger.info(String.format("Load all users with the authority '%s')", authority));

		String query = String.format("SELECT u.* FROM %s u INNER JOIN %s a ON a.%s = u.%s WHERE a.%s = ?",
				UserDaoImpl.TABLE_NAME, AuthorityDaoImpl.TABLE_NAME, AuthorityRowMapper.getColumn("username"),
				UserRowMapper.getColumn("username"), AuthorityRowMapper.getColumn("authority"));
		List<User> users = this.getSimpleJdbcTemplate().query(query, new UserRowMapper(), authority);

		this.populateAuthorities(users);
		return users;
	}

	public List<User> loadAllOrdered(OrderType order) {
		this.logger.info(String.format("Load all users ordered %s.", order.name()));

		String query = String.format("SELECT * FROM %s u ORDER BY u.%s, u.%s %s", UserDaoImpl.TABLE_NAME,
				UserRowMapper.getColumn("lastName"), UserRowMapper.getColumn("firstName"), order.getDatabaseCode());
		List<User> users = this.getSimpleJdbcTemplate().query(query, new UserRowMapper());

		this.populateAuthorities(users);
		return users;
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

		// Parameters are those words which begin with ':' in the following
		// query.
		String query = String
				.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
						UserDaoImpl.TABLE_NAME, UserRowMapper.getColumn("password"),
						UserRowMapper.getColumn("enabled"), UserRowMapper.getColumn("firstName"),
						UserRowMapper.getColumn("lastName"), UserRowMapper.getColumn("email"),
						UserRowMapper.getColumn("phone"), UserRowMapper.getColumn("username"));
		this.getSimpleJdbcTemplate().update(query, user.getPassword(), user.isEnabled(), user.getFirstName(),
				user.getLastName(), lowerCasedEmail, user.getPhone(), user.getUsername());

		// Updates authorities.
		this.authorityDao.delete(user.getUsername());
		this.authorityDao.save(user.getAuthorities());
	}

	/**
	 * Used during the save of the given <code>user</code>.
	 */
	private Map<String, Object> getNamedParameters(User user) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(UserRowMapper.getColumn("username"), user.getUsername());
		parameters.put(UserRowMapper.getColumn("password"), user.getPassword());
		parameters.put(UserRowMapper.getColumn("enabled"), user.isEnabled());
		parameters.put(UserRowMapper.getColumn("firstName"), user.getFirstName());
		parameters.put(UserRowMapper.getColumn("lastName"), user.getLastName());

		String lowerCasedEmail = null;
		if ((user != null) && (user.getEmail() != null)) {
			lowerCasedEmail = user.getEmail().toLowerCase();
		}
		parameters.put(UserRowMapper.getColumn("email"), lowerCasedEmail);

		parameters.put(UserRowMapper.getColumn("phone"), user.getPhone());
		return parameters;
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

		SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource()).withTableName(UserDaoImpl.TABLE_NAME)
				.usingColumns(UserRowMapper.getColumn("username"), UserRowMapper.getColumn("password"),
						UserRowMapper.getColumn("enabled"), UserRowMapper.getColumn("firstName"),
						UserRowMapper.getColumn("lastName"), UserRowMapper.getColumn("email"),
						UserRowMapper.getColumn("phone"));
		insert.execute(this.getNamedParameters(user));

		// NOTE: explicit save of the user's authorities.
		this.authorityDao.save(user.getAuthorities());
	}

	public void delete(User user) {
		String query = String.format("DELETE FROM %s WHERE %s = ?", UserDaoImpl.TABLE_NAME,
				UserRowMapper.getColumn("username"));
		this.getSimpleJdbcTemplate().update(query, user.getUsername());
	}
}
