package com.svnavigatoru600.web.records.session;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which delete the {@link com.svnavigatoru600.domain.records.SessionRecord
 * SessionRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
public abstract class AbstractDeleteRecordController extends AbstractSessionRecordController {

    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "session-records.deletion-failed-due-to-database-error";
    private static final String SUCCESSFUL_DELETE_URL_END = "smazano/";
    private final String successfulDeleteUrl;

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.SessionRecord
     * SessionRecords} of all {@link SessionRecordType SessionRecordTypes}.
     */
    public AbstractDeleteRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordService recordService, MessageSource messageSource) {
        // Note that allRecordTypes is set up during the creation of the parent.
        super(baseUrl, views, recordService, messageSource);
        this.successfulDeleteUrl = this.getBaseUrl()
                + AbstractDeleteRecordController.SUCCESSFUL_DELETE_URL_END;
    }

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.SessionRecord
     * SessionRecords} of the given <code>recordType</code>.
     */
    public AbstractDeleteRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordType recordType, SessionRecordService recordService, MessageSource messageSource) {
        super(baseUrl, views, recordType, recordService, messageSource);
        this.successfulDeleteUrl = this.getBaseUrl()
                + AbstractDeleteRecordController.SUCCESSFUL_DELETE_URL_END;
    }

    @Transactional
    public String delete(int recordId, HttpServletRequest request, ModelMap model) {
        try {
            this.getRecordService().delete(recordId);

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, this.successfulDeleteUrl);
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(e);
            model.addAttribute("error", AbstractDeleteRecordController.DATABASE_ERROR_MESSAGE_CODE);
            return this.getViews().getList();
        }
    }
}
