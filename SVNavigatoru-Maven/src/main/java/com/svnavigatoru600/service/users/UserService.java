package com.svnavigatoru600.service.users;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.NotificationSubscriberVisitor;
import com.svnavigatoru600.domain.users.NotificationType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.service.util.Hash;
import com.svnavigatoru600.service.util.Localization;
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
     * Assembles emails concerning user administration and sends them to corresponding {@link User users}.
     */
    private UserEmailService emailService;

    @Inject
    public UserService(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Inject
    public void setEmailService(final UserEmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Returns an {@link User} stored in the repository which has the given <code>username</code>. If such an
     * {@link User} is not found, an exception is thrown.
     * <p>
     * The returned user has its {@link User#getAuthorities() authorities} populated.
     */
    public User findByUsername(final String username) {
        return userDao.findByUsername(username);
    }

    /**
     * Returns an {@link User} stored in the repository which has the given <code>email</code> address. If such an
     * {@link User} is not found, an exception is thrown.
     * <p>
     * The returned user has its {@link User#getAuthorities() authorities} populated.
     * 
     * @param email
     *            Case-insensitive email address of the desired user
     */
    public User findByEmail(final String email) {
        return userDao.findByEmail(email);
    }

    /**
     * Returns all {@link User Users} stored in the repository which have the given {@link AuthorityType authority}.
     * <p>
     * {@link com.svnavigatoru600.domain.users.Authority Authorities} of the returned users are populated.
     */
    public List<User> findAllByAuthority(final AuthorityType authority) {
        return userDao.findAllByAuthority(authority.name());
    }

    /**
     * Returns all {@link User administrators} stored in the repository.
     * <p>
     * {@link com.svnavigatoru600.domain.users.Authority Authorities} of the returned administrators are populated.
     */
    public List<User> findAllAdministrators() {
        return findAllByAuthority(AuthorityType.ROLE_USER_ADMINISTRATOR);
    }

    /**
     * Returns all {@link User Users} stored in the repository which have the given {@link AuthorityType authority} and
     * which are subscribed to the given {@link NotificationType notifications}.
     * <p>
     * {@link com.svnavigatoru600.domain.users.Authority Authorities} of the returned users are NOT populated.
     */
    public List<User> findAllByAuthorityAndSubscription(final AuthorityType authority, final NotificationType notificationType) {
        return userDao.findAllByAuthorityAndSubscription(authority.name(), notificationType);
    }

    /**
     * Returns all {@link User Users} with email stored in the repository which have the given {@link AuthorityType
     * authority} and which are subscribed to the given {@link NotificationType notifications}.
     * <p>
     * {@link com.svnavigatoru600.domain.users.Authority Authorities} of the returned users are NOT populated.
     */
    public List<User> findAllWithEmailByAuthorityAndSubscription(final AuthorityType authority,
            final NotificationType notificationType) {
        final List<User> users = userDao.findAllByAuthorityAndSubscription(authority.name(), notificationType);
        final List<User> usersWithEmail = new ArrayList<User>(users.size());
        for (final User user : users) {
            if (StringUtils.isNotBlank(user.getEmail())) {
                usersWithEmail.add(user);
            }
        }
        return usersWithEmail;
    }

    /**
     * Returns all {@link User Users} stored in the repository arranged according to their {@link User#getLastName()
     * last names} and {@link User#getFirstName() first names} in the given {@link OrderType order}.
     * <p>
     * {@link com.svnavigatoru600.domain.users.Authority Authorities} of the returned users are populated.
     * 
     * @param testUsers
     *            If <code>true</code>, the method returns only test users. Otherwise, it returns only non-test users.
     *            Test users are those which are not accessible to the business user (= customer).
     */
    public List<User> findAllOrdered(final OrderType order, final boolean testUsers) {
        return userDao.findAllOrdered(order, testUsers);
    }

    /**
     * Returns all {@link User Users} stored in the repository arranged according to their {@link User#getLastName()
     * last names} and {@link User#getFirstName() first names} ascending. Considers only not-test users.
     * <p>
     * {@link com.svnavigatoru600.domain.users.Authority Authorities} of the returned users are populated.
     */
    public List<User> findAllOrdered() {
        return this.findAllOrdered(OrderType.ASCENDING, false);
    }

    /**
     * Updates the given {@link User} in the repository. The old version of this user should be already stored there.
     */
    public void update(final User user) {
        userDao.update(user);
    }

    /**
     * Updates properties of the given <code>userToUpdate</code> and persists this {@link User} into the repository. The
     * old version of this user should be already stored there. His updated
     * {@link com.svnavigatoru600.domain.users.Authority authorities} are persisted as well.
     * <p>
     * Finally, updates properties of the {@link User} object of the user which is currently logged to this web app.
     * 
     * @param userToUpdate
     *            Persisted {@link User}
     * @param newUser
     *            {@link User} which contains new values of properties of <code>userToUpdate</code>. These values are
     *            copied to the persisted user.
     * @param newPassword
     *            New not-hashed password of the persisted user
     */
    public void update(final User userToUpdate, final User newUser, final String newPassword) {
        final boolean isPasswordUpdated = StringUtils.isNotBlank(newPassword);
        if (isPasswordUpdated) {
            userToUpdate.setPassword(Hash.doSha1Hashing(newPassword));
        }
        userToUpdate.copyEmailPhoneSubscriptions(newUser);

        // Sets user's email to null if the email is blank. The reason is the
        // UNIQUE DB constraint.
        userToUpdate.setEmailToNullIfBlank();

        // Makes the data persistent.
        this.update(userToUpdate);

        final User loggedUser = UserUtils.getLoggedUser();
        loggedUser.copyEmailPhoneSubscriptions(userToUpdate);
        if (isPasswordUpdated) {
            loggedUser.setPassword(userToUpdate.getPassword());
        }
    }

    /**
     * Hashes the given <code>newPassword</code> and assigns it to the given {@link User userToUpdate}. Then, updates
     * other properties of the given <code>userToUpdate</code> and persists this {@link User} into the repository. The
     * old version of this user should be already stored there. His updated
     * {@link com.svnavigatoru600.domain.users.Authority authorities} are persisted as well.
     * <p>
     * Finally, notifies the <code>userToUpdate</code> by email about changes in his existing account.
     * 
     * @param userToUpdate
     *            Persisted {@link User}
     * @param newUser
     *            {@link User} which contains new values of properties of <code>userToUpdate</code>. These values are
     *            copied to the persisted user.
     * @param newPassword
     *            New not-hashed password of the persisted user
     * @param indicatorsOfNewAuthorities
     *            <code>true</code> in the index <code>x</code> in the array means that the authority with the
     *            {@link AuthorityType#ordinal() ordinal}<code> == x</code> has been selected as one of the new
     *            authorities of the persisted user; and vice versa.
     */
    public void updateAndNotifyUser(final User userToUpdate, final User newUser, final String newPassword,
            final boolean[] indicatorsOfNewAuthorities, final HttpServletRequest request, final MessageSource messageSource) {
        boolean passwordChanged = false;
        final boolean isPasswordUpdated = StringUtils.isNotBlank(newPassword);
        if (isPasswordUpdated) {
            final String newPasswordHash = Hash.doSha1Hashing(newPassword);
            passwordChanged = !newPasswordHash.equals(userToUpdate.getPassword());
            userToUpdate.setPassword(newPasswordHash);
        }
        userToUpdate.setFirstName(newUser.getFirstName());
        userToUpdate.setLastName(newUser.getLastName());

        final boolean authoritiesChanged = userToUpdate.updateAuthorities(indicatorsOfNewAuthorities);

        // Sets user's email to null if the email is blank. The reason is the UNIQUE DB constraint.
        userToUpdate.setEmailToNullIfBlank();

        this.update(userToUpdate);

        if (passwordChanged) {
            emailService.sendEmailOnPasswordChange(userToUpdate, newPassword, request, messageSource);
        }
        if (authoritiesChanged) {
            emailService.sendEmailOnAuthoritiesChange(userToUpdate, request, messageSource);
        }
    }

    /**
     * Stores the given {@link User} to the repository. If there is already a {@link User} with the same
     * {@link User#getUsername() username} or {@link User#getEmail() email}, throws an exception.
     * {@link com.svnavigatoru600.domain.users.Authority Authorities} of the user are persisted as well.
     */
    public void save(final User user) {
        userDao.save(user);
    }

    /**
     * Hashes the given <code>newPassword</code> and assigns it to the given {@link User newUser}. Then, stores this
     * user to the repository. If there is already a {@link User} with the same {@link User#getUsername() username} or
     * {@link User#getEmail() email}, throws an exception. {@link com.svnavigatoru600.domain.users.Authority
     * Authorities} of the user are persisted as well.
     * <p>
     * Finally, notifies the user by email about his new account.
     * 
     * @param newUser
     *            New user
     * @param newPassword
     *            New not-hashed password of the new user
     * @param indicatorsOfNewAuthorities
     *            <code>true</code> in the index <code>x</code> in the array means that the authority with the
     *            {@link AuthorityType#ordinal() ordinal}<code> == x</code> has been selected as one of the new
     *            authorities of the persisted user; and vice versa.
     */
    public void saveAndNotifyUser(final User newUser, final String newPassword, final boolean[] indicatorsOfNewAuthorities,
            final HttpServletRequest request, final MessageSource messageSource) {
        newUser.setPassword(Hash.doSha1Hashing(newPassword));

        newUser.updateAuthorities(indicatorsOfNewAuthorities);
        newUser.subscribeToEverything();

        // Sets user's email to null if the email is blank. The reason is the UNIQUE DB constraint.
        newUser.setEmailToNullIfBlank();

        save(newUser);

        if (!StringUtils.isBlank(newUser.getEmail())) {
            emailService.sendEmailOnUserCreation(newUser, newPassword, request, messageSource);
        }
    }

    /**
     * Deletes the given {@link User} together with all his {@link com.svnavigatoru600.domain.users.Authority
     * Authorities} from the repository.
     */
    public void delete(final User user) {
        userDao.delete(user);
    }

    /**
     * Deletes the specified {@link User} together with all his {@link com.svnavigatoru600.domain.users.Authority
     * Authorities} from the repository.
     * <p>
     * Finally, notifies the user about the deletion of his account.
     * 
     * @param username
     *            Username (=login) of the user which is to be deleted
     */
    public void delete(final String username, final HttpServletRequest request, final MessageSource messageSource) {
        final User user = findByUsername(username);
        userDao.delete(user);

        emailService.sendEmailOnUserDeletion(user, request, messageSource);
    }

    /**
     * Unsubscribes the specified {@link User} from receiving notifications which are of the given
     * {@link NotificationType}. This change is persisted.
     * 
     * @param username
     *            Username (= login) of the affected user.
     */
    public void unsubscribeFromNotifications(final String username, final NotificationType notificationType) {
        final User user = userDao.findByUsername(username);
        notificationType.accept(new NotificationSubscriberVisitor(user, false));
        userDao.update(user, false);
    }

    /**
     * Looks up in the repository and finds out whether an {@link User} with the given <code>username</code> exists, or
     * not.
     * 
     * @return <code>true</code> the corresponding {@link User} exists.
     */
    public boolean isUsernameOccupied(final String username) {
        try {
            findByUsername(username);
            return true;
        } catch (final DataRetrievalFailureException e) {
            return false;
        }
    }

    /**
     * Looks up in the repository and finds out whether an {@link User} with the given <code>emailAddress</code> exists,
     * or not.
     * 
     * @return <code>true</code> the corresponding {@link User} exists.
     */
    public boolean isEmailOccupied(final String emailAddress) {
        try {
            findByEmail(emailAddress);
            return true;
        } catch (final DataRetrievalFailureException e) {
            return false;
        }
    }

    /**
     * Sets the password of the specified {@link User} to a new generated password. Notifies the user about this
     * operation by email containing the new password.
     * 
     * @param email
     *            Email address of the {@link User} whose password is reset
     */
    public void resetPasswordAndNotifyUser(final String email, final HttpServletRequest request, final MessageSource messageSource) {
        final User user = findByEmail(email);

        final String newPassword = Password.generateNew();
        user.setPassword(Hash.doSha1Hashing(newPassword));
        this.update(user);

        emailService.sendEmailOnPasswordReset(user, newPassword, request, messageSource);
    }

    /**
     * Loads more information about authors of the last saved {@link Contribution Contributions} (see
     * {@link Thread#getLastSavedContributions(List) getLastSavedContributions}).
     */
    public void loadAuthorsOfLastSavedContributions(final Map<Thread, Contribution> lastSavedContributions) {
        for (final Thread thread : lastSavedContributions.keySet()) {
            final Contribution contribution = lastSavedContributions.get(thread);
            if (contribution == null) {
                continue;
            }

            final String authorUsername = contribution.getAuthor().getUsername();
            contribution.setAuthor(findByUsername(authorUsername));
        }
    }

    /**
     * Gets a {@link Map} which for each input {@link User} contains a corresponding localized delete question which is
     * asked before deletion of that user.
     */
    public static Map<User, String> getLocalizedDeleteQuestions(final List<User> users, final HttpServletRequest request,
            final MessageSource messageSource) {
        final String messageCode = "user-administration.do-you-really-want-to-delete-user";
        final Map<User, String> questions = new HashMap<User, String>();

        for (final User user : users) {
            final Object[] messageParams = new Object[] { user.getUsername(), user.getFullName() };
            questions.put(user, Localization.findLocaleMessage(messageSource, request, messageCode, messageParams));
        }
        return questions;
    }

    /**
     * Gets a {@link Map} which for each constant of the {@link NotificationType} enumeration contains a pair of its
     * {@link NotificationType#getOrdinal() ordinal} and its localized title.
     */
    public static Map<Long, String> getLocalizedNotificationTitles(final HttpServletRequest request,
            final MessageSource messageSource) {
        final Map<Long, String> ordinalTitleMap = new HashMap<Long, String>();

        for (final NotificationType type : NotificationType.values()) {
            final String localizedTitle = Localization.findLocaleMessage(messageSource, request,
                    type.getTitleLocalizationCode());
            ordinalTitleMap.put(type.getOrdinal(), localizedTitle);
        }
        return ordinalTitleMap;
    }

    public User getLoggerUser() {
        return UserUtils.getLoggedUser();
    }
}
