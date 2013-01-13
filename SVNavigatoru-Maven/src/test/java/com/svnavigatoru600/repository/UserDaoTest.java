package com.svnavigatoru600.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.NotificationType;
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
public class UserDaoTest extends AbstractRepositoryTest {

    /**
     * Default rights of the first test user.
     */
    private static final AuthorityType[] FIRST_USER_DEFAULT_AUTHORITIES = new AuthorityType[] {
            AuthorityType.ROLE_REGISTERED_USER, AuthorityType.ROLE_MEMBER_OF_SV };
    /**
     * Default rights of the second test user.
     */
    private static final AuthorityType[] SECOND_USER_DEFAULT_AUTHORITIES = new AuthorityType[] { AuthorityType.ROLE_USER_ADMINISTRATOR };
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
    private static final Set<GrantedAuthority> THIRD_USER_DEFAULT_AUTHORITIES = new HashSet<GrantedAuthority>();
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
        this.firstUser = this.createFirstTestUser();
        this.secondUser = this.createSecondTestUser();
    }

    @Test
    public void testFindByExistingUsername() throws Exception {
        UserDao userDao = TEST_UTILS.getUserDao();

        // SELECT ONE
        User user = userDao.findByUsername(RepositoryTestUtils.SECOND_USER_DEFAULT_USERNAME);
        Assert.assertEquals(RepositoryTestUtils.SECOND_USER_DEFAULT_USERNAME, user.getUsername());
        Assert.assertEquals(RepositoryTestUtils.SECOND_USER_DEFAULT_EMAIL, user.getEmail());
        int expectedAuthoritiesCount = SECOND_USER_DEFAULT_AUTHORITIES.length;
        Assert.assertEquals(expectedAuthoritiesCount, user.getAuthorities().size());
        Authority[] actualAuthorities = user.getAuthorities()
                .toArray(new Authority[expectedAuthoritiesCount]);
        Collection<String> actualAuthorityTypes = TEST_UTILS.extractAuthorityTypes(actualAuthorities);
        Assert.assertTrue(actualAuthorityTypes.contains(SECOND_USER_DEFAULT_AUTHORITIES[0].name()));
    }

    @Test
    public void testFindByNotExistingUsername() throws Exception {
        UserDao userDao = TEST_UTILS.getUserDao();

        // SELECT ONE
        try {
            userDao.findByUsername("not-existing username");
            Assert.fail("User with not-existing username should not have been found");
        } catch (DataRetrievalFailureException e) {
            // OK since such an user does not exist.
            ;
        }
    }

    @Test
    public void testFindByExistingEmail() throws Exception {
        UserDao userDao = TEST_UTILS.getUserDao();

        // SELECT ONE
        User user = userDao.findByEmail(RepositoryTestUtils.USER_DEFAULT_EMAIL);
        Assert.assertEquals(RepositoryTestUtils.USER_DEFAULT_USERNAME, user.getUsername());
        Assert.assertEquals(RepositoryTestUtils.USER_DEFAULT_EMAIL, user.getEmail());
        int expectedAuthoritiesCount = FIRST_USER_DEFAULT_AUTHORITIES.length;
        Assert.assertEquals(expectedAuthoritiesCount, user.getAuthorities().size());
        Authority[] actualAuthorities = user.getAuthorities()
                .toArray(new Authority[expectedAuthoritiesCount]);
        Collection<String> actualAuthorityTypes = TEST_UTILS.extractAuthorityTypes(actualAuthorities);
        Assert.assertTrue(actualAuthorityTypes.contains(FIRST_USER_DEFAULT_AUTHORITIES[0].name()));
        Assert.assertTrue(actualAuthorityTypes.contains(FIRST_USER_DEFAULT_AUTHORITIES[1].name()));
    }

    @Test
    public void testFindByNotExistingEmail() throws Exception {
        UserDao userDao = TEST_UTILS.getUserDao();

        // SELECT ONE
        try {
            userDao.findByEmail("not-existing email@host.com");
            Assert.fail("User with not-existing email should not have been found");
        } catch (DataRetrievalFailureException e) {
            // OK since such an user does not exist.
            ;
        }
    }

    @Test
    public void testFindAllByAuthoritySomeoneFound() throws Exception {
        UserDao userDao = TEST_UTILS.getUserDao();
        AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // TWO DELETES & ONE INSERT
        AuthorityType authorityType = RepositoryTestUtils.AUTHORITY_DEFAULT_TYPE;
        authorityDao.delete(this.firstUser.getUsername(), authorityType);
        authorityDao.delete(this.secondUser.getUsername(), authorityType);
        authorityDao.save(new Authority(this.secondUser.getUsername(), authorityType));

        // SELECT ALL
        List<User> foundUsers = userDao.findAllByAuthority(authorityType.name());
        int expectedFoundUserCount = 1;
        Assert.assertEquals(expectedFoundUserCount, foundUsers.size());
        Assert.assertEquals(this.secondUser.getUsername(), foundUsers.get(0).getUsername());
    }

    @Test
    public void testFindAllByAuthorityNobodyFound() throws Exception {
        UserDao userDao = TEST_UTILS.getUserDao();
        AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // TWO DELETES
        AuthorityType authorityType = RepositoryTestUtils.SECOND_AUTHORITY_DEFAULT_TYPE;
        authorityDao.delete(this.firstUser.getUsername(), authorityType);
        authorityDao.delete(this.secondUser.getUsername(), authorityType);

        // SELECT ALL
        List<User> foundUsers = userDao.findAllByAuthority(authorityType.name());
        int expectedFoundUserCount = 0;
        Assert.assertEquals(expectedFoundUserCount, foundUsers.size());
    }

    @Test
    public void testFindAllByAuthorityAndSubscription() throws Exception {
        UserDao userDao = TEST_UTILS.getUserDao();

        // THREE UPDATES
        AuthorityType authorityType = RepositoryTestUtils.AUTHORITY_DEFAULT_TYPE;
        NotificationType notificationType = NotificationType.IN_OTHER_DOCUMENTS;
        this.firstUser.addAuthority(authorityType);
        this.firstUser.setSubscribedToOtherDocuments(false);
        userDao.update(this.firstUser);
        this.secondUser.addAuthority(authorityType);
        this.secondUser.setSubscribedToOtherDocuments(true);
        userDao.update(this.secondUser);
        User thirdUser = this.createThirdDefaultTestUser();
        thirdUser.addAuthority(AuthorityType.ROLE_USER_ADMINISTRATOR);
        thirdUser.setSubscribedToOtherDocuments(true);
        userDao.update(thirdUser);

        // SELECT ALL
        List<User> foundUsers = userDao.findAllByAuthorityAndSubscription(authorityType.name(),
                notificationType);
        int expectedFoundUserCount = 1;
        Assert.assertEquals(expectedFoundUserCount, foundUsers.size());
        Assert.assertEquals(this.secondUser.getUsername(), foundUsers.get(0).getUsername());
    }

    @Test
    public void testFindAllByAuthorityAndSubscriptionAllUsers() throws Exception {
        UserDao userDao = TEST_UTILS.getUserDao();

        // THREE UPDATES
        AuthorityType authorityType = RepositoryTestUtils.AUTHORITY_DEFAULT_TYPE;
        NotificationType notificationType = NotificationType.IN_OTHER_DOCUMENTS;
        this.firstUser.addAuthority(authorityType);
        this.firstUser.setSubscribedToOtherDocuments(true);
        userDao.update(this.firstUser);
        this.secondUser.addAuthority(authorityType);
        this.secondUser.setSubscribedToOtherDocuments(true);
        userDao.update(this.secondUser);

        // SELECT ALL
        List<User> foundUsers = userDao.findAllByAuthorityAndSubscription(authorityType.name(),
                notificationType);
        int expectedFoundUserCount = 2;
        List<String> usernames = this.getUsernames(foundUsers);
        Assert.assertEquals(expectedFoundUserCount, usernames.size());
        Assert.assertTrue(usernames.contains(this.firstUser.getUsername()));
        Assert.assertTrue(usernames.contains(this.secondUser.getUsername()));
    }

    /**
     * Creates and saves the first test user.
     * 
     * @return Newly created first test user
     */
    private User createFirstTestUser() {
        User user = TEST_UTILS.createDefaultTestUser();
        this.updateTestUser(user, FIRST_USER_DEFAULT_AUTHORITIES);
        return user;
    }

    /**
     * Creates and saves the second test user.
     * 
     * @return Newly created second test user
     */
    private User createSecondTestUser() {
        User user = TEST_UTILS.createSecondDefaultTestUser();
        this.updateTestUser(user, SECOND_USER_DEFAULT_AUTHORITIES);
        return user;
    }

    /**
     * Creates and saves the third default test user.
     * 
     * @return Newly created user
     */
    private User createThirdDefaultTestUser() {
        return TEST_UTILS.createDefaultTestUser(THIRD_USER_DEFAULT_USERNAME, THIRD_USER_DEFAULT_EMAIL,
                THIRD_USER_DEFAULT_AUTHORITIES);
    }

    /**
     * Assigns the given authorities to the given user and persists these changes.
     * 
     * @param desiredAuthorities
     *            Authorities which are to be assigned to the user.
     */
    private void updateTestUser(User user, AuthorityType[] desiredAuthorities) {
        String username = user.getUsername();
        Collection<GrantedAuthority> userAuthorities = user.getAuthorities();
        for (AuthorityType authorityType : desiredAuthorities) {
            userAuthorities.add(new Authority(username, authorityType));
        }

        TEST_UTILS.getUserDao().update(user);
    }

    /**
     * Gets an array of usernames of the given users.
     */
    private List<String> getUsernames(List<User> users) {
        List<String> usernames = new ArrayList<String>(users.size());
        for (User user : users) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }
}
