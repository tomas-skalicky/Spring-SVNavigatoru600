package com.svnavigatoru600.web.news;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.News;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class GoToEditFormResponse extends AbstractGoToFormResponse {

    private static final String FORM_ACTION_MIDDLE = "existujici/";
    private static final String SUBMIT_BUTTON_TITLE_CODE = "edit.save-changes";

    public GoToEditFormResponse(News news, MessageSource messageSource, HttpServletRequest request) {
        super(news);
        this.setFormAction(request);
        super.setLocalizedTitleOfSubmit(SUBMIT_BUTTON_TITLE_CODE, messageSource, request);
    }

    private void setFormAction(HttpServletRequest request) {
        StringBuffer endingBuilder = new StringBuffer(FORM_ACTION_MIDDLE);
        endingBuilder.append(this.news.getId());
        endingBuilder.append("/");
        super.setFormAction(endingBuilder.toString(), request);
    }
}
