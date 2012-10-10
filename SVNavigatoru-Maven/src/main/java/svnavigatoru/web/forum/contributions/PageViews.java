package svnavigatoru.web.forum.contributions;

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
