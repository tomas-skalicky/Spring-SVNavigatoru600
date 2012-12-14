package com.svnavigatoru600.web.users.administration;

/**
 * Enumeration of all views (concerning MVC) used in the current package.
 */
enum PageViews {

    LIST("listUsers"), NEW("newUser"), EDIT("editUser");

    private String viewName;

    private PageViews(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return this.viewName;
    }
}
