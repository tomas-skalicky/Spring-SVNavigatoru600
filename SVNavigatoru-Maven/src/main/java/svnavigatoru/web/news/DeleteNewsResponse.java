package svnavigatoru.web.news;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import svnavigatoru.service.util.Localization;

public class DeleteNewsResponse extends NewsResponse {

	/**
	 * If the command has not been successful, the error populates the error field.
	 */
	private String error = null;

	/**
	 * Sets up everything in a way that the processing of the command has failed.
	 */
	public void setFail(String errorCode, MessageSource messageSource, HttpServletRequest request) {
		this.setFail();
		this.error = Localization.findLocaleMessage(messageSource, request, errorCode);
	}

	public String getError() {
		return error;
	}
}
