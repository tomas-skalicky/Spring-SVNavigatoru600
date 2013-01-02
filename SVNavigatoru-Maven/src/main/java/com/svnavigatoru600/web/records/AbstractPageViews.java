package com.svnavigatoru600.web.records;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;

/**
 * Abstraction which is common for all controllers of the {@link AbstractDocumentRecord} s. In fact, this
 * class is an enumeration of all views (concerning MVC) available in the given context.
 */
public abstract class AbstractPageViews {

    private final String list;
    private final String neww;
    private final String edit;

    /**
     * Constructor.
     */
    public AbstractPageViews(String listView, String newView, String editView) {
        this.list = listView;
        this.neww = newView;
        this.edit = editView;
    }

    /**
     * Trivial getter
     */
    public String getList() {
        return this.list;
    }

    /**
     * Trivial getter
     */
    public String getNeww() {
        return this.neww;
    }

    /**
     * Trivial getter
     */
    public String getEdit() {
        return this.edit;
    }
}
