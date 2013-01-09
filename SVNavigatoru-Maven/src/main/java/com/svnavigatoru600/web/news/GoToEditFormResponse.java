package com.svnavigatoru600.web.news;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.viewmodel.SendNotification;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class GoToEditFormResponse extends AbstractGoToFormResponse {

    private static final String FORM_ACTION_MIDDLE = "existujici/";
    private static final String SUBMIT_BUTTON_TITLE_CODE = "edit.save-changes";

    public GoToEditFormResponse(News news, SendNotification sendNotification, MessageSource messageSource,
            HttpServletRequest request) {
        super(news);
        super.setSendNotification(sendNotification);
        this.setFormAction(request);
        super.setLocalizedTitleOfSubmit(SUBMIT_BUTTON_TITLE_CODE, messageSource, request);
    }

    private void setFormAction(HttpServletRequest request) {
        StringBuffer endingBuilder = new StringBuffer(FORM_ACTION_MIDDLE);
        endingBuilder.append(this.getNews().getId());
        endingBuilder.append("/");
        super.setFormAction(endingBuilder.toString(), request);
    }
}
