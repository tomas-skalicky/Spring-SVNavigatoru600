package com.svnavigatoru600.web.news;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum PageViews {

    LIST("listNews"), NEW("newNews"), EDIT("editNews");

    private String viewName;

    public String getViewName() {
        return this.viewName;
    }

    private PageViews(String viewName) {
        this.viewName = viewName;
    }
}
