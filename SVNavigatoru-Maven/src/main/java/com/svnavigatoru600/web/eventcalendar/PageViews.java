package com.svnavigatoru600.web.eventcalendar;

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
