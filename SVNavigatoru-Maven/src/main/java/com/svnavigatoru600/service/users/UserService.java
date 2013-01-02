package com.svnavigatoru600.service.users;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Hash;
import com.svnavigatoru600.service.util.OrderType;
import com.svnavigatoru600.service.util.Password;
import com.svnavigatoru600.service.util.UserUtils;

/**
 * Provides convenient methods to work with {@link User} objects.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class UserService {

    /**
     * The object which provides a persistence.
     */
    private final UserDao userDao;

    /**
     * Constructor.
     */
    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Returns a {@link User} stored in the repository which has with the given <code>username</code>.
     */
    public User findByUsername(String username) {
        return this.userDao.findByUsername(username);
    }

    /**
     * Returns a {@link User} stored in the repository which has the given <code>email</code>.
     * <p>
     * If <em>exactly one</em> {@link User} is found, an exception is thrown.
     */
    public User findByEmail(String email) {
        return this.userDao.findByEmail(email);
    }

    /**
     * Returns all {@link User Users} stored in the repository which have the given authority.
     */
    public List<User> findAllByAuthority(String authority) {
        return this.userDao.findAllByAuthority(authority);
    }

    /**
     * Returns all {@link User Users} stored in the repository arranged according to their
     * {@link User#getLastName() last names} and {@link User#getFirstName() first names} in the given
     * {@link OrderType order}.
     * 
     * @param testUsers
     *            If <code>true</code>, the method returns only test users. Otherwise, it returns only
     *            non-test users. Test users are those, which are not accessible by the customer.
     */
    public List<User> findAllOrdered(OrderType order, boolean testUsers) {
        return this.userDao.findAllOrdered(order, testUsers);
    }

    /**
     * Updates the given {@link User} in the repository. The old version of this user should be already stored
     * there.
     */
    public void update(User user) {
        this.userDao.update(user);
    }

    /**
     * Updates properties of the given <code>userToUpdate</code> and persists this {@link User} into the
     * repository. The old version of this user should be already stored there.
     * <p>
     * Finally, updates properties of the {@link User} object of the user which is currently logged to the web
     * app.
     * 
     * @param userToUpdate
     *            The persisted {@link User}
     * @param newEvent
     *            The {@link User} which contains new values of properties of <code>userToUpdate</code>. These
     *            values are copied to the persisted user.
     * @param newPassword
     *            New not-hashed password of the persisted user
     */
    public void update(User userToUpdate, User newUser, String newPassword) {
        boolean isPasswordUpdated = StringUtils.isNotBlank(newPassword);
        if (isPasswordUpdated) {
            userToUpdate.setPassword(Hash.doSha1Hashing(newPassword));
        }
        userToUpdate.setEmail(newUser.getEmail());
        userToUpdate.setPhone(newUser.getPhone());

        // Sets user's email to null if the email is blank. The reason is the
        // UNIQUE DB constraint.
        userToUpdate.setEmailToNullIfBlank();

        // Makes the data persistent.
        this.update(userToUpdate);

        final User loggedUser = UserUtils.getLoggedUser();
        loggedUser.setEmail(userToUpdate.getEmail());
        loggedUser.setPhone(userToUpdate.getPhone());
        if (isPasswordUpdated) {
            loggedUser.setPassword(userToUpdate.getPassword());
        }
    }

    /**
     * Stores the given {@link User} to the repository. If there is already a {@link User} with the same
     * {@link User#getUsername() username} or {@link User#getEmail() email}, throws an exception.
     */
    public void save(User user) {
        this.userDao.save(user);
    }

    /**
     * Deletes the given {@link User} together with all his {@link com.svnavigatoru600.domain.users.Authority
     * Authorities} from the repository.
     */
    public void delete(User user) {
        this.userDao.delete(user);
    }

    /**
     * Looks up in the repository and finds out whether an {@link User} with the given <code>username</code>
     * exists, or not.
     * 
     * @return <code>true</code> the corresponding {@link User} exists.
     */
    public boolean isUsernameOccupied(String username) {
        try {
            this.findByUsername(username);
            return true;
        } catch (DataRetrievalFailureException e) {
            return false;
        }
    }

    /**
     * Looks up in the repository and finds out whether an {@link User} with the given
     * <code>emailAddress</code> exists, or not.
     * 
     * @return <code>true</code> the corresponding {@link User} exists.
     */
    public boolean isEmailOccupied(String emailAddress) {
        try {
            this.findByEmail(emailAddress);
            return true;
        } catch (DataRetrievalFailureException e) {
            return false;
        }
    }

    /**
     * Sets the password of the specified {@link User} to a new generated password. Notifies the user about
     * this operation by email containing the new password.
     * 
     * @param email
     *            Email address of the {@link User} whose password is reset
     */
    public void resetPasswordAndSendEmail(String email, HttpServletRequest request,
            MessageSource messageSource) {
        User user = this.findByEmail(email);

        String newPassword = Password.generateNew();
        user.setPassword(Hash.doSha1Hashing(newPassword));
        this.update(user);

        Email.sendEmailOnPasswordReset(email, user.getLastName(), newPassword, request, messageSource);
    }

    /**
     * Loads more information about authors of the last saved {@link Contribution Contributions} (see
     * {@link Thread#getLastSavedContributions(List) getLastSavedContributions}).
     */
    public void loadAuthorsOfLastSavedContributions(Map<Thread, Contribution> lastSavedContributions) {
        for (Thread thread : lastSavedContributions.keySet()) {
            Contribution contribution = lastSavedContributions.get(thread);
            if (contribution == null) {
                continue;
            }

            String authorUsername = contribution.getAuthor().getUsername();
            contribution.setAuthor(this.findByUsername(authorUsername));
        }
    }
}
