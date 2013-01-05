package com.svnavigatoru600.web.records.session;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which provide retrieving of stored
 * {@link com.svnavigatoru600.domain.records.SessionRecord SessionRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractRetrieveRecordController extends AbstractSessionRecordController {

    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "session-records.retrieve-failed-due-to-database-error";
    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.SessionRecord
     * SessionRecords} of all {@link SessionRecordType SessionRecordTypes}.
     */
    public AbstractRetrieveRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordService recordService, MessageSource messageSource) {
        // Note that allRecordTypes is set up during the creation of the parent.
        super(baseUrl, views, recordService, messageSource);
    }

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.SessionRecord
     * SessionRecords} of the given <code>recordType</code>.
     */
    public AbstractRetrieveRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordType recordType, SessionRecordService recordService, MessageSource messageSource) {
        super(baseUrl, views, recordType, recordService, messageSource);
    }

    public void retrieve(int recordId, HttpServletResponse response, ModelMap model) {
        try {
            this.getRecordService().retrieve(recordId, response);

            // NOTE: nothing is returned.

        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(e);
            model.addAttribute("error", AbstractRetrieveRecordController.DATABASE_ERROR_MESSAGE_CODE);
        } catch (SQLException e) {
            this.logger.error(e);
            model.addAttribute("error", AbstractRetrieveRecordController.DATABASE_ERROR_MESSAGE_CODE);
        } catch (IOException e) {
            this.logger.error(e);
            model.addAttribute("error", AbstractRetrieveRecordController.DATABASE_ERROR_MESSAGE_CODE);
        }
    }
}
