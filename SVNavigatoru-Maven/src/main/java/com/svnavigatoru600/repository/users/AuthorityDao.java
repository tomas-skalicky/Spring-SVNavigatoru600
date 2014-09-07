package com.svnavigatoru600.repository.users;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public interface AuthorityDao {

    /**
     * Returns all {@link Authority Authorities} stored in the repository which are associated with the given
     * <code>username</code>.
     */
    List<Authority> findAll(String username);

    /**
     * Stores the given {@link Authority authority} to the repository. If there is already an authority with
     * the same {@link com.svnavigatoru600.domain.users.AuthorityId#getUsername() username} and
     * {@link com.svnavigatoru600.domain.users.AuthorityId#getAuthority() authority's name}, throws an
     * exception.
     * <p>
     * <b>Preconditions:</b> {@link com.svnavigatoru600.domain.users.User User} with the new
     * <code>authority</code>' {@link com.svnavigatoru600.domain.users.AuthorityId#getUsername() username}
     * must have already been persisted in the repository.
     */
    void save(Authority authority);

    /**
     * Stores the given {@link GrantedAuthority authorities} to the repository. If there is already an
     * {@link Authority} with the same {@link com.svnavigatoru600.domain.users.AuthorityId#getUsername()
     * username} and {@link com.svnavigatoru600.domain.users.AuthorityId#getAuthority() authority's name},
     * throws an exception.
     * <p>
     * <b>Preconditions:</b> {@link com.svnavigatoru600.domain.users.User Users} with the new
     * <code>authorities</code>' {@link com.svnavigatoru600.domain.users.AuthorityId#getUsername() usernames}
     * must have already been persisted in the repository.
     */
    void save(Collection<GrantedAuthority> authorities);

    /**
     * Deletes all {@link Authority Authorities} of the specified
     * {@link com.svnavigatoru600.domain.users.User User}.
     * 
     * @param username
     *            Username (=login) of the user
     */
    void delete(String username);

    /**
     * Deletes a persisted {@link Authority Authority} of the specified
     * {@link com.svnavigatoru600.domain.users.User User} and the given {@link AuthorityType authorityType} if
     * such an authority exists. If it does not exist, NO exception is thrown.
     * 
     * @param username
     *            Username (=login) of the user
     * @param authorityType
     *            Type of the authority which is to be deleted
     */
    void delete(String username, AuthorityType authorityType);
}
