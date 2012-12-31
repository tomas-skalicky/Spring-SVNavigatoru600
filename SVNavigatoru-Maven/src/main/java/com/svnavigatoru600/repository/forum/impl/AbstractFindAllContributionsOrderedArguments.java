package com.svnavigatoru600.repository.forum.impl;

import com.svnavigatoru600.service.util.OrderType;

/**
 * Encapsulates common arguments of the {@link com.svnavigatoru600.repository.forum.ContributionDao
 * ContributionDao's} findAllOrdered methods in order to pass just one argument to them. It is the requirement
 * of MyBatis.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
abstract class AbstractFindAllContributionsOrderedArguments {

    /**
     * The field of the {@link com.svnavigatoru600.domain.forum.Contribution Contribution} class according
     * which the result records will be sorted.
     */
    private final ContributionField sortField;
    /**
     * The direction of sorting.
     */
    private final OrderType sortDirection;

    public AbstractFindAllContributionsOrderedArguments(ContributionField sortField, OrderType sortDirection) {
        this.sortField = sortField;
        this.sortDirection = sortDirection;
    }

    public ContributionField getSortField() {
        return this.sortField;
    }

    public OrderType getSortDirection() {
        return this.sortDirection;
    }
}
