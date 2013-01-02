package com.svnavigatoru600.web.news;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.service.util.Localization;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractGoToFormResponse extends AbstractNewsResponse {

    private static final String SECTION_URL = "/novinky/";
    /**
     * News which is to be visualised in the form.
     */
    private News news = null;
    /**
     * Value of the "action" attribute of the form.
     */
    private String formAction = null;
    /**
     * Localised title of the submit button.
     */
    private String localizedTitleOfSubmit = null;

    public AbstractGoToFormResponse(News news) {
        this.setSuccess();
        this.news = news;
    }

    protected void setFormAction(String formActionEnding, HttpServletRequest request) {
        StringBuffer urlBuilder = new StringBuffer(request.getContextPath());
        urlBuilder.append(SECTION_URL);
        urlBuilder.append(formActionEnding);
        this.formAction = urlBuilder.toString();
    }

    protected void setLocalizedTitleOfSubmit(String titleCode, MessageSource messageSource,
            HttpServletRequest request) {
        this.localizedTitleOfSubmit = Localization.findLocaleMessage(messageSource, request, titleCode);
    }

    public News getNews() {
        return this.news;
    }

    public String getFormAction() {
        return this.formAction;
    }

    public String getLocalizedTitleOfSubmit() {
        return this.localizedTitleOfSubmit;
    }
}
