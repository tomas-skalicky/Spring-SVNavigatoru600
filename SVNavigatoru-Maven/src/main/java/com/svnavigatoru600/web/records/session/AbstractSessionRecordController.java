package com.svnavigatoru600.web.records.session;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.web.records.AbstractDocumentRecordController;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which handle all operations upon the {@link SessionRecord}s.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractSessionRecordController extends AbstractDocumentRecordController {

    /**
     * If all {@link SessionRecord}s are considered (i.e. processed) by this controller,
     * <code>allRecordTypes</code> equals <code>true</code>. Otherwise, <code>allRecordTypes</code> equals
     * <code>false</code> and <code>RECORD_TYPE</code> determines the exact type of treated records.
     */
    protected final SessionRecordType recordType;
    protected boolean allRecordTypes = false;
    protected SessionRecordDao recordDao = null;

    /**
     * Constructs a controller which considers all {@link SessionRecord}s of all {@link SessionRecordType}s.
     */
    public AbstractSessionRecordController(String baseUrl, AbstractPageViews views, SessionRecordDao recordDao,
            MessageSource messageSource) {
        this(baseUrl, views, null, recordDao, messageSource);
        this.allRecordTypes = true;
    }

    /**
     * Constructs a controller which considers all {@link SessionRecord}s of the given <code>recordType</code>
     * .
     */
    public AbstractSessionRecordController(String baseUrl, AbstractPageViews views, SessionRecordType recordType,
            SessionRecordDao recordDao, MessageSource messageSource) {
        super(baseUrl, views, messageSource);
        this.recordType = recordType;
        this.recordDao = recordDao;
    }
}
