package com.svnavigatoru600.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.repository.forum.impl.ContributionField;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.service.util.OrderType;
import com.svnavigatoru600.test.category.PersistenceTests;

/**
 * Tests methods of the {@link ContributionDao} interface.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public class ContributionDaoTest extends AbstractRepositoryTest {

    /**
     * Default name of test thread.
     */
    private static final String THREAD_DEFAULT_NAME = "thread name 1";
    /**
     * Default text of test contribution.
     */
    private static final String CONTRIBUTION_DEFAULT_TEXT = "contribution text 1";
    /**
     * Text of the edited test contribution.
     */
    private static final String EDITED_CONTRIBUTION_TEXT = "contribution text 2";
    /**
     * Default test author of threads and contributions.
     */
    private User defaultAuthor = null;
    /**
     * Default test thread of contributions.
     */
    private Thread defaultThread = null;

    @Before
    public void createUserAndThread() {
        this.defaultAuthor = this.createDefaultTestUser();
        this.defaultThread = this.createDefaultTestThread();
    }

    @Test
    public void testCreateRetrieve() throws Exception {
        ContributionDao contributionDao = this.getContributionDao();

        // INSERT
        int contributionId = this.createDefaultTestContribution(contributionDao);

        // SELECT ONE
        Contribution contribution = contributionDao.findById(contributionId);
        Assert.assertTrue(contribution.getId() >= 1);
        Assert.assertEquals(contributionId, contribution.getId());
        Assert.assertEquals(this.defaultThread.getId(), contribution.getThread().getId());
        Assert.assertEquals(CONTRIBUTION_DEFAULT_TEXT, contribution.getText());
        Assert.assertEquals(this.defaultAuthor.getUsername(), contribution.getAuthor().getUsername());
        Assert.assertTrue(new Date().after(contribution.getCreationTime()));
        Assert.assertTrue(new Date().after(contribution.getLastSaveTime()));
        Assert.assertEquals(contribution.getCreationTime(), contribution.getLastSaveTime());
    }

    @Test
    public void testUpdate() throws Exception {
        ContributionDao contributionDao = this.getContributionDao();

        // INSERT & SELECT ONE
        int contributionId = this.createDefaultTestContribution(contributionDao);
        Contribution contribution = contributionDao.findById(contributionId);

        // UPDATE
        contribution.setThread(this.createDefaultTestThread());
        contribution.setText(EDITED_CONTRIBUTION_TEXT);
        contributionDao.update(contribution);

        // SELECT ONE
        contribution = contributionDao.findById(contribution.getId());
        Assert.assertTrue(contribution.getId() >= 1);
        Assert.assertEquals(contributionId, contribution.getId());
        Assert.assertNotNull(contribution.getThread());
        Assert.assertNotSame(this.defaultThread.getId(), contribution.getThread().getId());
        Assert.assertEquals(EDITED_CONTRIBUTION_TEXT, contribution.getText());
        Assert.assertTrue(contribution.getLastSaveTime().after(contribution.getCreationTime()));
    }

    @Test
    public void testDelete() throws Exception {
        ContributionDao contributionDao = this.getContributionDao();

        // INSERT & SELECT ONE
        int contributionId = this.createDefaultTestContribution(contributionDao);
        Contribution contribution = contributionDao.findById(contributionId);

        // DELETE
        contributionDao.delete(contribution);

        // SELECT ONE
        try {
            contribution = contributionDao.findById(contribution.getId());
            Assert.fail("The contribution has been found");
        } catch (EmptyResultDataAccessException ex) {
            // OK since the contribution cannot have been found.
            ;
        }
    }

    @Test
    public void testFindAll() throws Exception {
        ContributionDao contributionDao = this.getContributionDao();

        // THREE INSERTS
        int firstContributionId = this.createDefaultTestContribution(contributionDao);
        int secondContributionId = this.createDefaultTestContribution(contributionDao);
        @SuppressWarnings("unused")
        int thirdContributionId = this.createTestContribution(this.createDefaultTestThread(),
                CONTRIBUTION_DEFAULT_TEXT, this.defaultAuthor, contributionDao);

        // SELECT ALL
        List<Contribution> foundContributions = contributionDao.findAll(this.defaultThread.getId());
        int expectedFoundContributionCount = 2;
        Assert.assertEquals(expectedFoundContributionCount, foundContributions.size());
        Assert.assertEquals(firstContributionId, foundContributions.get(0).getId());
        Assert.assertEquals(secondContributionId, foundContributions.get(1).getId());
    }

    @Test
    public void testFindAllOrdered() throws Exception {
        ContributionDao contributionDao = this.getContributionDao();

        // THREE INSERTS
        @SuppressWarnings("unused")
        int firstContributionId = this.createDefaultTestContribution(contributionDao);
        int secondContributionId = this.createDefaultTestContribution(contributionDao);
        int thirdContributionId = this.createTestContribution(this.createDefaultTestThread(),
                CONTRIBUTION_DEFAULT_TEXT, this.defaultAuthor, contributionDao);

        // SELECT ALL
        int maxResultSize = 2;
        List<Contribution> foundContributions = contributionDao.findAllOrdered(
                ContributionField.creationTime, OrderType.DESCENDING, maxResultSize);
        Assert.assertEquals(maxResultSize, foundContributions.size());
        Assert.assertEquals(thirdContributionId, foundContributions.get(0).getId());
        Assert.assertEquals(secondContributionId, foundContributions.get(1).getId());
    }

    @Test
    public void testFindAllOrderedWithThreadFilter() throws Exception {
        ContributionDao contributionDao = this.getContributionDao();

        // THREE INSERTS
        int firstContributionId = this.createDefaultTestContribution(contributionDao);
        @SuppressWarnings("unused")
        int secondContributionId = this.createTestContribution(this.createDefaultTestThread(),
                CONTRIBUTION_DEFAULT_TEXT, this.defaultAuthor, contributionDao);
        int thirdContributionId = this.createDefaultTestContribution(contributionDao);

        // SELECT ALL
        List<Contribution> foundContributions = contributionDao.findAllOrdered(this.defaultThread.getId(),
                ContributionField.lastSaveTime, OrderType.ASCENDING);
        int expectedFoundContributionCount = 2;
        Assert.assertEquals(expectedFoundContributionCount, foundContributions.size());
        Assert.assertEquals(firstContributionId, foundContributions.get(0).getId());
        Assert.assertEquals(thirdContributionId, foundContributions.get(1).getId());
    }

    /**
     * Gets a {@link ContributionDao} from an application context.
     */
    private ContributionDao getContributionDao() {
        return APPLICATION_CONTEXT.getBean(ContributionDao.class);
    }

    /**
     * Creates and saves a default test contribution.
     * 
     * @return ID of the newly created contribution
     */
    private int createDefaultTestContribution(ContributionDao contributionDao) {
        return this.createTestContribution(this.defaultThread, CONTRIBUTION_DEFAULT_TEXT, this.defaultAuthor,
                contributionDao);
    }

    /**
     * Creates and saves a test contribution.
     * 
     * @return ID of the newly created contribution
     */
    private int createTestContribution(Thread thread, String text, User author,
            ContributionDao contributionDao) {
        Contribution contribution = new Contribution();
        contribution.setThread(thread);
        contribution.setText(text);
        contribution.setAuthor(author);
        return contributionDao.save(contribution);
    }

    /**
     * Gets a {@link ThreadDao} from an application context.
     */
    private ThreadDao getThreadDao() {
        return APPLICATION_CONTEXT.getBean(ThreadDao.class);
    }

    /**
     * Creates and saves a default test thread.
     * 
     * @return Newly created thread
     */
    private Thread createDefaultTestThread() {
        ThreadDao threadDao = this.getThreadDao();
        int threadId = this.createDefaultTestThread(threadDao);
        return threadDao.findById(threadId);
    }

    /**
     * Creates and saves a default test thread.
     * 
     * @return ID of the newly created thread
     */
    private int createDefaultTestThread(ThreadDao threadDao) {
        return this.createTestThread(THREAD_DEFAULT_NAME, this.defaultAuthor, threadDao);
    }

    /**
     * Creates and saves a test thread.
     * 
     * @return ID of the newly created thread
     */
    private int createTestThread(String name, User author, ThreadDao threadDao) {
        Thread thread = new Thread();
        thread.setName(name);
        thread.setAuthor(author);
        thread.setContributions(new ArrayList<Contribution>());
        return threadDao.save(thread);
    }

    /**
     * Gets a {@link UserDao} from an application context.
     */
    private UserDao getUserDao() {
        return APPLICATION_CONTEXT.getBean(UserDao.class);
    }

    /**
     * Creates and saves a default test user.
     * 
     * @return ID of the newly created user
     */
    private User createDefaultTestUser() {
        UserDao userDao = this.getUserDao();
        String username = "username 1";
        String password = "password 1";
        boolean enabled = true;
        String firstName = "first name 1";
        String lastName = "last name 1";
        String email = "email 1";
        String phone = "phone 1";
        boolean isTestUser = false;
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new Authority(username, AuthorityType.ROLE_REGISTERED_USER.name()));
        this.createTestUser(username, password, enabled, firstName, lastName, email, phone, isTestUser,
                authorities, userDao);
        return userDao.findByUsername(username);
    }

    /**
     * Creates and saves a test user.
     * 
     * @return ID of the newly created user
     */
    private void createTestUser(String username, String password, boolean enabled, String firstName,
            String lastName, String email, String phone, boolean isTestUser,
            Set<GrantedAuthority> authorities, UserDao userDao) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(enabled);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setTestUser(isTestUser);
        user.setAuthorities(authorities);
        userDao.save(user);
    }
}
