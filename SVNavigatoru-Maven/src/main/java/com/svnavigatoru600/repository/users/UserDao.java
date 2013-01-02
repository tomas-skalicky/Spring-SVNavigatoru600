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
     * Returns a {@link User} stored in the repository which has with the given <code>username</code>.
     */
    User findByUsername(String username);

    /**
     * Returns a {@link User} stored in the repository which has the given <code>email</code>.
     */
    User findByEmail(String email);

    /**
     * Returns all {@link User Users} stored in the repository which have the given <code>authority</code>.
     */
    List<User> findAllByAuthority(String authority);

    /**
     * Returns all {@link User Users} stored in the repository. The {@link User} are arranged according to
     * their last and first names in the given <code>order</code>.
     * 
     * @param testUsers
     *            If <code>true</code>, the method returns only test users. Otherwise, it returns only
     *            non-test users.
     */
    List<User> loadAllOrdered(OrderType order, boolean testUsers);

    /**
     * Updates the given <code>user</code> in the repository. The old version of the <code>user</code> should
     * be already stored there.
     */
    void update(User user);

    /**
     * Stores the given <code>user</code> to the repository. If there is already a {@link User} with the same
     * username or email, throws an exception.
     */
    void save(User user);

    /**
     * Deletes the given <code>user</code> together with all his authorities from the repository.
     */
    void delete(User user);
}
