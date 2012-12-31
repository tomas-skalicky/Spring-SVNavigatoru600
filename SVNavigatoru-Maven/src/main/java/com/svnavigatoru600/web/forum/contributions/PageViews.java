package com.svnavigatoru600.web.forum.contributions;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum PageViews {

    LIST("listContributions"), NEW("newContribution"), EDIT("editContribution");

    private String viewName;

    public String getViewName() {
        return this.viewName;
    }

    private PageViews(String viewName) {
        this.viewName = viewName;
    }
}
