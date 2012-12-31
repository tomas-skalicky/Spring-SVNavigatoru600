package com.svnavigatoru600.web.news;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.viewmodel.news.AbstractNewEditNews;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractNewEditNewsResponse extends AbstractNewsResponse {

    /**
     * Holds data of the command.
     */
    protected AbstractNewEditNews command = null;
    /**
     * Contains global errors if any exists.
     */
    protected List<ObjectError> globalErrors = new ArrayList<ObjectError>();
    /**
     * Contains field errors if any exists.
     */
    protected List<FieldError> fieldErrors = new ArrayList<FieldError>();

    public AbstractNewEditNewsResponse(AbstractNewEditNews command) {
        super();
        this.command = command;
    }

    /**
     * Sets up everything in a way that the processing of the command has failed.
     */
    public void setFail(BindingResult result, MessageSource messageSource, HttpServletRequest request) {
        this.setFail();
        this.globalErrors = Localization.localizeGlobalErrors(result.getGlobalErrors(), messageSource,
                request);
        this.fieldErrors = Localization.localizeFieldErrors(result.getFieldErrors(), messageSource, request);
    }

    public AbstractNewEditNews getCommand() {
        return command;
    }

    public List<ObjectError> getGlobalErrors() {
        return globalErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
