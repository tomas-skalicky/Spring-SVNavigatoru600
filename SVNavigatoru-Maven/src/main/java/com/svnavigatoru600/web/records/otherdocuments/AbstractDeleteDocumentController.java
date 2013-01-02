package com.svnavigatoru600.web.records.otherdocuments;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.web.Configuration;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which delete the {@link OtherDocumentRecord OtherDocumentRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
public abstract class AbstractDeleteDocumentController extends AbstractOtherDocumentRecordController {

    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "other-documents.deletion-failed-due-to-database-error";
    private static final String SUCCESSFUL_DELETE_URL_END = "smazano/";
    private final String successfulDeleteUrl;

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord OtherDocumentRecords} of all
     * {@link OtherDocumentRecordType OtherDocumentRecordTypes}.
     */
    public AbstractDeleteDocumentController(String baseUrl, AbstractPageViews views,
            OtherDocumentRecordDao recordDao, MessageSource messageSource) {
        // Note that allRecordTypes is set up during the creation of the parent.
        super(baseUrl, views, recordDao, messageSource);
        this.successfulDeleteUrl = this.getBaseUrl()
                + AbstractDeleteDocumentController.SUCCESSFUL_DELETE_URL_END;
    }

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord OtherDocumentRecords} of the
     * given <code>recordType</code>.
     */
    public AbstractDeleteDocumentController(String baseUrl, AbstractPageViews views,
            OtherDocumentRecordType recordType, OtherDocumentRecordDao recordDao, MessageSource messageSource) {
        super(baseUrl, views, recordType, recordDao, messageSource);
        this.successfulDeleteUrl = this.getBaseUrl()
                + AbstractDeleteDocumentController.SUCCESSFUL_DELETE_URL_END;
    }

    @Transactional
    public String delete(int recordId, HttpServletRequest request, ModelMap model) {
        try {
            // Deletes the record from the repository and deletes the associated
            // file from the target folder.
            final OtherDocumentRecordDao recordDao = this.getRecordDao();
            AbstractDocumentRecord record = recordDao.findById(recordId, false);
            recordDao.delete(record);
            // /////////////////////////////////////////////////////////////////
            // Store in the FILESYSTEM
            // --------------------------------------------------------------
            // java.io.File file = File.getUploadedFile(record.getFileName());
            // file.delete();
            // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE, this.successfulDeleteUrl);
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(e);
            model.addAttribute("error", AbstractDeleteDocumentController.DATABASE_ERROR_MESSAGE_CODE);
            return this.getViews().getList();
        }
    }
}
