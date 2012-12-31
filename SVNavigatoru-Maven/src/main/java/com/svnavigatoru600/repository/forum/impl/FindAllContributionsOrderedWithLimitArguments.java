package com.svnavigatoru600.repository.forum.impl;

import com.svnavigatoru600.service.util.OrderType;

/**
 * Encapsulates arguments of the
 * {@link com.svnavigatoru600.repository.forum.ContributionDao#findAllOrdered(ContributionField, OrderType, int)
 * findAllOrdered} method in order to pass just one argument to the method. It is the requirement of MyBatis.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class FindAllContributionsOrderedWithLimitArguments extends
        AbstractFindAllContributionsOrderedArguments {

    /**
     * The maximal allowed number of returned records. If the result has more records, only the first
     * {@link maxResultSize} are returned.
     * <p>
     * NOT USED (IMPLEMENTED) YET
     */
    private final int maxResultSize;

    public FindAllContributionsOrderedWithLimitArguments(ContributionField sortField,
            OrderType sortDirection, int maxResultSize) {
        super(sortField, sortDirection);
        this.maxResultSize = maxResultSize;
    }

    public int getMaxResultSize() {
        return this.maxResultSize;
    }
}
