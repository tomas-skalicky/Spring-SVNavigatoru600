package com.svnavigatoru600.web.forum.threads;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum PageViews {

    LIST("listThreads"), NEW("newThread"), EDIT("editThread");

    private String viewName;

    public String getViewName() {
        return this.viewName;
    }

    private PageViews(String viewName) {
        this.viewName = viewName;
    }
}
