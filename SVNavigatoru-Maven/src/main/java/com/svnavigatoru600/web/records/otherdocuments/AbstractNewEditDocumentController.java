package com.svnavigatoru600.web.records.otherdocuments;

import java.io.IOException;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.service.records.otherdocuments.validator.AbstractOtherDocumentRecordValidator;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which create and edit the {@link OtherDocumentRecord OtherDocumentRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
public abstract class AbstractNewEditDocumentController extends AbstractOtherDocumentRecordController {

    /**
     * Code of the error message used when the {@link IllegalStateException} or {@link IOException} is thrown.
     */
    public static final String UPLOAD_FILE_ERROR_MESSAGE_CODE = "file.error-during-saving-file";
    /**
     * Command used in /main-content/records/other-documents/templates/new-edit-record.jsp.
     */
    public static final String COMMAND = "newEditRecordCommand";
    private Validator validator;

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord OtherDocumentRecords} of all
     * {@link OtherDocumentRecordType OtherDocumentRecordTypes}.
     */
    public AbstractNewEditDocumentController(String baseUrl, AbstractPageViews views,
            OtherDocumentRecordDao recordDao, AbstractOtherDocumentRecordValidator validator,
            MessageSource messageSource) {
        super(baseUrl, views, recordDao, messageSource);
        this.validator = validator;
    }

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord OtherDocumentRecords} of the
     * given <code>recordType</code>.
     */
    public AbstractNewEditDocumentController(String baseUrl, AbstractPageViews views,
            OtherDocumentRecordType recordType, OtherDocumentRecordDao recordDao,
            AbstractOtherDocumentRecordValidator validator, MessageSource messageSource) {
        super(baseUrl, views, recordType, recordDao, messageSource);
        this.validator = validator;
    }

    /**
     * Trivial getter
     */
    protected Validator getValidator() {
        return this.validator;
    }
}
