package com.svnavigatoru600.web.records.session;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.web.records.DocumentRecordController;
import com.svnavigatoru600.web.records.PageViews;


/**
 * Parent of all controllers which handle all operations upon the
 * {@link SessionRecord}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class SessionRecordController extends DocumentRecordController {

	/**
	 * If all {@link SessionRecord}s are considered (i.e. processed) by this
	 * controller, <code>allRecordTypes</code> equals <code>true</code>.
	 * Otherwise, <code>allRecordTypes</code> equals <code>false</code> and
	 * <code>RECORD_TYPE</code> determines the exact type of treated records.
	 */
	protected final SessionRecordType RECORD_TYPE;
	protected boolean allRecordTypes = false;
	protected SessionRecordDao recordDao = null;

	/**
	 * Constructs a controller which considers all {@link SessionRecord}s of all
	 * {@link SessionRecordType}s.
	 */
	public SessionRecordController(String baseUrl, PageViews views, SessionRecordDao recordDao,
			MessageSource messageSource) {
		this(baseUrl, views, null, recordDao, messageSource);
		this.allRecordTypes = true;
	}

	/**
	 * Constructs a controller which considers all {@link SessionRecord}s of the
	 * given <code>recordType</code>.
	 */
	public SessionRecordController(String baseUrl, PageViews views, SessionRecordType recordType,
			SessionRecordDao recordDao, MessageSource messageSource) {
		super(baseUrl, views, messageSource);
		this.RECORD_TYPE = recordType;
		this.recordDao = recordDao;
	}
}
