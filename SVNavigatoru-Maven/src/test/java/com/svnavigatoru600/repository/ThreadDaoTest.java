package com.svnavigatoru600.repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.dao.EmptyResultDataAccessException;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.domain.forum.ForumThread;
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
        defaultAuthor = TEST_UTILS.createDefaultTestUser();
    }

    @Test
    public void testSaveFindById() throws Exception {
        final ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // INSERT
        final int threadId = createDefaultTestThread();

        // SELECT ONE
        final ForumThread thread = threadDao.findById(threadId);
        Assert.assertTrue(thread.getId() >= 1);
        Assert.assertEquals(threadId, thread.getId());
        Assert.assertEquals(RepositoryTestUtils.THREAD_DEFAULT_NAME, thread.getName());
        Assert.assertTrue(new Date().after(thread.getCreationTime()));
        Assert.assertEquals(defaultAuthor.getUsername(), thread.getAuthor().getUsername());
        final int expectedContributionCount = 0;
        Assert.assertEquals(expectedContributionCount, thread.getContributions().size());
    }

    @Test
    public void testFindByIdWithContribution() throws Exception {
        final ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // INSERT
        final int threadId = createDefaultTestThreadWithContribution();

        // SELECT ONE
        final ForumThread thread = threadDao.findById(threadId);
        final int expectedContributionCount = 1;
        Assert.assertEquals(expectedContributionCount, thread.getContributions().size());
        final int expectedContributionId = TEST_UTILS.getContributionDao().findByThreadId(threadId).get(0).getId();
        final int actualContributionId = thread.getContributions().get(0).getId();
        Assert.assertEquals(expectedContributionId, actualContributionId);
    }

    @Test
    public void testLoadAllNoContribution() throws Exception {
        final ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // TWO INSERTS
        final int firstThreadId = createDefaultTestThread();
        final int secondThreadId = createDefaultTestThread();

        // SELECT ALL
        final List<ForumThread> foundThreads = threadDao.loadAll();
        final int expectedFoundThreadCount = 2;
        Assert.assertEquals(expectedFoundThreadCount, foundThreads.size());
        Assert.assertEquals(firstThreadId, foundThreads.get(0).getId());
        Assert.assertEquals(secondThreadId, foundThreads.get(1).getId());
    }

    @Test
    public void testLoadAllWithContribution() throws Exception {
        final ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // INSERT
        final int threadId = createDefaultTestThreadWithContribution();

        // SELECT ALL
        final List<ForumThread> foundThreads = threadDao.loadAll();
        final ForumThread thread = foundThreads.get(0);
        Assert.assertEquals(defaultAuthor.getUsername(), thread.getAuthor().getUsername());
        final int expectedContributionId = TEST_UTILS.getContributionDao().findByThreadId(threadId).get(0).getId();
        final int actualContributionId = thread.getContributions().get(0).getId();
        Assert.assertEquals(expectedContributionId, actualContributionId);
    }

    @Test
    public void testUpdate() throws Exception {
        final ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // INSERT & SELECT ONE
        final int threadId = createDefaultTestThread();
        ForumThread thread = threadDao.findById(threadId);

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
        final ThreadDao threadDao = TEST_UTILS.getThreadDao();

        // INSERT & SELECT ONE
        final int threadId = createDefaultTestThreadWithContribution();
        final ForumThread thread = threadDao.findById(threadId);

        // DELETE
        threadDao.delete(thread);

        // SELECT ONE
        try {
            threadDao.findById(thread.getId());
            Assert.fail("The thread has been found");
        } catch (final EmptyResultDataAccessException ex) {
            // OK since the thread cannot have been found.
            ;
        }

        // SELECT ONE
        // Throws an exception since the contribution cannot be found.
        final int contributionId = thread.getContributions().get(0).getId();
        TEST_UTILS.getContributionDao().findById(contributionId);
    }

    /**
     * Creates and saves a default test thread.
     *
     * @return ID of the newly created thread
     */
    private int createDefaultTestThread() {
        return TEST_UTILS.createTestThread(RepositoryTestUtils.THREAD_DEFAULT_NAME, defaultAuthor,
                RepositoryTestUtils.THREAD_DEFAULT_CONTRIBUTIONS);
    }

    /**
     * Creates and saves a default test thread.
     *
     * @return ID of the newly created thread
     */
    private int createDefaultTestThreadWithContribution() {
        final ForumContribution contribution = getDefaultTestContribution();
        return TEST_UTILS.createTestThread(RepositoryTestUtils.THREAD_DEFAULT_NAME, defaultAuthor,
                Arrays.asList(contribution));
    }

    /**
     * Gets a default test contribution. Does not persists the contribution. Its thread property is not set.
     *
     * @return The default contribution object
     */
    private ForumContribution getDefaultTestContribution() {
        final ForumContribution contribution = new ForumContribution();
        contribution.setText(RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT);
        contribution.setAuthor(defaultAuthor);
        return contribution;
    }
}
