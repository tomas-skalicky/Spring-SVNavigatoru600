package svnavigatoru.web.records.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;

import svnavigatoru.domain.records.SessionRecord;
import svnavigatoru.domain.records.SessionRecordType;
import svnavigatoru.repository.records.SessionRecordDao;
import svnavigatoru.service.records.session.SessionRecordValidator;
import svnavigatoru.service.util.Localization;
import svnavigatoru.web.records.PageViews;

/**
 * Parent of all controllers which create and edit the {@link SessionRecord}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
public abstract class NewEditRecordController extends SessionRecordController {

	/**
	 * Code of the error message used when the {@link IllegalStateException} or
	 * {@link IOException} is thrown.
	 */
	public static final String UPLOAD_FILE_ERROR_MESSAGE_CODE = "file.error-during-saving-file";
	/**
	 * Command used in
	 * /main-content/records/session/templates/new-edit-record.jsp.
	 */
	public static final String COMMAND = "newEditRecordCommand";
	protected Validator validator;

	/**
	 * Constructs a controller which considers all {@link SessionRecord}s of all
	 * {@link SessionRecordType}s.
	 */
	public NewEditRecordController(String baseUrl, PageViews views, SessionRecordDao recordDao,
			SessionRecordValidator validator, MessageSource messageSource) {
		super(baseUrl, views, recordDao, messageSource);
		this.validator = validator;
	}

	/**
	 * Constructs a controller which considers all {@link SessionRecord}s of the
	 * given <code>recordType</code>.
	 */
	public NewEditRecordController(String baseUrl, PageViews views, SessionRecordType recordType,
			SessionRecordDao recordDao, SessionRecordValidator validator, MessageSource messageSource) {
		super(baseUrl, views, recordType, recordDao, messageSource);
		this.validator = validator;
	}

	/**
	 * <p>
	 * Creates a {@list List} of localized names of {@link SessionRecordType}s.
	 * The forms which use this controller can access the resulting list.
	 * </p>
	 * <p>
	 * This method is used for filling up the tag <em>radiobuttons</em> and the
	 * value of the selected radiobutton is stored to
	 * <code>NewEditSessionRecord.newType</code>.
	 * </p>
	 */
	@ModelAttribute("sessionRecordTypeList")
	public List<String> populateSessionRecordTypeList(HttpServletRequest request) {
		List<String> sessionRecordTypeList = new ArrayList<String>();

		for (SessionRecordType type : SessionRecordType.values()) {
			String localizationCode = type.getLocalizationCode();
			sessionRecordTypeList.add(Localization.findLocaleMessage(this.messageSource, request, localizationCode));
		}
		return sessionRecordTypeList;
	}
}
