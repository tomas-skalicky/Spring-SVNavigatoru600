package com.svnavigatoru600.web.records;

/**
 * Abstraction which is common for all controllers of {@link com.svnavigatoru600.domain.records.AbstractDocumentRecord
 * AbstractDocumentRecords}. In fact, this class is an enumeration of all views (concerning MVC) available in the given
 * context.
 */
public abstract class AbstractPageViews {

    private final String list;
    private final String neww;
    private final String edit;

    public AbstractPageViews(final String listView, final String newView, final String editView) {
        list = listView;
        neww = newView;
        edit = editView;
    }

    /**
     * Trivial getter
     */
    public String getList() {
        return list;
    }

    /**
     * Trivial getter
     */
    public String getNeww() {
        return neww;
    }

    /**
     * Trivial getter
     */
    public String getEdit() {
        return edit;
    }
}
