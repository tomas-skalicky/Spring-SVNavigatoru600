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
     * Stores the given <code>authorities</code> to the repository. If there is already an {@link Authority}
     * with the same username and authority's name, throws an exception.
     */
    void save(Collection<GrantedAuthority> authorities);

    /**
     * Deletes all {@link Authority Authorities} of {@link com.svnavigatoru600.domain.users.User user} with
     * the given <code>username</code> from the repository.
     */
    void delete(String username);
}
