package svnavigatoru.web.news;

import svnavigatoru.service.news.ShowAllNews;

public class ListNewsResponse extends NewsResponse {

	/**
	 * Holds data of the command.
	 */
	private ShowAllNews command = null;

	public ListNewsResponse(ShowAllNews command) {
		this.successful = true;
		this.command = command;
	}

	public ShowAllNews getCommand() {
		return command;
	}
}
