package com.svnavigatoru600.web.records.otherdocuments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.service.records.otherdocuments.ShowAllRecords;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderType;
import com.svnavigatoru600.web.records.PageViews;


/**
 * Parent of all controllers which list the {@link OtherDocumentRecord}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class ListDocumentsController extends OtherDocumentRecordController {

	/**
	 * Command used in /main-content/records/other-documents/templates/list-records.jsp.
	 */
	public static final String COMMAND = "showAllRecordsCommand";

	/**
	 * Constructs a controller which considers all {@link OtherDocumentRecord}s
	 * of all {@link OtherDocumentRecordType}s.
	 */
	public ListDocumentsController(String baseUrl, PageViews views, OtherDocumentRecordDao recordDao,
			MessageSource messageSource) {
		super(baseUrl, views, recordDao, messageSource);
	}

	/**
	 * Constructs a controller which considers all {@link OtherDocumentRecord}s
	 * of the given <code>recordType</code>.
	 */
	public ListDocumentsController(String baseUrl, PageViews views, OtherDocumentRecordType recordType,
			OtherDocumentRecordDao recordDao, MessageSource messageSource) {
		super(baseUrl, views, recordType, recordDao, messageSource);
	}

	public String initPage(HttpServletRequest request, ModelMap model) {

		ShowAllRecords command = new ShowAllRecords();

		List<OtherDocumentRecord> records = null;
		if (this.allRecordTypes) {
			records = this.recordDao.findOrdered(OrderType.DESCENDING);
		} else {
			records = this.recordDao.findOrdered(this.RECORD_TYPE, OrderType.DESCENDING);
		}
		command.setRecords(records);

		// Sets up all auxiliary (but necessary) maps.
		command.setLocalizedDeleteQuestions(this.getLocalizedDeleteQuestions(records, request));

		model.addAttribute(ListDocumentsController.COMMAND, command);
		return VIEWS.LIST;
	}

	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String initPageAfterCreate(HttpServletRequest request, ModelMap model) {
		String view = this.initPage(request, model);
		((ShowAllRecords) model.get(ListDocumentsController.COMMAND)).setRecordCreated(true);
		return view;
	}

	@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
	public String initPageAfterDelete(HttpServletRequest request, ModelMap model) {
		String view = this.initPage(request, model);
		((ShowAllRecords) model.get(ListDocumentsController.COMMAND)).setRecordDeleted(true);
		return view;
	}

	/**
	 * Gets a {@link Map} which for each input {@link OtherDocumentRecord}
	 * contains an appropriate localized delete questions.
	 */
	private Map<OtherDocumentRecord, String> getLocalizedDeleteQuestions(List<OtherDocumentRecord> records,
			HttpServletRequest request) {
		final String messageCode = "other-documents.do-you-really-want-to-delete-document";
		Map<OtherDocumentRecord, String> questions = new HashMap<OtherDocumentRecord, String>();

		for (OtherDocumentRecord record : records) {
			Object[] messageParams = new Object[] { record.getName() };
			questions.put(record,
					Localization.findLocaleMessage(this.messageSource, request, messageCode, messageParams));
		}
		return questions;
	}
}