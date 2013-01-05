package com.svnavigatoru600.repository;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.test.category.PersistenceTests;

/**
 * Tests methods of the {@link Thread} interface.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public class ThreadDaoTest extends AbstractRepositoryTest {

    /**
     * Name of the edited test thread.
     */
    private static final String EDITED_THREAD_NAME = "thread name 2";
    /**
     * Default username of the second test user.
     */
    private static final String SECOND_USER_DEFAULT_USERNAME = "username 2";
    /**
     * Default email of the second test user.
     */
    private static final String SECOND_USER_DEFAULT_EMAIL = "email 2";
    /**
     * Default authorities of the second test user.
     */
    static final Set<GrantedAuthority> SECOND_USER_DEFAULT_AUTHORITIES;

    static {
        SECOND_USER_DEFAULT_AUTHORITIES = new HashSet<GrantedAuthority>();
        SECOND_USER_DEFAULT_AUTHORITIES.add(new Authority(SECOND_USER_DEFAULT_USERNAME,
                AuthorityType.ROLE_REGISTERED_USER.name()));
    }

    /**
     * Default test author of threads and contributions.
     */
    private User defaultAuthor = null;

    @Before
    public void createDefaultAuthor() {
        this.defaultAuthor = TEST_UTILS.createDefaultTestUser();
    }

    @Test
    public void testCreateRetrieve() throws Exception {
        ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // INSERT
        int threadId = this.createDefaultTestThread();

        // SELECT ONE
        Thread thread = threadDao.findById(threadId);
        Assert.assertTrue(thread.getId() >= 1);
        Assert.assertEquals(threadId, thread.getId());
        Assert.assertEquals(RepositoryTestUtils.THREAD_DEFAULT_NAME, thread.getName());
        Assert.assertTrue(new Date().after(thread.getCreationTime()));
        Assert.assertEquals(this.defaultAuthor.getUsername(), thread.getAuthor().getUsername());
        int expectedContributionCount = 0;
        Assert.assertEquals(expectedContributionCount, thread.getContributions().size());
    }

    @Test
    public void testFindByIdWithContribution() throws Exception {
        ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // INSERT
        Contribution contribution = new Contribution(RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT,
                this.defaultAuthor);
        int threadId = TEST_UTILS.createTestThread(RepositoryTestUtils.THREAD_DEFAULT_NAME,
                this.defaultAuthor, Arrays.asList(contribution));

        // SELECT ONE
        Thread thread = threadDao.findById(threadId);
        int expectedContributionCount = 1;
        Assert.assertEquals(expectedContributionCount, thread.getContributions().size());
        int expectedContributionId = TEST_UTILS.getContributionDao().findAll(threadId).get(0).getId();
        int actualContributionId = thread.getContributions().get(0).getId();
        Assert.assertEquals(expectedContributionId, actualContributionId);
    }

    @Test
    public void testLoadAll() throws Exception {
        ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // TWO INSERTS
        int firstThreadId = this.createDefaultTestThread();
        int secondThreadId = this.createDefaultTestThread();

        // SELECT ALL
        List<Thread> foundThreads = threadDao.loadAll();
        int expectedFoundThreadCount = 2;
        Assert.assertEquals(expectedFoundThreadCount, foundThreads.size());
        Assert.assertEquals(firstThreadId, foundThreads.get(0).getId());
        Assert.assertEquals(secondThreadId, foundThreads.get(1).getId());
    }

    @Test
    public void testLoadAllWithContribution() throws Exception {
        ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // INSERT
        Contribution contribution = new Contribution(RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT,
                this.defaultAuthor);
        User author = this.defaultAuthor;
        int threadId = TEST_UTILS.createTestThread(RepositoryTestUtils.THREAD_DEFAULT_NAME, author,
                Arrays.asList(contribution));

        // SELECT ALL
        List<Thread> foundThreads = threadDao.loadAll();
        Thread thread = foundThreads.get(0);
        Assert.assertEquals(author.getUsername(), thread.getAuthor().getUsername());
        int expectedContributionId = TEST_UTILS.getContributionDao().findAll(threadId).get(0).getId();
        int actualContributionId = thread.getContributions().get(0).getId();
        Assert.assertEquals(expectedContributionId, actualContributionId);
    }

    @Test
    public void testUpdate() throws Exception {
        ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // INSERT & SELECT ONE
        int threadId = this.createDefaultTestThread();
        Thread thread = threadDao.findById(threadId);

        // UPDATE
        thread.setName(EDITED_THREAD_NAME);
        String newUsername = SECOND_USER_DEFAULT_USERNAME;
        thread.setAuthor(TEST_UTILS.createDefaultTestUser(newUsername, SECOND_USER_DEFAULT_EMAIL,
                SECOND_USER_DEFAULT_AUTHORITIES));
        threadDao.update(thread);

        // SELECT ONE
        thread = threadDao.findById(thread.getId());
        Assert.assertEquals(threadId, thread.getId());
        Assert.assertEquals(newUsername, thread.getAuthor().getUsername());
        Assert.assertEquals(EDITED_THREAD_NAME, thread.getName());
    }

    /**
     * Creates and saves a default test thread.
     * 
     * @return ID of the newly created thread
     */
    private int createDefaultTestThread() {
        return TEST_UTILS.createTestThread(RepositoryTestUtils.THREAD_DEFAULT_NAME, this.defaultAuthor,
                RepositoryTestUtils.THREAD_DEFAULT_CONTRIBUTIONS);
    }
}
