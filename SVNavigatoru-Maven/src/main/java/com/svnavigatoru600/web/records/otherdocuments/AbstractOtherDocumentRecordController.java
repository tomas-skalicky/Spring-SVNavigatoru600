package com.svnavigatoru600.web.records.otherdocuments;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.web.records.AbstractDocumentRecordController;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which handle all operations upon the
 * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractOtherDocumentRecordController extends AbstractDocumentRecordController {

    /**
     * If all {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecords} are considered (i.e.
     * processed) by this controller, <code>allRecordTypes</code> equals <code>true</code>. Otherwise,
     * <code>allRecordTypes</code> equals <code>false</code> and <code>RECORD_TYPE</code> determines the exact type of
     * treated records.
     */
    private final OtherDocumentRecordTypeEnum recordType;
    private boolean allRecordTypes = false;
    private OtherDocumentRecordService recordService = null;

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.OtherDocumentRecord
     * OtherDocumentRecords} of all {@link OtherDocumentRecordTypeEnum OtherDocumentRecordTypes}.
     */
    public AbstractOtherDocumentRecordController(final String baseUrl, final AbstractPageViews views,
            final OtherDocumentRecordService recordService, final MessageSource messageSource) {
        this(baseUrl, views, null, recordService, messageSource);
        allRecordTypes = true;
    }

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.OtherDocumentRecord
     * OtherDocumentRecords} of the given <code>recordType</code>.
     */
    public AbstractOtherDocumentRecordController(final String baseUrl, final AbstractPageViews views,
            final OtherDocumentRecordTypeEnum recordType, final OtherDocumentRecordService recordService, final MessageSource messageSource) {
        super(baseUrl, views, messageSource);
        this.recordType = recordType;
        this.recordService = recordService;
    }

    /**
     * Trivial getter
     */
    protected OtherDocumentRecordTypeEnum getRecordType() {
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
    protected OtherDocumentRecordService getRecordService() {
        return recordService;
    }
}
