package svnavigatoru.web.news;

import svnavigatoru.service.news.NewEditNews;

public class EditNewsResponse extends NewEditNewsResponse {

	public EditNewsResponse(NewEditNews command) {
		super(command);
	}
}
