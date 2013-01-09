package com.svnavigatoru600.web.news;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.viewmodel.SendNotification;

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
     * Checkbox via which the user can say whether to notify the others of a certain news, or not.
     */
    private SendNotification sendNotification = null;
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

    protected void setSendNotification(SendNotification sendNotification) {
        this.sendNotification = sendNotification;
    }

    protected void setFormAction(String formActionEnding, HttpServletRequest request) {
        this.formAction = new StringBuffer(request.getContextPath()).append(SECTION_URL)
                .append(formActionEnding).toString();
    }

    protected void setLocalizedTitleOfSubmit(String titleCode, MessageSource messageSource,
            HttpServletRequest request) {
        this.localizedTitleOfSubmit = Localization.findLocaleMessage(messageSource, request, titleCode);
    }

    public News getNews() {
        return this.news;
    }

    public SendNotification getSendNotification() {
        return this.sendNotification;
    }

    public String getFormAction() {
        return this.formAction;
    }

    public String getLocalizedTitleOfSubmit() {
        return this.localizedTitleOfSubmit;
    }

    @Override
    public String toString() {
        return new StringBuilder("[news=").append(this.news).append(", sendNotification=")
                .append(this.sendNotification).append(", formAction=").append(this.formAction)
                .append(", localizedTitleOfSubmit=").append(this.localizedTitleOfSubmit).append("]")
                .toString();
    }
}
