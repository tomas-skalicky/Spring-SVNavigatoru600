package com.svnavigatoru600.repository;

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
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.impl.ContributionFieldEnum;
import com.svnavigatoru600.service.util.OrderTypeEnum;
import com.svnavigatoru600.test.category.PersistenceTests;

/**
 * Tests methods of the {@link ContributionDao} interface.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public final class ContributionDaoTest extends AbstractRepositoryTest {

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
    private ForumThread defaultThread = null;

    @Before
    public void createDefaultAuthorAndThread() {
        defaultAuthor = TEST_UTILS.createDefaultTestUser();
        defaultThread = createDefaultTestThread();
    }

    @Test
    public void testSaveFindById() throws Exception {
        final ContributionDao contributionDao = TEST_UTILS.getContributionDao();

        // INSERT
        final int contributionId = createDefaultTestContribution();

        // SELECT ONE
        final ForumContribution contribution = contributionDao.findById(contributionId);
        Assert.assertTrue(contribution.getId() >= 1);
        Assert.assertEquals(contributionId, contribution.getId());
        Assert.assertEquals(defaultThread.getId(), contribution.getThread().getId());
        Assert.assertEquals(RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT, contribution.getText());
        Assert.assertEquals(defaultAuthor.getUsername(), contribution.getAuthor().getUsername());
        Assert.assertTrue(new Date().after(contribution.getCreationTime()));
        Assert.assertTrue(new Date().after(contribution.getLastSaveTime()));
        Assert.assertEquals(contribution.getCreationTime(), contribution.getLastSaveTime());
    }

    @Test
    public void testFindAll() throws Exception {
        final ContributionDao contributionDao = TEST_UTILS.getContributionDao();

        // THREE INSERTS
        final int firstContributionId = createDefaultTestContribution();
        final int secondContributionId = createDefaultTestContribution();
        @SuppressWarnings("unused")
        final
        int thirdContributionId = TEST_UTILS.createTestContribution(createDefaultTestThread(),
                RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT, defaultAuthor);

        // SELECT ALL
        final List<ForumContribution> foundContributions = contributionDao.findByThreadId(defaultThread.getId());
        final int expectedFoundContributionCount = 2;
        Assert.assertEquals(expectedFoundContributionCount, foundContributions.size());
        Assert.assertEquals(firstContributionId, foundContributions.get(0).getId());
        Assert.assertEquals(secondContributionId, foundContributions.get(1).getId());
    }

    @Test
    public void testFindAllOrdered() throws Exception {
        final ContributionDao contributionDao = TEST_UTILS.getContributionDao();

        // THREE INSERTS
        @SuppressWarnings("unused")
        final
        int firstContributionId = createDefaultTestContribution();
        final int secondContributionId = createDefaultTestContribution();
        final int thirdContributionId = TEST_UTILS.createTestContribution(createDefaultTestThread(),
                RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT, defaultAuthor);

        // SELECT ALL
        final int maxResultSize = 2;
        final List<ForumContribution> foundContributions = contributionDao.findAllOrdered(ContributionFieldEnum.CREATION_TIME,
                OrderTypeEnum.DESCENDING, maxResultSize);
        Assert.assertEquals(maxResultSize, foundContributions.size());
        Assert.assertEquals(thirdContributionId, foundContributions.get(0).getId());
        Assert.assertEquals(secondContributionId, foundContributions.get(1).getId());
    }

    @Test
    public void testFindAllOrderedWithThreadFilter() throws Exception {
        final ContributionDao contributionDao = TEST_UTILS.getContributionDao();

        // THREE INSERTS
        final int firstContributionId = createDefaultTestContribution();
        @SuppressWarnings("unused")
        final
        int secondContributionId = TEST_UTILS.createTestContribution(createDefaultTestThread(),
                RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT, defaultAuthor);
        final int thirdContributionId = createDefaultTestContribution();

        // SELECT ALL
        final List<ForumContribution> foundContributions = contributionDao.findAllOrdered(defaultThread.getId(),
                ContributionFieldEnum.LAST_SAVE_TIME, OrderTypeEnum.ASCENDING);
        final int expectedFoundContributionCount = 2;
        Assert.assertEquals(expectedFoundContributionCount, foundContributions.size());
        Assert.assertEquals(firstContributionId, foundContributions.get(0).getId());
        Assert.assertEquals(thirdContributionId, foundContributions.get(1).getId());
    }

    @Test
    public void testUpdate() throws Exception {
        final ContributionDao contributionDao = TEST_UTILS.getContributionDao();

        // INSERT & SELECT ONE
        final int contributionId = createDefaultTestContribution();
        ForumContribution contribution = contributionDao.findById(contributionId);

        // UPDATE
        contribution.setThread(createDefaultTestThread());
        contribution.setText(EDITED_CONTRIBUTION_TEXT);
        contributionDao.update(contribution);

        // SELECT ONE
        contribution = contributionDao.findById(contribution.getId());
        Assert.assertEquals(contributionId, contribution.getId());
        Assert.assertNotSame(defaultThread.getId(), contribution.getThread().getId());
        Assert.assertEquals(EDITED_CONTRIBUTION_TEXT, contribution.getText());
        Assert.assertTrue(contribution.getLastSaveTime().after(contribution.getCreationTime()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testDelete() throws Exception {
        final ContributionDao contributionDao = TEST_UTILS.getContributionDao();

        // INSERT & SELECT ONE
        final int contributionId = createDefaultTestContribution();
        final ForumContribution contribution = contributionDao.findById(contributionId);

        // DELETE
        contributionDao.delete(contribution);

        // SELECT ONE
        // Throws an exception since the contribution cannot be found.
        contributionDao.findById(contribution.getId());
    }

    /**
     * Creates and saves a default test contribution.
     *
     * @return ID of the newly created contribution
     */
    private int createDefaultTestContribution() {
        return TEST_UTILS.createTestContribution(defaultThread, RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT,
                defaultAuthor);
    }

    /**
     * Creates and saves a default test thread.
     *
     * @return Newly created thread
     */
    ForumThread createDefaultTestThread() {
        final int threadId = createDefaultTestThreadAndGetId();
        return TEST_UTILS.getThreadDao().findById(threadId);
    }

    /**
     * Creates and saves a default test thread.
     *
     * @return ID of the newly created thread
     */
    private int createDefaultTestThreadAndGetId() {
        return TEST_UTILS.createTestThread(RepositoryTestUtils.THREAD_DEFAULT_NAME, defaultAuthor,
                RepositoryTestUtils.THREAD_DEFAULT_CONTRIBUTIONS);
    }
}
