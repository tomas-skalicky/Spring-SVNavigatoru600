package svnavigatoru.web.records.session;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import svnavigatoru.domain.records.SessionRecord;
import svnavigatoru.domain.records.SessionRecordType;
import svnavigatoru.repository.records.SessionRecordDao;
import svnavigatoru.web.records.DocumentRecordController;
import svnavigatoru.web.records.PageViews;

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
