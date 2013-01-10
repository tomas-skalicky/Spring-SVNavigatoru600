package com.svnavigatoru600.repository.users;

import java.util.List;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.MapperInterface;
import com.svnavigatoru600.service.util.OrderType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@MapperInterface
public interface UserDao {

    /**
     * Returns an {@link User} stored in the repository which has the given <code>username</code>.
     * <p>
     * The returned user has its {@link User#getAuthorities() authorities} populated.
     */
    User findByUsername(String username);

    /**
     * Returns an {@link User} stored in the repository which has the given <code>email</code> address. If
     * such an {@link User} is not found, an exception is thrown.
     * <p>
     * The returned user has its {@link User#getAuthorities() authorities} populated.
     * 
     * @param email
     *            Case-insensitive email address of the desired user
     */
    User findByEmail(String email);

    /**
     * Returns all {@link User Users} stored in the repository which have the given <code>authority</code>.
     * <p>
     * {@link com.svnavigatoru600.domain.users.Authority Authorities} of the returned users are populated.
     */
    List<User> findAllByAuthority(String authority);

    /**
     * Returns all {@link User Users} stored in the repository arranged according to their
     * {@link User#getLastName() last names} and {@link User#getFirstName() first names} in the given
     * {@link OrderType order}.
     * <p>
     * {@link com.svnavigatoru600.domain.users.Authority Authorities} of the returned users are populated.
     * 
     * @param testUsers
     *            If <code>true</code>, the method returns only test users. Otherwise, it returns only
     *            non-test users. Test users are those which are not accessible to the business user (=
     *            customer).
     */
    List<User> findAllOrdered(OrderType order, boolean testUsers);

    /**
     * Updates the given {@link User} in the repository. The old version of this user should be already stored
     * there. His updated {@link com.svnavigatoru600.domain.users.Authority authorities} are persisted as
     * well.
     */
    void update(User user);

    /**
     * Stores the given {@link User} to the repository. If there is already a {@link User} with the same
     * {@link User#getUsername() username} or {@link User#getEmail() email}, throws an exception.
     * {@link com.svnavigatoru600.domain.users.Authority Authorities} of the user are persisted as well.
     */
    void save(User user);

    /**
     * Deletes the given {@link User} together with all his {@link com.svnavigatoru600.domain.users.Authority
     * Authorities} from the repository.
     */
    void delete(User user);
}
