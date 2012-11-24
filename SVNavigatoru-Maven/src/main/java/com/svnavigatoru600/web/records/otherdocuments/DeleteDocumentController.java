package com.svnavigatoru600.web.records.otherdocuments;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.DocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.web.Configuration;
import com.svnavigatoru600.web.records.PageViews;


/**
 * Parent of all controllers which delete the {@link OtherDocumentRecord}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
public abstract class DeleteDocumentController extends OtherDocumentRecordController {

	/**
	 * Code of the database error message used when the
	 * {@link DataAccessException} is thrown.
	 */
	public static final String DATABASE_ERROR_MESSAGE_CODE = "other-documents.deletion-failed-due-to-database-error";
	private static final String SUCCESSFUL_DELETE_URL_END = "smazano/";
	protected final String SUCCESSFUL_DELETE_URL;

	/**
	 * Constructs a controller which considers all {@link OtherDocumentRecord}s
	 * of all {@link OtherDocumentRecordType}s.
	 */
	public DeleteDocumentController(String baseUrl, PageViews views, OtherDocumentRecordDao recordDao,
			MessageSource messageSource) {
		// Note that allRecordTypes is set up during the creation of the parent.
		super(baseUrl, views, recordDao, messageSource);
		this.SUCCESSFUL_DELETE_URL = this.BASE_URL + DeleteDocumentController.SUCCESSFUL_DELETE_URL_END;
	}

	/**
	 * Constructs a controller which considers all {@link OtherDocumentRecord}s
	 * of the given <code>recordType</code>.
	 */
	public DeleteDocumentController(String baseUrl, PageViews views, OtherDocumentRecordType recordType,
			OtherDocumentRecordDao recordDao, MessageSource messageSource) {
		super(baseUrl, views, recordType, recordDao, messageSource);
		this.SUCCESSFUL_DELETE_URL = this.BASE_URL + DeleteDocumentController.SUCCESSFUL_DELETE_URL_END;
	}

	public String delete(int recordId, HttpServletRequest request, ModelMap model) {
		try {
			// Deletes the record from the repository and deletes the associated
			// file from the target folder.
			DocumentRecord record = this.recordDao.findById(recordId, false);
			this.recordDao.delete(record);
			// /////////////////////////////////////////////////////////////////
			// Store in the FILESYSTEM
			// --------------------------------------------------------------
			// java.io.File file = File.getUploadedFile(record.getFileName());
			// file.delete();
			// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

			// Returns the form success view.
			model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE, this.SUCCESSFUL_DELETE_URL);
			return Configuration.REDIRECTION_PAGE;

		} catch (DataAccessException e) {
			// We encountered a database problem.
			this.logger.error(e);
			model.addAttribute("error", DeleteDocumentController.DATABASE_ERROR_MESSAGE_CODE);
			return VIEWS.LIST;
		}
	}
}
