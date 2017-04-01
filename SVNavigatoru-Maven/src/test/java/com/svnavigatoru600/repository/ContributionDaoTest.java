package com.svnavigatoru600.repository;

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
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.impl.ContributionField;
import com.svnavigatoru600.service.util.OrderType;
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
    private Thread defaultThread = null;

    @Before
    public void createDefaultAuthorAndThread() {
        this.defaultAuthor = TEST_UTILS.createDefaultTestUser();
        this.defaultThread = createDefaultTestThread();
    }

    @Test
    public void testSaveFindById() throws Exception {
        ContributionDao contributionDao = TEST_UTILS.getContributionDao();

        // INSERT
        int contributionId = createDefaultTestContribution();

        // SELECT ONE
        Contribution contribution = contributionDao.findById(contributionId);
        Assert.assertTrue(contribution.getId() >= 1);
        Assert.assertEquals(contributionId, contribution.getId());
        Assert.assertEquals(this.defaultThread.getId(), contribution.getThread().getId());
        Assert.assertEquals(RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT, contribution.getText());
        Assert.assertEquals(this.defaultAuthor.getUsername(), contribution.getAuthor().getUsername());
        Assert.assertTrue(new Date().after(contribution.getCreationTime()));
        Assert.assertTrue(new Date().after(contribution.getLastSaveTime()));
        Assert.assertEquals(contribution.getCreationTime(), contribution.getLastSaveTime());
    }

    @Test
    public void testFindAll() throws Exception {
        ContributionDao contributionDao = TEST_UTILS.getContributionDao();

        // THREE INSERTS
        int firstContributionId = createDefaultTestContribution();
        int secondContributionId = createDefaultTestContribution();
        @SuppressWarnings("unused")
        int thirdContributionId = TEST_UTILS.createTestContribution(createDefaultTestThread(),
                RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT, this.defaultAuthor);

        // SELECT ALL
        List<Contribution> foundContributions = contributionDao.findAll(this.defaultThread.getId());
        int expectedFoundContributionCount = 2;
        Assert.assertEquals(expectedFoundContributionCount, foundContributions.size());
        Assert.assertEquals(firstContributionId, foundContributions.get(0).getId());
        Assert.assertEquals(secondContributionId, foundContributions.get(1).getId());
    }

    @Test
    public void testFindAllOrdered() throws Exception {
        ContributionDao contributionDao = TEST_UTILS.getContributionDao();

        // THREE INSERTS
        @SuppressWarnings("unused")
        int firstContributionId = createDefaultTestContribution();
        int secondContributionId = createDefaultTestContribution();
        int thirdContributionId = TEST_UTILS.createTestContribution(createDefaultTestThread(),
                RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT, this.defaultAuthor);

        // SELECT ALL
        int maxResultSize = 2;
        List<Contribution> foundContributions = contributionDao.findAllOrdered(ContributionField.creationTime,
                OrderType.DESCENDING, maxResultSize);
        Assert.assertEquals(maxResultSize, foundContributions.size());
        Assert.assertEquals(thirdContributionId, foundContributions.get(0).getId());
        Assert.assertEquals(secondContributionId, foundContributions.get(1).getId());
    }

    @Test
    public void testFindAllOrderedWithThreadFilter() throws Exception {
        ContributionDao contributionDao = TEST_UTILS.getContributionDao();

        // THREE INSERTS
        int firstContributionId = createDefaultTestContribution();
        @SuppressWarnings("unused")
        int secondContributionId = TEST_UTILS.createTestContribution(createDefaultTestThread(),
                RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT, this.defaultAuthor);
        int thirdContributionId = createDefaultTestContribution();

        // SELECT ALL
        List<Contribution> foundContributions = contributionDao.findAllOrdered(this.defaultThread.getId(),
                ContributionField.lastSaveTime, OrderType.ASCENDING);
        int expectedFoundContributionCount = 2;
        Assert.assertEquals(expectedFoundContributionCount, foundContributions.size());
        Assert.assertEquals(firstContributionId, foundContributions.get(0).getId());
        Assert.assertEquals(thirdContributionId, foundContributions.get(1).getId());
    }

    @Test
    public void testUpdate() throws Exception {
        ContributionDao contributionDao = TEST_UTILS.getContributionDao();

        // INSERT & SELECT ONE
        int contributionId = createDefaultTestContribution();
        Contribution contribution = contributionDao.findById(contributionId);

        // UPDATE
        contribution.setThread(createDefaultTestThread());
        contribution.setText(EDITED_CONTRIBUTION_TEXT);
        contributionDao.update(contribution);

        // SELECT ONE
        contribution = contributionDao.findById(contribution.getId());
        Assert.assertEquals(contributionId, contribution.getId());
        Assert.assertNotSame(this.defaultThread.getId(), contribution.getThread().getId());
        Assert.assertEquals(EDITED_CONTRIBUTION_TEXT, contribution.getText());
        Assert.assertTrue(contribution.getLastSaveTime().after(contribution.getCreationTime()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testDelete() throws Exception {
        ContributionDao contributionDao = TEST_UTILS.getContributionDao();

        // INSERT & SELECT ONE
        int contributionId = createDefaultTestContribution();
        Contribution contribution = contributionDao.findById(contributionId);

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
        return TEST_UTILS.createTestContribution(this.defaultThread, RepositoryTestUtils.CONTRIBUTION_DEFAULT_TEXT,
                this.defaultAuthor);
    }

    /**
     * Creates and saves a default test thread.
     * 
     * @return Newly created thread
     */
    Thread createDefaultTestThread() {
        int threadId = createDefaultTestThreadAndGetId();
        return TEST_UTILS.getThreadDao().findById(threadId);
    }

    /**
     * Creates and saves a default test thread.
     * 
     * @return ID of the newly created thread
     */
    private int createDefaultTestThreadAndGetId() {
        return TEST_UTILS.createTestThread(RepositoryTestUtils.THREAD_DEFAULT_NAME, this.defaultAuthor,
                RepositoryTestUtils.THREAD_DEFAULT_CONTRIBUTIONS);
    }
}
