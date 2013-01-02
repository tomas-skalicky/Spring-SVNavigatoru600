package com.svnavigatoru600.service.users;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.repository.users.AuthorityDao;

/**
 * Provides convenient methods to work with {@link Authority} objects.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class AuthorityService {

    /**
     * The object which provides a persistence.
     */
    private final AuthorityDao authorityDao;

    /**
     * Constructor.
     */
    @Inject
    public AuthorityService(AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }

    /**
     * Returns all {@link Authority Authorities} stored in the repository which are associated with the given
     * <code>username</code>.
     */
    public List<Authority> findAll(String username) {
        return this.authorityDao.findAll(username);
    }

    /**
     * Stores the given {@link GrantedAuthority authorities} to the repository. If there is already an
     * {@link Authority} with the same {@link com.svnavigatoru600.domain.users.AuthorityId#getUsername()
     * username} and {@link com.svnavigatoru600.domain.users.AuthorityId#getAuthority() authority's name},
     * throws an exception.
     */
    public void save(Collection<GrantedAuthority> authorities) {
        this.authorityDao.save(authorities);
    }

    /**
     * Deletes all {@link Authority Authorities} of the specified
     * {@link com.svnavigatoru600.domain.users.User User}.
     * 
     * @param username
     *            The username (=login) of the user
     */
    public void delete(String username) {
        this.authorityDao.delete(username);
    }
}
