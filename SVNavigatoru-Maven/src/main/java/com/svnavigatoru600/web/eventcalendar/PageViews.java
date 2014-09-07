package com.svnavigatoru600.web.eventcalendar;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum PageViews {

    LIST("listEvents"), NEW("newEvent"), EDIT("editEvent");

    private String viewName;

    public String getViewName() {
        return this.viewName;
    }

    private PageViews(String viewName) {
        this.viewName = viewName;
    }
}
