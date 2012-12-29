package com.svnavigatoru600.web.records.session;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderType;
import com.svnavigatoru600.viewmodel.records.session.ShowAllSessionRecords;
import com.svnavigatoru600.web.records.PageViews;

/**
 * Parent of all controllers which list the {@link SessionRecord}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class ListRecordsController extends SessionRecordController {

    /**
     * Command used in /main-content/records/session/templates/list-records.jsp.
     */
    public static final String COMMAND = "showAllRecordsCommand";

    /**
     * Constructs a controller which considers all {@link SessionRecord}s of all {@link SessionRecordType}s.
     */
    public ListRecordsController(final String baseUrl, final PageViews views,
            final SessionRecordDao recordDao, final MessageSource messageSource) {
        super(baseUrl, views, recordDao, messageSource);
    }

    /**
     * Constructs a controller which considers all {@link SessionRecord}s of the given <code>recordType</code>
     * .
     */
    public ListRecordsController(final String baseUrl, final PageViews views,
            final SessionRecordType recordType, final SessionRecordDao recordDao,
            final MessageSource messageSource) {
        super(baseUrl, views, recordType, recordDao, messageSource);
    }

    public String initPage(final HttpServletRequest request, final ModelMap model) {

        final ShowAllSessionRecords command = new ShowAllSessionRecords();
        command.setAllRecordTypes(this.allRecordTypes);

        List<SessionRecord> records = null;
        if (this.allRecordTypes) {
            records = this.recordDao.findOrdered(OrderType.DESCENDING);
        } else {
            records = this.recordDao.findOrdered(this.RECORD_TYPE, OrderType.DESCENDING);
        }
        command.setRecords(records);

        // Sets up all auxiliary (but necessary) maps.
        if (this.allRecordTypes) {
            command.setLocalizedTypeTitles(this.getLocalizedTypeTitles(records, request));
        }
        final Map<SessionRecord, String> sessionDates = this.getLocalizedSessionDates(records, request);
        command.setLocalizedSessionDates(sessionDates);
        command.setLocalizedDeleteQuestions(this.getLocalizedDeleteQuestions(records, request, sessionDates));

        model.addAttribute(ListRecordsController.COMMAND, command);
        return VIEWS.LIST;
    }

    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String initPageAfterCreate(final HttpServletRequest request, final ModelMap model) {
        final String view = this.initPage(request, model);
        ((ShowAllSessionRecords) model.get(ListRecordsController.COMMAND)).setRecordCreated(true);
        return view;
    }

    @PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
    public String initPageAfterDelete(final HttpServletRequest request, final ModelMap model) {
        final String view = this.initPage(request, model);
        ((ShowAllSessionRecords) model.get(ListRecordsController.COMMAND)).setRecordDeleted(true);
        return view;
    }

    /**
     * Gets a {@link Map} which for each input {@link SessionRecord} contains a localized title of its
     * record's type.
     */
    private Map<SessionRecord, String> getLocalizedTypeTitles(final List<SessionRecord> records,
            final HttpServletRequest request) {
        final Map<SessionRecord, String> typeTitles = new HashMap<SessionRecord, String>();

        for (SessionRecord record : records) {
            final String titleCode = record.getTypedType().getLocalizationCode();
            typeTitles.put(record, Localization.findLocaleMessage(this.messageSource, request, titleCode));
        }
        return typeTitles;
    }

    /**
     * Gets a {@link Map} which for each input {@link SessionRecord} contains its localized
     * <code>sessionDate</code>.
     */
    private Map<SessionRecord, String> getLocalizedSessionDates(final List<SessionRecord> records,
            final HttpServletRequest request) {
        final Locale locale = Localization.getLocale(request);
        final Map<SessionRecord, String> sessionDates = new HashMap<SessionRecord, String>();

        for (SessionRecord record : records) {
            final String date = DateUtils.format(record.getSessionDate(),
                    DateUtils.LONG_DATE_FORMATS.get(locale), locale);
            sessionDates.put(record, date);
        }
        return sessionDates;
    }

    /**
     * Gets a {@link Map} which for each input {@link SessionRecord} contains an appropriate localized delete
     * questions.
     */
    private Map<SessionRecord, String> getLocalizedDeleteQuestions(final List<SessionRecord> records,
            final HttpServletRequest request, final Map<SessionRecord, String> localizedSessionDates) {
        final String messageCode = "session-records.do-you-really-want-to-delete-record";
        final Map<SessionRecord, String> questions = new HashMap<SessionRecord, String>();

        for (SessionRecord record : records) {
            final Object[] messageParams = new Object[] { localizedSessionDates.get(record) };
            questions.put(record,
                    Localization.findLocaleMessage(this.messageSource, request, messageCode, messageParams));
        }
        return questions;
    }
}
