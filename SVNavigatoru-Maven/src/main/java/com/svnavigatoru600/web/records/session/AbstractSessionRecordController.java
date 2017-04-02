package com.svnavigatoru600.web.records.session;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.records.SessionRecordTypeEnum;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.web.records.AbstractDocumentRecordController;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which handle all operations upon the
 * {@link com.svnavigatoru600.domain.records.SessionRecord SessionRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractSessionRecordController extends AbstractDocumentRecordController {

    /**
     * If all {@link com.svnavigatoru600.domain.records.SessionRecord SessionRecords} are considered (i.e. processed) by
     * this controller, <code>allRecordTypes</code> equals <code>true</code>. Otherwise, <code>allRecordTypes</code>
     * equals <code>false</code> and <code>RECORD_TYPE</code> determines the exact type of treated records.
     */
    private final SessionRecordTypeEnum recordType;
    private boolean allRecordTypes = false;
    private SessionRecordService recordService = null;

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.SessionRecord
     * SessionRecords} of all {@link SessionRecordTypeEnum SessionRecordTypes}.
     */
    public AbstractSessionRecordController(final String baseUrl, final AbstractPageViews views, final SessionRecordService recordService,
            final MessageSource messageSource) {
        this(baseUrl, views, null, recordService, messageSource);
        allRecordTypes = true;
    }

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.SessionRecord
     * SessionRecords} of the given <code>recordType</code>.
     */
    public AbstractSessionRecordController(final String baseUrl, final AbstractPageViews views, final SessionRecordTypeEnum recordType,
            final SessionRecordService recordService, final MessageSource messageSource) {
        super(baseUrl, views, messageSource);
        this.recordType = recordType;
        this.recordService = recordService;
    }

    /**
     * Trivial getter
     */
    protected SessionRecordTypeEnum getRecordType() {
        return recordType;
    }

    /**
     * Trivial getter
     */
    protected boolean isAllRecordTypes() {
        return allRecordTypes;
    }

    /**
     * Trivial getter
     */
    protected SessionRecordService getRecordService() {
        return recordService;
    }
}
