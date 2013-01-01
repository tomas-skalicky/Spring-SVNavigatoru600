package com.svnavigatoru600.web.records;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * Parent of all controllers which handle all operations upon the {@link AbstractDocumentRecord}s.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractDocumentRecordController extends AbstractPrivateSectionMetaController {

    /**
     * The part of the URL which is common for all operations performed on the considered documents.
     */
    protected final String baseUrl;
    protected final AbstractPageViews views;
    protected MessageSource messageSource;

    /**
     * Constructs a controller which considers all {@link AbstractDocumentRecord}s.
     */
    public AbstractDocumentRecordController(String baseUrl, AbstractPageViews views,
            MessageSource messageSource) {
        this.baseUrl = baseUrl;
        this.views = views;
        this.messageSource = messageSource;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Nasty since the format is localized.
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.CALENDAR_DATE_FORMAT);

        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}