package com.svnavigatoru600.web.records.session;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.service.records.session.SessionRecordService;
import com.svnavigatoru600.web.records.AbstractDocumentRecordController;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which handle all operations upon the {@link SessionRecord SessionRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractSessionRecordController extends AbstractDocumentRecordController {

    /**
     * If all {@link SessionRecord SessionRecords} are considered (i.e. processed) by this controller,
     * <code>allRecordTypes</code> equals <code>true</code>. Otherwise, <code>allRecordTypes</code> equals
     * <code>false</code> and <code>RECORD_TYPE</code> determines the exact type of treated records.
     */
    private final SessionRecordType recordType;
    private boolean allRecordTypes = false;
    private SessionRecordService recordService = null;

    /**
     * Constructs a controller which considers all {@link SessionRecord SessionRecords} of all
     * {@link SessionRecordType SessionRecordTypes}.
     */
    public AbstractSessionRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordService recordService, MessageSource messageSource) {
        this(baseUrl, views, null, recordService, messageSource);
        this.allRecordTypes = true;
    }

    /**
     * Constructs a controller which considers all {@link SessionRecord SessionRecords} of the given
     * <code>recordType</code> .
     */
    public AbstractSessionRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordType recordType, SessionRecordService recordService, MessageSource messageSource) {
        super(baseUrl, views, messageSource);
        this.recordType = recordType;
        this.recordService = recordService;
    }

    /**
     * Trivial getter
     */
    protected SessionRecordType getRecordType() {
        return this.recordType;
    }

    /**
     * Trivial getter
     */
    protected boolean isAllRecordTypes() {
        return this.allRecordTypes;
    }

    /**
     * Trivial getter
     */
    protected SessionRecordService getRecordService() {
        return this.recordService;
    }
}
