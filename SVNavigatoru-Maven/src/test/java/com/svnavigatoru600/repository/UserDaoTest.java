package com.svnavigatoru600.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityTypeEnum;
import com.svnavigatoru600.domain.users.NotificationTypeEnum;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.AuthorityDao;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.test.category.PersistenceTests;

/**
 * Tests methods of the {@link UserDao} interface.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public final class UserDaoTest extends AbstractRepositoryTest {

    /**
     * Default rights of the first test user.
     */
    private static final AuthorityTypeEnum[] FIRST_USER_DEFAULT_AUTHORITIES = new AuthorityTypeEnum[] {
            AuthorityTypeEnum.ROLE_REGISTERED_USER, AuthorityTypeEnum.ROLE_MEMBER_OF_SV };
    /**
     * Default rights of the second test user.
     */
    private static final AuthorityTypeEnum[] SECOND_USER_DEFAULT_AUTHORITIES = new AuthorityTypeEnum[] {
            AuthorityTypeEnum.ROLE_USER_ADMINISTRATOR };
    /**
     * Default username of the third test user.
     */
    private static final String THIRD_USER_DEFAULT_USERNAME = "username 3";
    /**
     * Default email of the third test user.
     */
    private static final String THIRD_USER_DEFAULT_EMAIL = "email3@host.com";
    /**
     * Default authorities of the third test user.
     */
    private static final Set<GrantedAuthority> THIRD_USER_DEFAULT_AUTHORITIES = Sets.newHashSet();
    /**
     * The first test user.
     */
    private User firstUser = null;
    /**
     * The second test user.
     */
    private User secondUser = null;

    @Before
    public void createTestUsers() {
        firstUser = createFirstTestUser();
        secondUser = createSecondTestUser();
    }

    @Test
    public void testFindByExistingUsername() throws Exception {
        final UserDao userDao = TEST_UTILS.getUserDao();

        // SELECT ONE
        final User user = userDao.findByUsername(RepositoryTestUtils.SECOND_USER_DEFAULT_USERNAME);
        Assert.assertEquals(RepositoryTestUtils.SECOND_USER_DEFAULT_USERNAME, user.getUsername());
        Assert.assertEquals(RepositoryTestUtils.SECOND_USER_DEFAULT_EMAIL, user.getEmail());
        final int expectedAuthoritiesCount = SECOND_USER_DEFAULT_AUTHORITIES.length;
        Assert.assertEquals(expectedAuthoritiesCount, user.getAuthorities().size());
        final Authority[] actualAuthorities = user.getAuthorities().toArray(new Authority[expectedAuthoritiesCount]);
        final Collection<String> actualAuthorityTypes = TEST_UTILS.extractAuthorityTypes(actualAuthorities);
        Assert.assertTrue(actualAuthorityTypes.contains(SECOND_USER_DEFAULT_AUTHORITIES[0].name()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testFindByNotExistingUsername() throws Exception {
        final UserDao userDao = TEST_UTILS.getUserDao();

        // SELECT ONE
        // Throws an exception since such an user does not exist.
        userDao.findByUsername("not-existing username");
    }

    @Test
    public void testFindByExistingEmail() throws Exception {
        final UserDao userDao = TEST_UTILS.getUserDao();

        // SELECT ONE
        final User user = userDao.findByEmail(RepositoryTestUtils.USER_DEFAULT_EMAIL);
        Assert.assertEquals(RepositoryTestUtils.USER_DEFAULT_USERNAME, user.getUsername());
        Assert.assertEquals(RepositoryTestUtils.USER_DEFAULT_EMAIL, user.getEmail());
        final int expectedAuthoritiesCount = FIRST_USER_DEFAULT_AUTHORITIES.length;
        Assert.assertEquals(expectedAuthoritiesCount, user.getAuthorities().size());
        final Authority[] actualAuthorities = user.getAuthorities().toArray(new Authority[expectedAuthoritiesCount]);
        final Collection<String> actualAuthorityTypes = TEST_UTILS.extractAuthorityTypes(actualAuthorities);
        Assert.assertTrue(actualAuthorityTypes.contains(FIRST_USER_DEFAULT_AUTHORITIES[0].name()));
        Assert.assertTrue(actualAuthorityTypes.contains(FIRST_USER_DEFAULT_AUTHORITIES[1].name()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testFindByNotExistingEmail() throws Exception {
        final UserDao userDao = TEST_UTILS.getUserDao();

        // SELECT ONE
        // Throws an exception since such an user does not exist.
        userDao.findByEmail("not-existing email@host.com");
    }

    @Test
    public void testFindAllByAuthoritySomeoneFound() throws Exception {
        final UserDao userDao = TEST_UTILS.getUserDao();
        final AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // TWO DELETES & ONE INSERT
        final AuthorityTypeEnum authorityType = RepositoryTestUtils.AUTHORITY_DEFAULT_TYPE;
        authorityDao.delete(firstUser.getUsername(), authorityType);
        authorityDao.delete(secondUser.getUsername(), authorityType);
        authorityDao.save(new Authority(secondUser.getUsername(), authorityType));

        // SELECT ALL
        final List<User> foundUsers = userDao.findAllByAuthority(authorityType.name());
        final int expectedFoundUserCount = 1;
        Assert.assertEquals(expectedFoundUserCount, foundUsers.size());
        Assert.assertEquals(secondUser.getUsername(), foundUsers.get(0).getUsername());
    }

    @Test
    public void testFindAllByAuthorityNobodyFound() throws Exception {
        final UserDao userDao = TEST_UTILS.getUserDao();
        final AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // TWO DELETES
        final AuthorityTypeEnum authorityType = RepositoryTestUtils.SECOND_AUTHORITY_DEFAULT_TYPE;
        authorityDao.delete(firstUser.getUsername(), authorityType);
        authorityDao.delete(secondUser.getUsername(), authorityType);

        // SELECT ALL
        final List<User> foundUsers = userDao.findAllByAuthority(authorityType.name());
        final int expectedFoundUserCount = 0;
        Assert.assertEquals(expectedFoundUserCount, foundUsers.size());
    }

    @Test
    public void testFindAllByAuthorityAndSubscription() throws Exception {
        final UserDao userDao = TEST_UTILS.getUserDao();

        // THREE UPDATES
        final AuthorityTypeEnum authorityType = RepositoryTestUtils.AUTHORITY_DEFAULT_TYPE;
        final NotificationTypeEnum notificationType = NotificationTypeEnum.IN_OTHER_DOCUMENTS;
        firstUser.addAuthority(authorityType);
        firstUser.setSubscribedToOtherDocuments(false);
        userDao.update(firstUser);
        secondUser.addAuthority(authorityType);
        secondUser.setSubscribedToOtherDocuments(true);
        userDao.update(secondUser);
        final User thirdUser = createThirdDefaultTestUser();
        thirdUser.addAuthority(AuthorityTypeEnum.ROLE_USER_ADMINISTRATOR);
        thirdUser.setSubscribedToOtherDocuments(true);
        userDao.update(thirdUser);

        // SELECT ALL
        final List<User> foundUsers = userDao.findAllByAuthorityAndSubscription(authorityType.name(), notificationType);
        final int expectedFoundUserCount = 1;
        Assert.assertEquals(expectedFoundUserCount, foundUsers.size());
        Assert.assertEquals(secondUser.getUsername(), foundUsers.get(0).getUsername());
    }

    @Test
    public void testFindAllByAuthorityAndSubscriptionAllUsers() throws Exception {
        final UserDao userDao = TEST_UTILS.getUserDao();

        // THREE UPDATES
        final AuthorityTypeEnum authorityType = RepositoryTestUtils.AUTHORITY_DEFAULT_TYPE;
        final NotificationTypeEnum notificationType = NotificationTypeEnum.IN_OTHER_DOCUMENTS;
        firstUser.addAuthority(authorityType);
        firstUser.setSubscribedToOtherDocuments(true);
        userDao.update(firstUser);
        secondUser.addAuthority(authorityType);
        secondUser.setSubscribedToOtherDocuments(true);
        userDao.update(secondUser);

        // SELECT ALL
        final List<User> foundUsers = userDao.findAllByAuthorityAndSubscription(authorityType.name(), notificationType);
        final int expectedFoundUserCount = 2;
        final List<String> usernames = getUsernames(foundUsers);
        Assert.assertEquals(expectedFoundUserCount, usernames.size());
        Assert.assertTrue(usernames.contains(firstUser.getUsername()));
        Assert.assertTrue(usernames.contains(secondUser.getUsername()));
    }

    /**
     * Creates and saves the first test user.
     *
     * @return Newly created first test user
     */
    private User createFirstTestUser() {
        final User user = TEST_UTILS.createDefaultTestUser();
        updateTestUser(user, FIRST_USER_DEFAULT_AUTHORITIES);
        return user;
    }

    /**
     * Creates and saves the second test user.
     *
     * @return Newly created second test user
     */
    private User createSecondTestUser() {
        final User user = TEST_UTILS.createSecondDefaultTestUser();
        updateTestUser(user, SECOND_USER_DEFAULT_AUTHORITIES);
        return user;
    }

    /**
     * Creates and saves the third default test user.
     *
     * @return Newly created user
     */
    private User createThirdDefaultTestUser() {
        return TEST_UTILS.saveTestUser(TEST_UTILS.createDefaultUserBuilder().withUsername(THIRD_USER_DEFAULT_USERNAME)
                .withEmail(THIRD_USER_DEFAULT_EMAIL).withAuthorities(THIRD_USER_DEFAULT_AUTHORITIES));
    }

    /**
     * Assigns the given authorities to the given user and persists these changes.
     *
     * @param desiredAuthorities
     *            Authorities which are to be assigned to the user.
     */
    private void updateTestUser(final User user, final AuthorityTypeEnum[] desiredAuthorities) {
        final String username = user.getUsername();
        final Collection<GrantedAuthority> userAuthorities = user.getAuthorities();
        for (final AuthorityTypeEnum authorityType : desiredAuthorities) {
            userAuthorities.add(new Authority(username, authorityType));
        }

        TEST_UTILS.getUserDao().update(user);
    }

    /**
     * Gets an array of usernames of the given users.
     */
    private List<String> getUsernames(final List<User> users) {
        final List<String> usernames = new ArrayList<String>(users.size());
        for (final User user : users) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }
}
