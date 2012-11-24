package com.svnavigatoru600.web.news;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.service.util.Localization;


public abstract class GoToFormResponse extends NewsResponse {

	private static final String SECTION_URL = "/novinky/";
	/**
	 * News which is to be visualised in the form.
	 */
	protected News news = null;
	/**
	 * Value of the "action" attribute of the form.
	 */
	protected String formAction = null;
	/**
	 * Localised title of the submit button.
	 */
	protected String localizedTitleOfSubmit = null;

	public GoToFormResponse(News news) {
		this.successful = true;
		this.news = news;
	}

	protected void setFormAction(String formActionEnding, HttpServletRequest request) {
		StringBuffer urlBuilder = new StringBuffer(request.getContextPath());
		urlBuilder.append(SECTION_URL);
		urlBuilder.append(formActionEnding);
		this.formAction = urlBuilder.toString();
	}

	protected void setLocalizedTitleOfSubmit(String titleCode, MessageSource messageSource, HttpServletRequest request) {
		this.localizedTitleOfSubmit = Localization.findLocaleMessage(messageSource, request, titleCode);
	}

	public News getNews() {
		return news;
	}

	public String getFormAction() {
		return formAction;
	}

	public String getLocalizedTitleOfSubmit() {
		return localizedTitleOfSubmit;
	}
}
