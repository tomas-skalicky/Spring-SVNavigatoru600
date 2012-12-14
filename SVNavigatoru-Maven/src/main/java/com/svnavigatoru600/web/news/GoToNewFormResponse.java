package com.svnavigatoru600.web.news;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.News;

public class GoToNewFormResponse extends GoToFormResponse {

    private static final String FORM_ACTION_ENDING = "novy/";
    private static final String SUBMIT_BUTTON_TITLE_CODE = "news.add-news";

    public GoToNewFormResponse(News news, MessageSource messageSource, HttpServletRequest request) {
        super(news);
        super.setFormAction(FORM_ACTION_ENDING, request);
        super.setLocalizedTitleOfSubmit(SUBMIT_BUTTON_TITLE_CODE, messageSource, request);
    }
}
