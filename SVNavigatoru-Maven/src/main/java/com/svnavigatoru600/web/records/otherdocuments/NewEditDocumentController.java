package com.svnavigatoru600.web.records.otherdocuments;

import java.io.IOException;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.service.records.otherdocuments.validator.OtherDocumentRecordValidator;
import com.svnavigatoru600.web.records.PageViews;

/**
 * Parent of all controllers which create and edit the {@link OtherDocumentRecord}s.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
public abstract class NewEditDocumentController extends OtherDocumentRecordController {

    /**
     * Code of the error message used when the {@link IllegalStateException} or {@link IOException} is thrown.
     */
    public static final String UPLOAD_FILE_ERROR_MESSAGE_CODE = "file.error-during-saving-file";
    /**
     * Command used in /main-content/records/other-documents/templates/new-edit-record.jsp.
     */
    public static final String COMMAND = "newEditRecordCommand";
    protected Validator validator;

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord}s of all
     * {@link OtherDocumentRecordType}s.
     */
    public NewEditDocumentController(String baseUrl, PageViews views, OtherDocumentRecordDao recordDao,
            OtherDocumentRecordValidator validator, MessageSource messageSource) {
        super(baseUrl, views, recordDao, messageSource);
        this.validator = validator;
    }

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord}s of the given
     * <code>recordType</code>.
     */
    public NewEditDocumentController(String baseUrl, PageViews views, OtherDocumentRecordType recordType,
            OtherDocumentRecordDao recordDao, OtherDocumentRecordValidator validator,
            MessageSource messageSource) {
        super(baseUrl, views, recordType, recordDao, messageSource);
        this.validator = validator;
    }
}
