package com.svnavigatoru600.web.records;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;

/**
 * Abstraction which is common for all controllers of the {@link AbstractDocumentRecord} s. In fact, this class is an
 * enumeration of all views (concerning MVC) available in the given context.
 */
public abstract class AbstractPageViews {

    public final String list;
    public final String neww;
    public final String edit;

    /**
     * Constructor.
     */
    public AbstractPageViews(String listView, String newView, String editView) {
        this.list = listView;
        this.neww = newView;
        this.edit = editView;
    }
}
