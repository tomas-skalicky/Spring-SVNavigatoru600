package com.svnavigatoru600.web.records.session;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordTypeEnum;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.viewmodel.records.session.NewSessionRecord;
import com.svnavigatoru600.viewmodel.records.session.validator.NewSessionRecordValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.SendNotificationModelFiller;
import com.svnavigatoru600.web.records.AbstractPageViews;
import com.svnavigatoru600.web.url.CommonUrlParts;

/**
 * Parent of all controllers which create the {@link SessionRecord SessionRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractNewRecordController extends AbstractNewEditRecordController {

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "session-records.adding-failed-due-to-database-error";
    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * Constructs a controller which considers all {@link SessionRecord SessionRecords} of all {@link SessionRecordTypeEnum
     * SessionRecordTypes}.
     */
    public AbstractNewRecordController(final String baseUrl, final AbstractPageViews views, final SessionRecordService recordService,
            final SendNotificationModelFiller sendNotificationModelFiller, final NewSessionRecordValidator validator,
            final MessageSource messageSource) {
        super(baseUrl, views, recordService, sendNotificationModelFiller, validator, messageSource);
    }

    /**
     * Constructs a controller which considers all {@link SessionRecord SessionRecords} of the given
     * <code>recordType</code>.
     */
    public AbstractNewRecordController(final String baseUrl, final AbstractPageViews views, final SessionRecordTypeEnum recordType,
            final SessionRecordService recordService, final SendNotificationModelFiller sendNotificationModelFiller,
            final NewSessionRecordValidator validator, final MessageSource messageSource) {
        super(baseUrl, views, recordType, recordService, sendNotificationModelFiller, validator, messageSource);
    }

    /**
     * Initializes the form.
     */
    public String initForm(final SessionRecordTypeEnum recordType, final HttpServletRequest request, final ModelMap model) {

        final NewSessionRecord command = new NewSessionRecord();

        final SessionRecord record = new SessionRecord();
        command.setRecord(record);

        final MessageSource messageSource = getMessageSource();
        command.setNewType(Localization.findLocaleMessage(messageSource, request, recordType.getLocalizationCode()));

        getSendNotificationModelFiller().populateSendNotificationInInitForm(command, request, messageSource);

        model.addAttribute(AbstractNewEditRecordController.COMMAND, command);
        return getViews().getNeww();
    }

    /**
     * If values in the form are OK, the new record is stored to the repository. Otherwise, returns back to the form.
     * 
     * @return The name of the view which is to be shown.
     */
    @Transactional
    public String processSubmittedForm(final NewSessionRecord command, final BindingResult result, final SessionStatus status,
            final HttpServletRequest request, final ModelMap model) {

        final MessageSource messageSource = getMessageSource();
        getSendNotificationModelFiller().populateSendNotificationInSubmitForm(command, request, messageSource);

        getValidator().validate(command, result);
        if (result.hasErrors()) {
            return getViews().getNeww();
        }

        final SessionRecord newRecord = command.getRecord();
        try {
            getRecordService().saveAndNotifyUsers(newRecord, command.getNewType(), command.getNewFile(),
                    command.getSendNotification().isStatus(), request, messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE,
                    String.format("%s%s", getBaseUrl(), CommonUrlParts.CREATED_EXTENSION));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final IllegalStateException e) {
            logger.error(newRecord, e);
            result.reject(AbstractNewEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (final IOException e) {
            logger.error(newRecord, e);
            result.reject(AbstractNewEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (final SerialException e) {
            logger.error(newRecord, e);
            result.reject(AbstractNewEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (final SQLException e) {
            logger.error(newRecord, e);
            result.reject(AbstractNewEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (final DataAccessException e) {
            // We encountered a database problem.
            logger.error(newRecord, e);
            result.reject(AbstractNewRecordController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return getViews().getNeww();
    }
}
