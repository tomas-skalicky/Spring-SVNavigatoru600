package com.svnavigatoru600.web.news;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.viewmodel.SendNotification;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class GoToNewFormResponse extends AbstractGoToFormResponse {

    private static final String SUBMIT_BUTTON_TITLE_CODE = "news.add-news";

    public GoToNewFormResponse(News news, SendNotification sendNotification, MessageSource messageSource,
            HttpServletRequest request) {
        super(news);
        super.setSendNotification(sendNotification);
        super.setFormAction(CommonUrlParts.NEW_EXTENSION, request);
        super.setLocalizedTitleOfSubmit(SUBMIT_BUTTON_TITLE_CODE, messageSource, request);
    }
}
