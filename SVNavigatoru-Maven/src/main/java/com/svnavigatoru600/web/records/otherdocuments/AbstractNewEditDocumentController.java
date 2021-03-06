package com.svnavigatoru600.web.records.otherdocuments;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Validator;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.viewmodel.records.otherdocuments.validator.AbstractOtherDocumentRecordValidator;
import com.svnavigatoru600.web.SendNotificationModelFiller;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which create and edit the {@link com.svnavigatoru600.domain.records.OtherDocumentRecord
 * OtherDocumentRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
public abstract class AbstractNewEditDocumentController extends AbstractOtherDocumentRecordController {

    /**
     * Code of the error message used when the {@link IllegalStateException} or {@link java.io.IOException IOException}
     * is thrown.
     */
    public static final String UPLOAD_FILE_ERROR_MESSAGE_CODE = "file.error-during-saving-file";
    /**
     * Command used in /main-content/records/other-documents/templates/new-edit-record.jsp.
     */
    public static final String COMMAND = "newEditRecordCommand";
    private final Validator validator;
    private final SendNotificationModelFiller sendNotificationModelFiller;

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.OtherDocumentRecord
     * OtherDocumentRecords} of all {@link OtherDocumentRecordTypeEnum OtherDocumentRecordTypes}.
     */
    public AbstractNewEditDocumentController(final String baseUrl, final AbstractPageViews views,
            final OtherDocumentRecordService recordService, final SendNotificationModelFiller sendNotificationModelFiller,
            final AbstractOtherDocumentRecordValidator validator, final MessageSource messageSource) {
        super(baseUrl, views, recordService, messageSource);
        this.validator = validator;
        this.sendNotificationModelFiller = sendNotificationModelFiller;
    }

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.OtherDocumentRecord
     * OtherDocumentRecords} of the given <code>recordType</code>.
     */
    public AbstractNewEditDocumentController(final String baseUrl, final AbstractPageViews views,
            final OtherDocumentRecordTypeEnum recordType, final OtherDocumentRecordService recordService,
            final SendNotificationModelFiller sendNotificationModelFiller, final AbstractOtherDocumentRecordValidator validator,
            final MessageSource messageSource) {
        super(baseUrl, views, recordType, recordService, messageSource);
        this.validator = validator;
        this.sendNotificationModelFiller = sendNotificationModelFiller;
    }

    /**
     * Trivial getter
     */
    protected Validator getValidator() {
        return validator;
    }

    /**
     * Trivial getter
     */
    protected SendNotificationModelFiller getSendNotificationModelFiller() {
        return sendNotificationModelFiller;
    }
}
