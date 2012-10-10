package svnavigatoru.repository.users;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import svnavigatoru.domain.users.Authority;
import svnavigatoru.domain.users.User;

public interface AuthorityDao {

	/**
	 * Returns all {@link Authority}s stored in the repository which are
	 * associated with the given <code>username</code>.
	 */
	public List<Authority> find(String username);

	/**
	 * Stores the given <code>authorities</code> to the repository. If there is
	 * already an {@link Authority} with the same username and authority's name,
	 * throws an exception.
	 */
	public void save(Collection<GrantedAuthority> authorities);

	/**
	 * Deletes all {@link Authority}s of {@link User} with the given
	 * <code>username</code> from the repository.
	 */
	public void delete(String username);
}
