package com.svnavigatoru600.repository.users;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.repository.MapperInterface;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@MapperInterface
public interface AuthorityDao {

    /**
     * Returns all {@link Authority Authorities} stored in the repository which are associated with the given
     * <code>username</code>.
     */
    List<Authority> findAll(String username);

    /**
     * Stores the given {@link GrantedAuthority authorities} to the repository. If there is already an
     * {@link Authority} with the same {@link com.svnavigatoru600.domain.users.AuthorityId#getUsername()
     * username} and {@link com.svnavigatoru600.domain.users.AuthorityId#getAuthority() authority's name},
     * throws an exception.
     */
    void save(Collection<GrantedAuthority> authorities);

    /**
     * Deletes all {@link Authority Authorities} of the specified
     * {@link com.svnavigatoru600.domain.users.User User}.
     * 
     * @param username
     *            The username (=login) of the user
     */
    void delete(String username);
}
