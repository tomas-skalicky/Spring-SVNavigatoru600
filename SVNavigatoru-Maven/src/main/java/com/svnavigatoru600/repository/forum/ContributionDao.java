package com.svnavigatoru600.repository.forum;

import java.util.List;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.service.util.OrderType;

public interface ContributionDao {

    /**
     * Returns a {@link Contribution} stored in the repository which has the given <code>ID</code>.
     */
    public Contribution findById(int contributionId);

    /**
     * Returns all {@link Contribution}s stored in the repository which belong to the {@link Thread} with the
     * given <code>threadId</code>.
     */
    public List<Contribution> find(int threadId);

    /**
     * Returns all {@link Contribution}s stored in the repository arranged according to their values of the
     * <code>attribute</code> in the given <code>order</code>. Moreover, returns only the first <code>x</code>
     * {@link Contribution}s. The <code>x</code> is the given <code>count</code> .
     */
    public List<Contribution> findOrdered(String attribute, OrderType order, int count);

    /**
     * Returns all {@link Contribution}s stored in the repository arranged according to their values of the
     * <code>attribute</code> in the given <code>order</code>. Moreover, the resulting {@link Contribution}s
     * are only those which are parts of the {@link Thread} with the given <code>threadId</code>.
     */
    public List<Contribution> findOrdered(int threadId, String attribute, OrderType order);

    /**
     * Updates the given <code>contribution</code> in the repository. The old version of the
     * <code>contribution</code> should be already stored there.
     */
    public void update(Contribution contribution);

    /**
     * Stores the given <code>contribution</code> to the repository.
     * 
     * @return the generated identifier
     */
    public int save(Contribution contribution);

    /**
     * Deletes the given <code>contribution</code> from the repository.
     */
    public void delete(Contribution contribution);
}
