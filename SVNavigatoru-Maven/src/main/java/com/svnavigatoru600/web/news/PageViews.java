package com.svnavigatoru600.web.news;

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
