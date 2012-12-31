package com.svnavigatoru600.repository.news.impl;

import com.svnavigatoru600.service.util.OrderType;

/**
 * Encapsulates arguments of the {@link com.svnavigatoru600.repository.NewsDao#findOrdered findOrdered} method
 * in order to pass just one argument to the method. It is the requirement of MyBatis.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class FindAllOrderedArguments {

    /**
     * The field of the {@link com.svnavigatoru600.domain.News News} class according which the result records
     * will be sorted.
     */
    private final NewsField sortField;
    /**
     * The direction of sorting.
     */
    private final OrderType sortDirection;

    public FindAllOrderedArguments(NewsField sortField, OrderType sortDirection) {
        this.sortField = sortField;
        this.sortDirection = sortDirection;
    }

    public NewsField getSortField() {
        return this.sortField;
    }

    public OrderType getSortDirection() {
        return this.sortDirection;
    }
}
