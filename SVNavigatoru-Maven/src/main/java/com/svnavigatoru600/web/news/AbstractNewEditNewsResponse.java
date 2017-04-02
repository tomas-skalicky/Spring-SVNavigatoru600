package com.svnavigatoru600.web.news;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.google.common.collect.Lists;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.viewmodel.news.AbstractNewEditNews;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractNewEditNewsResponse extends AbstractNewsResponse {

    /**
     * Holds data of the command.
     */
    private AbstractNewEditNews command = null;
    /**
     * Contains global errors if any exists.
     */
    private List<ObjectError> globalErrors = Lists.newArrayList();
    /**
     * Contains field errors if any exists.
     */
    private List<FieldError> fieldErrors = Lists.newArrayList();

    public AbstractNewEditNewsResponse(final AbstractNewEditNews command) {
        super();
        this.command = command;
    }

    /**
     * Sets up everything in a way that the processing of the command has failed.
     */
    public void setFail(final BindingResult result, final MessageSource messageSource, final HttpServletRequest request) {
        this.setFail();
        globalErrors = Localization.localizeGlobalErrors(result.getGlobalErrors(), messageSource, request);
        fieldErrors = Localization.localizeFieldErrors(result.getFieldErrors(), messageSource, request);
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
