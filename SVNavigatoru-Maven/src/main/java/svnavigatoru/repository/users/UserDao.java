package svnavigatoru.repository.users;

import java.util.List;

import svnavigatoru.domain.users.User;
import svnavigatoru.service.util.OrderType;

public interface UserDao {

	/**
	 * Returns a {@link User} stored in the repository which has with the given
	 * <code>username</code>.
	 */
	public User findByUsername(String username);

	/**
	 * Returns a {@link User} stored in the repository which has the given
	 * <code>email</code>.
	 */
	public User findByEmail(String email);

	/**
	 * Returns all {@link User}s stored in the repository which have the given
	 * <code>authority</code>.
	 */
	public List<User> findByAuthority(String authority);

	/**
	 * Returns all {@link User}s stored in the repository. The {@link User} are
	 * arranged according to their last and first names in the given
	 * <code>order</code>.
	 */
	public List<User> loadAllOrdered(OrderType order);

	/**
	 * Updates the given <code>user</code> in the repository. The old version of
	 * the <code>user</code> should be already stored there.
	 */
	public void update(User user);

	/**
	 * Stores the given <code>user</code> to the repository. If there is already
	 * a {@link User} with the same username or email, throws an exception.
	 */
	public void save(User user);

	/**
	 * Deletes the given <code>user</code> together with all his authorities
	 * from the repository.
	 */
	public void delete(User user);
}
