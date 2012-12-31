package com.svnavigatoru600.web.records.session;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.web.records.AbstractPageViews;
import com.svnavigatoru600.web.records.RetrieveDocumentRecordUtils;

/**
 * Parent of all controllers which provide retrieving of stored {@link SessionRecord}s.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractRetrieveRecordController extends AbstractSessionRecordController {

    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "session-records.retrieve-failed-due-to-database-error";
    private static final String RETRIEVE_URL_END = "stahnout/";
    protected final String retrieveUrl;

    /**
     * Constructs a controller which considers all {@link SessionRecord}s of all {@link SessionRecordType}s.
     */
    public AbstractRetrieveRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordDao recordDao, MessageSource messageSource) {
        // Note that allRecordTypes is set up during the creation of the parent.
        super(baseUrl, views, recordDao, messageSource);
        this.retrieveUrl = this.baseUrl + AbstractRetrieveRecordController.RETRIEVE_URL_END;
    }

    /**
     * Constructs a controller which considers all {@link SessionRecord}s of the given <code>recordType</code>
     * .
     */
    public AbstractRetrieveRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordType recordType, SessionRecordDao recordDao, MessageSource messageSource) {
        super(baseUrl, views, recordType, recordDao, messageSource);
        this.retrieveUrl = this.baseUrl + AbstractRetrieveRecordController.RETRIEVE_URL_END;
    }

    public void retrieve(int recordId, HttpServletResponse response, ModelMap model) {
        try {
            RetrieveDocumentRecordUtils.retrieve(recordId, this.recordDao, response);

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
