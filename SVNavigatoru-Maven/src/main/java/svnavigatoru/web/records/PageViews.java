package svnavigatoru.web.records;

import svnavigatoru.domain.records.DocumentRecord;

/**
 * Abstraction which is common for all controllers of the {@link DocumentRecord}
 * s. In fact, this class is an enumeration of all views (concerning MVC)
 * available in the given context.
 */
public abstract class PageViews {

	public final String LIST;
	public final String NEW;
	public final String EDIT;

	/**
	 * Constructor.
	 */
	public PageViews(String listView, String newView, String editView) {
		this.LIST = listView;
		this.NEW = newView;
		this.EDIT = editView;
	}
}
