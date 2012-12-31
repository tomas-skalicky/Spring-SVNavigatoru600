package com.svnavigatoru600.repository.forum;

import java.util.List;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.repository.MapperInterface;
import com.svnavigatoru600.repository.forum.impl.ContributionField;
import com.svnavigatoru600.service.util.OrderType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@MapperInterface
public interface ContributionDao {

    /**
     * Returns a {@link Contribution} stored in the repository which has the given <code>ID</code>.
     */
    Contribution findById(int contributionId);

    /**
     * Returns all {@link Contribution Contributions} stored in the repository which belong to the
     * {@link com.svnavigatoru600.domain.forum.Thread thread} with the given <code>threadId</code>.
     */
    List<Contribution> findAll(int threadId);

    /**
     * Returns all {@link Contribution Contributions} stored in the repository arranged according to the given
     * <code>sortField</code> and <code>sortDirection</code>.
     * <p>
     * NOT IMPLEMENTED YET: It returns only the first <code>x</code> {@link Contribution contributions}. The
     * <code>x</code> is stored in the <code>arguments</code> as well.
     */
    List<Contribution> findAllOrdered(ContributionField sortField, OrderType sortDirection, int maxResultSize);

    /**
     * Returns all {@link Contribution Contributions} stored in the repository arranged according to the given
     * <code>sortField</code> and <code>sortDirection</code>.
     * <p>
     * Moreover, the resulting {@link Contribution contributions} are only those which are parts of the
     * {@link com.svnavigatoru600.domain.forum.Thread thread} of which ID is specified in the
     * <code>arguments</code> as well.
     */
    List<Contribution> findAllOrdered(int threadId, ContributionField sortField, OrderType sortDirection);

    /**
     * Updates the given {@link Contribution} in the repository. The old version of the
     * <code>contribution</code> should be already stored there.
     */
    void update(Contribution contribution);

    /**
     * Stores the given {@link Contribution} to the repository.
     * 
     * @return the generated identifier
     */
    int save(Contribution contribution);

    /**
     * Deletes the given {@link Contribution} from the repository.
     */
    void delete(Contribution contribution);
}
