package com.svnavigatoru600.web.records.otherdocuments;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.viewmodel.records.otherdocuments.ShowAllRecords;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which list the {@link OtherDocumentRecord OtherDocumentRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractListDocumentsController extends AbstractOtherDocumentRecordController {

    /**
     * Command used in /main-content/records/other-documents/templates/list-records.jsp.
     */
    public static final String COMMAND = "showAllRecordsCommand";

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord OtherDocumentRecords} of all
     * {@link OtherDocumentRecordTypeEnum OtherDocumentRecordTypes}.
     */
    public AbstractListDocumentsController(final String baseUrl, final AbstractPageViews views,
            final OtherDocumentRecordService recordService, final MessageSource messageSource) {
        super(baseUrl, views, recordService, messageSource);
    }

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord OtherDocumentRecords} of the given
     * <code>recordType</code>.
     */
    public AbstractListDocumentsController(final String baseUrl, final AbstractPageViews views, final OtherDocumentRecordTypeEnum recordType,
            final OtherDocumentRecordService recordService, final MessageSource messageSource) {
        super(baseUrl, views, recordType, recordService, messageSource);
    }

    public String initPage(final HttpServletRequest request, final ModelMap model) {

        final ShowAllRecords command = new ShowAllRecords();

        final List<OtherDocumentRecord> records = getRecordService().findAllOrdered(isAllRecordTypes(), getRecordType());
        command.setRecords(records);

        // Sets up all auxiliary (but necessary) maps.
        command.setLocalizedDeleteQuestions(
                OtherDocumentRecordService.getLocalizedDeleteQuestions(records, request, getMessageSource()));

        model.addAttribute(AbstractListDocumentsController.COMMAND, command);
        return getViews().getList();
    }

    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String initPageAfterCreate(final HttpServletRequest request, final ModelMap model) {
        final String view = initPage(request, model);
        ((ShowAllRecords) model.get(AbstractListDocumentsController.COMMAND)).setRecordCreated(true);
        return view;
    }

    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String initPageAfterDelete(final HttpServletRequest request, final ModelMap model) {
        final String view = initPage(request, model);
        ((ShowAllRecords) model.get(AbstractListDocumentsController.COMMAND)).setRecordDeleted(true);
        return view;
    }
}
