package com.svnavigatoru600.web.forum.threads;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum PageViewsEnum {

    // @formatter:off
    //       viewName
    LIST    ("listThreads"),
    NEW     ("newThread"),
    EDIT    ("editThread"),
    ;
    // @formatter:on

    private final String viewName;

    private PageViewsEnum(final String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

}
