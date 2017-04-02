package com.svnavigatoru600.web.records.session;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordTypeEnum;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.viewmodel.records.session.ShowAllSessionRecords;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which list the {@link SessionRecord SessionRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractListRecordsController extends AbstractSessionRecordController {

    /**
     * Command used in /main-content/records/session/templates/list-records.jsp.
     */
    public static final String COMMAND = "showAllRecordsCommand";

    /**
     * Constructs a controller which considers all {@link SessionRecord SessionRecords} of all {@link SessionRecordTypeEnum
     * SessionRecordTypes}.
     */
    public AbstractListRecordsController(final String baseUrl, final AbstractPageViews views, final SessionRecordService recordService,
            final MessageSource messageSource) {
        super(baseUrl, views, recordService, messageSource);
    }

    /**
     * Constructs a controller which considers all {@link SessionRecord SessionRecords} of the given
     * <code>recordType</code>.
     */
    public AbstractListRecordsController(final String baseUrl, final AbstractPageViews views, final SessionRecordTypeEnum recordType,
            final SessionRecordService recordService, final MessageSource messageSource) {
        super(baseUrl, views, recordType, recordService, messageSource);
    }

    public String initPage(final HttpServletRequest request, final ModelMap model) {

        final ShowAllSessionRecords command = new ShowAllSessionRecords();
        final boolean allRecordTypes = isAllRecordTypes();
        command.setAllRecordTypes(allRecordTypes);

        final List<SessionRecord> records = getRecordService().findAllOrdered(allRecordTypes, getRecordType());
        command.setRecords(records);

        // Sets up all auxiliary (but necessary) maps.
        final MessageSource messageSource = getMessageSource();
        if (allRecordTypes) {
            command.setLocalizedTypeTitles(
                    SessionRecordService.getLocalizedTypeTitles(records, request, messageSource));
        }
        final Map<SessionRecord, String> sessionDates = SessionRecordService.getLocalizedSessionDates(records, request);
        command.setLocalizedSessionDates(sessionDates);
        command.setLocalizedDeleteQuestions(
                SessionRecordService.getLocalizedDeleteQuestions(records, request, sessionDates, messageSource));

        model.addAttribute(AbstractListRecordsController.COMMAND, command);
        return getViews().getList();
    }

    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String initPageAfterCreate(final HttpServletRequest request, final ModelMap model) {
        final String view = initPage(request, model);
        ((ShowAllSessionRecords) model.get(AbstractListRecordsController.COMMAND)).setRecordCreated(true);
        return view;
    }

    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String initPageAfterDelete(final HttpServletRequest request, final ModelMap model) {
        final String view = initPage(request, model);
        ((ShowAllSessionRecords) model.get(AbstractListRecordsController.COMMAND)).setRecordDeleted(true);
        return view;
    }
}
