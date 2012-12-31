package com.svnavigatoru600.repository.forum.impl;

import com.svnavigatoru600.service.util.OrderType;

/**
 * Encapsulates arguments of the
 * {@link com.svnavigatoru600.repository.forum.ContributionDao#findAllOrdered(int, ContributionField, OrderType)
 * findAllOrdered} method in order to pass just one argument to the method. It is the requirement of MyBatis.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class FindAllContributionsOrderedWithThreadIdArguments extends
        AbstractFindAllContributionsOrderedArguments {

    /**
     * The ID of {@link com.svnavigatoru600.domain.forum.Thread thread} of which
     * {@link com.svnavigatoru600.domain.forum.Contribution contributions} are considered. The other
     * contributions cannot be a part of the result.
     */
    private final int threadId;

    public FindAllContributionsOrderedWithThreadIdArguments(int threadId, ContributionField sortField,
            OrderType sortDirection) {
        super(sortField, sortDirection);
        this.threadId = threadId;
    }

    public int getThreadId() {
        return this.threadId;
    }
}
