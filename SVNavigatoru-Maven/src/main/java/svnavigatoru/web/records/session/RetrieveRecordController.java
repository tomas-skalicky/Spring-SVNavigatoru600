package svnavigatoru.web.records.session;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import svnavigatoru.domain.records.SessionRecord;
import svnavigatoru.domain.records.SessionRecordType;
import svnavigatoru.repository.records.SessionRecordDao;
import svnavigatoru.web.records.PageViews;
import svnavigatoru.web.records.RetrieveDocumentRecordUtils;

/**
 * Parent of all controllers which provide retrieving of stored
 * {@link SessionRecord}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class RetrieveRecordController extends SessionRecordController {

	/**
	 * Code of the database error message used when the
	 * {@link DataAccessException} is thrown.
	 */
	public static final String DATABASE_ERROR_MESSAGE_CODE = "session-records.retrieve-failed-due-to-database-error";
	private static final String RETRIEVE_URL_END = "stahnout/";
	protected final String RETRIEVE_URL;

	/**
	 * Constructs a controller which considers all {@link SessionRecord}s of all
	 * {@link SessionRecordType}s.
	 */
	public RetrieveRecordController(String baseUrl, PageViews views, SessionRecordDao recordDao,
			MessageSource messageSource) {
		// Note that allRecordTypes is set up during the creation of the parent.
		super(baseUrl, views, recordDao, messageSource);
		this.RETRIEVE_URL = this.BASE_URL + RetrieveRecordController.RETRIEVE_URL_END;
	}

	/**
	 * Constructs a controller which considers all {@link SessionRecord}s of the
	 * given <code>recordType</code>.
	 */
	public RetrieveRecordController(String baseUrl, PageViews views, SessionRecordType recordType,
			SessionRecordDao recordDao, MessageSource messageSource) {
		super(baseUrl, views, recordType, recordDao, messageSource);
		this.RETRIEVE_URL = this.BASE_URL + RetrieveRecordController.RETRIEVE_URL_END;
	}

	public void retrieve(int recordId, HttpServletResponse response, ModelMap model) {
		try {
			RetrieveDocumentRecordUtils.retrieve(recordId, this.recordDao, response);

			// NOTE: nothing is returned.

		} catch (DataAccessException e) {
			// We encountered a database problem.
			this.logger.error(e);
			model.addAttribute("error", RetrieveRecordController.DATABASE_ERROR_MESSAGE_CODE);
		} catch (SQLException e) {
			this.logger.error(e);
			model.addAttribute("error", RetrieveRecordController.DATABASE_ERROR_MESSAGE_CODE);
		} catch (IOException e) {
			this.logger.error(e);
			model.addAttribute("error", RetrieveRecordController.DATABASE_ERROR_MESSAGE_CODE);
		}
	}
}
