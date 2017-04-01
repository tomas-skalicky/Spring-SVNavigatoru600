package com.svnavigatoru600.repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.dao.EmptyResultDataAccessException;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.test.category.PersistenceTests;

/**
 * Tests methods of the {@link ThreadDao} interface.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public final class ThreadDaoTest extends AbstractRepositoryTest {

    /**
     * Name of the edited test thread.
     */
    private static final String EDITED_THREAD_NAME = "thread name 2";
    /**
     * Default test author of threads and contributions.
     */
    private User defaultAuthor = null;

    @Before
    public void createDefaultAuthor() {
        this.defaultAuthor = TEST_UTILS.createDefaultTestUser();
    }

    @Test
    public void testSaveFindById() throws Exception {
        ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // INSERT
        int threadId = createDefaultTestThread();

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
        int threadId = createDefaultTestThreadWithContribution();

        // SELECT ONE
        Thread thread = threadDao.findById(threadId);
        int expectedContributionCount = 1;
        Assert.assertEquals(expectedContributionCount, thread.getContributions().size());
        int expectedContributionId = TEST_UTILS.getContributionDao().findAll(threadId).get(0).getId();
        int actualContributionId = thread.getContributions().get(0).getId();
        Assert.assertEquals(expectedContributionId, actualContributionId);
    }

    @Test
    public void testLoadAllNoContribution() throws Exception {
        ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // TWO INSERTS
        int firstThreadId = createDefaultTestThread();
        int secondThreadId = createDefaultTestThread();

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
        int threadId = createDefaultTestThreadWithContribution();

        // SELECT ALL
        List<Thread> foundThreads = threadDao.loadAll();
        Thread thread = foundThreads.get(0);
        Assert.assertEquals(this.defaultAuthor.getUsername(), thread.getAuthor().getUsername());
        int expectedContributionId = TEST_UTILS.getContributionDao().findAll(threadId).get(0).getId();
        int actualContributionId = thread.getContributions().get(0).getId();
        Assert.assertEquals(expectedContributionId, actualContributionId);
    }

    @Test
    public void testUpdate() throws Exception {
        ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // INSERT & SELECT ONE
        int threadId = createDefaultTestThread();
        Thread thread = threadDao.findById(threadId);

        // UPDATE
        thread.setName(EDITED_THREAD_NAME);
        thread.setAuthor(TEST_UTILS.createSecondDefaultTestUser());
        threadDao.update(thread);

        // SELECT ONE
        thread = threadDao.findById(thread.getId());
        Assert.assertEquals(threadId, thread.getId());
        Assert.assertEquals(RepositoryTestUtils.SECOND_USER_DEFAULT_USERNAME, thread.getAuthor().getUsername());
        Assert.assertEquals(EDITED_THREAD_NAME, thread.getName());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testDelete() throws Exception {
        ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // INSERT & SELECT ONE
        int threadId = createDefaultTestThreadWithContribution();
        Thread thread = threadDao.findById(threadId);

        // DELETE
        threadDao.delete(thread);

        // SELECT ONE
        try {
            threadDao.findById(thread.getId());
            Assert.fail("The thread has been found");
        } catch (EmptyResultDataAccessException ex) {
            // OK since the thread cannot have been found.
            ;
        }

        // SELECT ONE
        // Throws an exception since the contribution cannot be found.
        int contributionId = thread.getContributions().get(0).getId();
        TEST_UTILS.getContributionDao().findById(contributionId);
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

    /**
     * Creates and saves a default test thread.
     * 
     * @return ID of the newly created thread
     */
    private int createDefaultTestThreadWithContribution() {
        Contribution contribution = getDefaultTestContribution();
        return TEST_UTILS.createTestThread(RepositoryTestUtils.THREAD_DEFAULT_NAME, this.defaultAuthor,
                Arrays.asList(contribution));
    }

    /**
     * Gets a default test contribution. Does not persists the contribution. Its thread property is not set.
     * 
     * @return The default contribution object
     */
    private Contribution getDefaultTestContribution() {
        return new Contribution(RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT, this.defaultAuthor);
    }
}
