package com.svnavigatoru600.web.records.session;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.viewmodel.records.session.validator.AbstractSessionRecordValidator;
import com.svnavigatoru600.web.SendNotificationModelFiller;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which create and edit the {@link com.svnavigatoru600.domain.records.SessionRecord
 * SessionRecords}.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@PreAuthorize("hasRole('ROLE_MEMBER_OF_BOARD')")
public abstract class AbstractNewEditRecordController extends AbstractSessionRecordController {

    /**
     * Code of the error message used when the {@link IllegalStateException} or {@link java.io.IOException IOException}
     * is thrown.
     */
    public static final String UPLOAD_FILE_ERROR_MESSAGE_CODE = "file.error-during-saving-file";
    /**
     * Command used in /main-content/records/session/templates/new-edit-record.jsp.
     */
    public static final String COMMAND = "newEditRecordCommand";
    private final Validator validator;
    private final SendNotificationModelFiller sendNotificationModelFiller;

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.SessionRecord
     * SessionRecords} of all {@link SessionRecordType SessionRecordTypes}.
     */
    public AbstractNewEditRecordController(final String baseUrl, final AbstractPageViews views, final SessionRecordService recordService,
            final SendNotificationModelFiller sendNotificationModelFiller, final AbstractSessionRecordValidator validator,
            final MessageSource messageSource) {
        super(baseUrl, views, recordService, messageSource);
        this.validator = validator;
        this.sendNotificationModelFiller = sendNotificationModelFiller;
    }

    /**
     * Constructs a controller which considers all {@link com.svnavigatoru600.domain.records.SessionRecord
     * SessionRecords} of the given <code>recordType</code>.
     */
    public AbstractNewEditRecordController(final String baseUrl, final AbstractPageViews views, final SessionRecordType recordType,
            final SessionRecordService recordService, final SendNotificationModelFiller sendNotificationModelFiller,
            final AbstractSessionRecordValidator validator, final MessageSource messageSource) {
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

    /**
     * Creates a {@link List} of localized names of {@link SessionRecordType SessionRecordTypes}. The forms which use
     * this controller can access the resulting list.
     * <p>
     * This method is used for filling up the tag <em>radiobuttons</em> and the value of the selected radiobutton is
     * stored to <code>NewEditSessionRecord.newType</code>.
     */
    @ModelAttribute("sessionRecordTypeList")
    public List<String> populateSessionRecordTypeList(final HttpServletRequest request) {
        return SessionRecordService.getLocalizedTypes(request, getMessageSource());
    }
}
