package svnavigatoru.web.news;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import svnavigatoru.domain.News;
import svnavigatoru.service.news.NewEditNews;
import svnavigatoru.service.util.DateUtils;
import svnavigatoru.service.util.Localization;
import svnavigatoru.service.util.UserUtils;

public class NewNewsResponse extends NewEditNewsResponse {

	/**
	 * All variables intended for visualisation of the new news in the list of news.
	 */
	private String localizedMonth = null;
	private String localizedDay = null;
	private boolean loggedUserCanEditNews = false;
	private String localizedDeleteQuestion = null;
	private String editUrlBeginning = null;

	public NewNewsResponse(NewEditNews command) {
		super(command);
	}

	public void setSuccess(MessageSource messageSource, HttpServletRequest request) {
		super.setSuccess();
		this.initVariables(messageSource, request);
	}

	void initVariables(MessageSource messageSource, HttpServletRequest request) {
		this.initDateVariables(request);
		this.initUserVariables();
		this.initMessageVariables(messageSource, request);
		this.initEditUrlVariables(request);
	}

	private void initDateVariables(HttpServletRequest request) {
		Locale locale = Localization.getLocale(request);

		News news = command.getNews();
		Date creationTime = news.getCreationTime();
		this.localizedMonth = DateUtils.format(creationTime, DateUtils.LONG_MONTH_FORMATS.get(locale), locale);
		this.localizedDay = DateUtils.format(creationTime, DateUtils.SHORT_DAY_FORMATS.get(locale), locale);
	}

	private void initUserVariables() {
		this.loggedUserCanEditNews = UserUtils.getLoggedUser().canEditNews();
	}

	private void initMessageVariables(MessageSource messageSource, HttpServletRequest request) {
		News news = command.getNews();
		this.localizedDeleteQuestion = ListNewsController.getLocalizedDeleteQuestion(news, messageSource, request);
	}

	private void initEditUrlVariables(HttpServletRequest request) {
		StringBuffer urlBuilder = new StringBuffer(request.getContextPath());
		urlBuilder.append(NewsController.BASE_URL);
		urlBuilder.append("existujici/");
		this.editUrlBeginning = urlBuilder.toString();
	}

	public String getLocalizedMonth() {
		return localizedMonth;
	}

	public String getLocalizedDay() {
		return localizedDay;
	}

	public boolean isLoggedUserCanEditNews() {
		return loggedUserCanEditNews;
	}

	public String getLocalizedDeleteQuestion() {
		return localizedDeleteQuestion;
	}

	public String getEditUrlBeginning() {
		return editUrlBeginning;
	}
}
