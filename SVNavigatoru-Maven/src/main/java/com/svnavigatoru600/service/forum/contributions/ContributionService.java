package com.svnavigatoru600.service.forum.contributions;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.impl.ContributionField;
import com.svnavigatoru600.service.util.OrderType;

/**
 * Provides convenient methods to work with {@link com.svnavigatoru600.domain.forum.Contribution Contribution}
 * objects.
 * <p>
 * For instance: There are methods which determine whether the currently logged-in user has rights to
 * manipulate with a particular {@link com.svnavigatoru600.domain.forum.Contribution Contribution}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class ContributionService {

    /**
     * The object which provides a persistence.
     */
    private final ContributionDao contributionDao;

    /**
     * Constructor.
     */
    @Inject
    public ContributionService(ContributionDao contributionDao) {
        this.contributionDao = contributionDao;
    }

    @PreAuthorize("hasPermission(#contributionId, 'com.svnavigatoru600.domain.forum.Contribution', 'edit')")
    public void canEdit(int contributionId) {
    }

    @PreAuthorize("hasPermission(#contributionId, 'com.svnavigatoru600.domain.forum.Contribution', 'delete')")
    public void canDelete(int contributionId) {
    }

    /**
     * Returns a {@link Contribution} stored in the repository which has the given ID.
     */
    public Contribution findById(int contributionId) {
        return this.contributionDao.findById(contributionId);
    }

    /**
     * Returns all {@link Contribution Contributions} stored in the repository which belong to the specified
     * {@link com.svnavigatoru600.domain.forum.Thread thread}.
     * 
     * @param threadId
     *            The ID of the thread
     */
    public List<Contribution> findAll(int threadId) {
        return this.contributionDao.findAll(threadId);
    }

    /**
     * Returns all {@link Contribution Contributions} stored in the repository arranged according to the given
     * {@link ContributionField sortField} and {@link OrderType sortDirection}.
     * <p>
     * NOT IMPLEMENTED YET: It returns only the first <code>maxResultSize</code> {@link Contribution
     * Contributions}.
     */
    public List<Contribution> findAllOrdered(ContributionField sortField, OrderType sortDirection,
            int maxResultSize) {
        return this.contributionDao.findAllOrdered(sortField, sortDirection, maxResultSize);
    }

    /**
     * Returns all {@link Contribution Contributions} stored in the repository arranged according to the given
     * {@link ContributionField sortField} and {@link OrderType sortDirection}.
     * <p>
     * Moreover, the resulting {@link Contribution Contributions} are only those which belong to the specified
     * {@link com.svnavigatoru600.domain.forum.Thread Thread}.
     * 
     * @param threadId
     *            The ID of the thread
     */
    public List<Contribution> findAllOrdered(int threadId, ContributionField sortField,
            OrderType sortDirection) {
        return this.contributionDao.findAllOrdered(threadId, sortField, sortDirection);
    }

    /**
     * Updates the given {@link Contribution} in the repository. The old version of this contribution should
     * be already stored there.
     */
    public void update(Contribution contribution) {
        this.contributionDao.update(contribution);
    }

    /**
     * Updates properties of the given <code>contributionToUpdate</code> and persists this
     * {@link Contribution} into the repository. The old version of this contribution should be already stored
     * there.
     * 
     * @param contributionToUpdate
     *            The persisted {@link Contribution}
     * @param newContribution
     *            The {@link Contribution} which contains new values of properties of
     *            <code>contributionToUpdate</code>. These values are copied to the persisted contribution.
     */
    public void update(Contribution contributionToUpdate, Contribution newContribution) {
        contributionToUpdate.setText(newContribution.getText());
        this.update(contributionToUpdate);
    }

    /**
     * Stores the given {@link Contribution} to the repository.
     * 
     * @return The ID of the given {@link Contribution} generated by the repository
     */
    public int save(Contribution contribution) {
        return this.contributionDao.save(contribution);
    }

    /**
     * Deletes the given {@link Contribution} from the repository.
     */
    public void delete(Contribution contribution) {
        this.contributionDao.delete(contribution);
    }

    /**
     * Deletes the specified {@link Contribution} from the repository.
     * 
     * @param contributionId
     *            The ID of the contribution
     */
    public void delete(int contributionId) {
        Contribution contribution = this.findById(contributionId);
        this.contributionDao.delete(contribution);
    }
}
