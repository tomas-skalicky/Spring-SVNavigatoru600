package com.svnavigatoru600.web.news;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.service.util.Localization;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class DeleteNewsResponse extends AbstractNewsResponse {

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
        return this.error;
    }
}
