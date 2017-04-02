package com.svnavigatoru600.repository.news.impl;

import com.svnavigatoru600.service.util.OrderTypeEnum;

/**
 * Encapsulates arguments of the {@link com.svnavigatoru600.repository.NewsDao#findAllOrdered findAllOrdered} method in
 * order to pass just one argument to the method. It is the requirement of MyBatis.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class FindAllOrderedArguments {

    /**
     * The field of the {@link com.svnavigatoru600.domain.News News} class according which the result records will be
     * sorted.
     */
    private final NewsFieldEnum sortField;
    /**
     * The direction of sorting.
     */
    private final OrderTypeEnum sortDirection;

    public FindAllOrderedArguments(NewsFieldEnum sortField, OrderTypeEnum sortDirection) {
        this.sortField = sortField;
        this.sortDirection = sortDirection;
    }

    public NewsFieldEnum getSortField() {
        return sortField;
    }

    public OrderTypeEnum getSortDirection() {
        return sortDirection;
    }

}
