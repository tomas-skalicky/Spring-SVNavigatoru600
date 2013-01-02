package com.svnavigatoru600.web.records.session;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.service.util.File;
import com.svnavigatoru600.web.Configuration;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which delete the {@link SessionRecord SessionRecords}.
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
    protected final String successfulDeleteUrl;

    /**
     * Constructs a controller which considers all {@link SessionRecord SessionRecords} of all
     * {@link SessionRecordType SessionRecordTypes}.
     */
    public AbstractDeleteRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordDao recordDao, MessageSource messageSource) {
        // Note that allRecordTypes is set up during the creation of the parent.
        super(baseUrl, views, recordDao, messageSource);
        this.successfulDeleteUrl = this.baseUrl + AbstractDeleteRecordController.SUCCESSFUL_DELETE_URL_END;
    }

    /**
     * Constructs a controller which considers all {@link SessionRecord SessionRecords} of the given
     * <code>recordType</code> .
     */
    public AbstractDeleteRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordType recordType, SessionRecordDao recordDao, MessageSource messageSource) {
        super(baseUrl, views, recordType, recordDao, messageSource);
        this.successfulDeleteUrl = this.baseUrl + AbstractDeleteRecordController.SUCCESSFUL_DELETE_URL_END;
    }

    @Transactional
    public String delete(int recordId, HttpServletRequest request, ModelMap model) {
        try {
            // Deletes the record from the repository and deletes the associated
            // file from the target folder.
            AbstractDocumentRecord record = this.recordDao.findById(recordId, false);
            this.recordDao.delete(record);
            java.io.File file = File.getUploadedFile(record.getFileName());
            file.delete();

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE, this.successfulDeleteUrl);
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(e);
            model.addAttribute("error", AbstractDeleteRecordController.DATABASE_ERROR_MESSAGE_CODE);
            return this.views.list;
        }
    }
}
