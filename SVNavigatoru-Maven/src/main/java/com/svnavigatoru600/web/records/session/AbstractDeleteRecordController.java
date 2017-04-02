package com.svnavigatoru600.web.records.session;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.SessionRecordTypeEnum;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which delete the {@link com.svnavigatoru600.domain.records.SessionRecord SessionRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
public abstract class AbstractDeleteRecordController extends AbstractSessionRecordController {

    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "session-records.deletion-failed-due-to-database-error";
    private final String successfulDeleteUrl;

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.SessionRecord
     * SessionRecords} of all {@link SessionRecordTypeEnum SessionRecordTypes}.
     */
    public AbstractDeleteRecordController(final String baseUrl, final AbstractPageViews views, final SessionRecordService recordService,
            final MessageSource messageSource) {
        // Note that allRecordTypes is set up during the creation of the parent.
        super(baseUrl, views, recordService, messageSource);
        successfulDeleteUrl = getBaseUrl() + CommonUrlParts.DELETED_EXTENSION;
    }

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.SessionRecord
     * SessionRecords} of the given <code>recordType</code>.
     */
    public AbstractDeleteRecordController(final String baseUrl, final AbstractPageViews views, final SessionRecordTypeEnum recordType,
            final SessionRecordService recordService, final MessageSource messageSource) {
        super(baseUrl, views, recordType, recordService, messageSource);
        successfulDeleteUrl = getBaseUrl() + CommonUrlParts.DELETED_EXTENSION;
    }

    @Transactional
    public String delete(final int recordId, final HttpServletRequest request, final ModelMap model) {
        try {
            getRecordService().delete(recordId);

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, successfulDeleteUrl);
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(e);
            model.addAttribute("error", AbstractDeleteRecordController.DATABASE_ERROR_MESSAGE_CODE);
            return getViews().getList();
        }
    }
}
