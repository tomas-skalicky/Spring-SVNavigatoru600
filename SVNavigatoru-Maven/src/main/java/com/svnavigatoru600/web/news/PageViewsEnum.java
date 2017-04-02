package com.svnavigatoru600.web.news;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum PageViewsEnum {

    // @formatter:off
    //       viewName
    LIST    ("listNews"),
    NEW     ("newNews"),
    EDIT    ("editNews");
    // @formatter:on

    private final String viewName;

    private PageViewsEnum(final String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

}
