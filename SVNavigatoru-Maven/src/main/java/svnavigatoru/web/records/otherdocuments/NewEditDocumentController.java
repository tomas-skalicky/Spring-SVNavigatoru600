package svnavigatoru.web.records.otherdocuments;

import java.io.IOException;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import svnavigatoru.domain.records.OtherDocumentRecord;
import svnavigatoru.domain.records.OtherDocumentRecordType;
import svnavigatoru.repository.records.OtherDocumentRecordDao;
import svnavigatoru.service.records.otherdocuments.OtherDocumentRecordValidator;
import svnavigatoru.web.records.PageViews;

/**
 * Parent of all controllers which create and edit the
 * {@link OtherDocumentRecord}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
public abstract class NewEditDocumentController extends OtherDocumentRecordController {

	/**
	 * Code of the error message used when the {@link IllegalStateException} or
	 * {@link IOException} is thrown.
	 */
	public static final String UPLOAD_FILE_ERROR_MESSAGE_CODE = "file.error-during-saving-file";
	/**
	 * Command used in /main-content/records/other-documents/templates/new-edit-record.jsp.
	 */
	public static final String COMMAND = "newEditRecordCommand";
	protected Validator validator;

	/**
	 * Constructs a controller which considers all {@link OtherDocumentRecord}s
	 * of all {@link OtherDocumentRecordType}s.
	 */
	public NewEditDocumentController(String baseUrl, PageViews views, OtherDocumentRecordDao recordDao,
			OtherDocumentRecordValidator validator, MessageSource messageSource) {
		super(baseUrl, views, recordDao, messageSource);
		this.validator = validator;
	}

	/**
	 * Constructs a controller which considers all {@link OtherDocumentRecord}s
	 * of the given <code>recordType</code>.
	 */
	public NewEditDocumentController(String baseUrl, PageViews views, OtherDocumentRecordType recordType,
			OtherDocumentRecordDao recordDao, OtherDocumentRecordValidator validator, MessageSource messageSource) {
		super(baseUrl, views, recordType, recordDao, messageSource);
		this.validator = validator;
	}
}
