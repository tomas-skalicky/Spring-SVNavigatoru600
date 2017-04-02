package com.svnavigatoru600.web.forum.contributions;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum PageViewsEnum {

    // @formatter:off
    //       viewName
    LIST    ("listContributions"),
    NEW     ("newContribution"),
    EDIT    ("editContribution"),
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
