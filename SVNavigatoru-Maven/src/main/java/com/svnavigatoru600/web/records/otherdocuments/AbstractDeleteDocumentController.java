package com.svnavigatoru600.web.records.otherdocuments;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which delete the {@link com.svnavigatoru600.domain.records.OtherDocumentRecord
 * OtherDocumentRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
public abstract class AbstractDeleteDocumentController extends AbstractOtherDocumentRecordController {

    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "other-documents.deletion-failed-due-to-database-error";
    private final String successfulDeleteUrl;

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.OtherDocumentRecord
     * OtherDocumentRecords} of all {@link OtherDocumentRecordTypeEnum OtherDocumentRecordTypes}.
     */
    public AbstractDeleteDocumentController(final String baseUrl, final AbstractPageViews views,
            final OtherDocumentRecordService recordService, final MessageSource messageSource) {
        // Note that allRecordTypes is set up during the creation of the parent.
        super(baseUrl, views, recordService, messageSource);
        successfulDeleteUrl = getBaseUrl() + CommonUrlParts.DELETED_EXTENSION;
    }

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.OtherDocumentRecord
     * OtherDocumentRecords} of the given <code>recordType</code>.
     */
    public AbstractDeleteDocumentController(final String baseUrl, final AbstractPageViews views, final OtherDocumentRecordTypeEnum recordType,
            final OtherDocumentRecordService recordService, final MessageSource messageSource) {
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
            model.addAttribute("error", AbstractDeleteDocumentController.DATABASE_ERROR_MESSAGE_CODE);
            return getViews().getList();
        }
    }
}
