package com.svnavigatoru600.web.records.otherdocuments;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.web.records.AbstractPageViews;
import com.svnavigatoru600.web.records.RetrieveDocumentRecordUtils;

/**
 * Parent of all controllers which provide retrieving of stored {@link OtherDocumentRecord}s.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractRetrieveDocumentController extends AbstractOtherDocumentRecordController {

    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "other-documents.retrieve-failed-due-to-database-error";
    private static final String RETRIEVE_URL_END = "stahnout/";
    protected final String retrieveUrl;

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord}s of all
     * {@link OtherDocumentRecordType}s.
     */
    public AbstractRetrieveDocumentController(String baseUrl, AbstractPageViews views,
            OtherDocumentRecordDao recordDao, MessageSource messageSource) {
        // Note that the allRecordTypes indicator is set up during the creation
        // of the parent.
        super(baseUrl, views, recordDao, messageSource);
        this.retrieveUrl = this.baseUrl + AbstractRetrieveDocumentController.RETRIEVE_URL_END;
    }

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord}s of the given
     * <code>recordType</code>.
     */
    public AbstractRetrieveDocumentController(String baseUrl, AbstractPageViews views,
            OtherDocumentRecordType recordType, OtherDocumentRecordDao recordDao, MessageSource messageSource) {
        super(baseUrl, views, recordType, recordDao, messageSource);
        this.retrieveUrl = this.baseUrl + AbstractRetrieveDocumentController.RETRIEVE_URL_END;
    }

    public void retrieve(int recordId, HttpServletResponse response, ModelMap model) {
        try {
            RetrieveDocumentRecordUtils.retrieve(recordId, this.recordDao, response);

            // NOTE: nothing is returned.

        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(e);
            model.addAttribute("error", AbstractRetrieveDocumentController.DATABASE_ERROR_MESSAGE_CODE);
        } catch (SQLException e) {
            this.logger.error(e);
            model.addAttribute("error", AbstractRetrieveDocumentController.DATABASE_ERROR_MESSAGE_CODE);
        } catch (IOException e) {
            this.logger.error(e);
            model.addAttribute("error", AbstractRetrieveDocumentController.DATABASE_ERROR_MESSAGE_CODE);
        }
    }
}