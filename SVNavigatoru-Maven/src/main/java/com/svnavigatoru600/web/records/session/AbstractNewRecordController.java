package com.svnavigatoru600.web.records.session;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.viewmodel.records.session.NewSessionRecord;
import com.svnavigatoru600.viewmodel.records.session.validator.NewSessionRecordValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.SendNotificationModelFiller;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which create the {@link SessionRecord SessionRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractNewRecordController extends AbstractNewEditRecordController {

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "session-records.adding-failed-due-to-database-error";
    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * Constructs a controller which considers all {@link SessionRecord SessionRecords} of all
     * {@link SessionRecordType SessionRecordTypes}.
     */
    public AbstractNewRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordService recordService, SendNotificationModelFiller sendNotificationModelFiller,
            NewSessionRecordValidator validator, MessageSource messageSource) {
        super(baseUrl, views, recordService, sendNotificationModelFiller, validator, messageSource);
    }

    /**
     * Constructs a controller which considers all {@link SessionRecord SessionRecords} of the given
     * <code>recordType</code>.
     */
    public AbstractNewRecordController(String baseUrl, AbstractPageViews views, SessionRecordType recordType,
            SessionRecordService recordService, SendNotificationModelFiller sendNotificationModelFiller,
            NewSessionRecordValidator validator, MessageSource messageSource) {
        super(baseUrl, views, recordType, recordService, sendNotificationModelFiller, validator,
                messageSource);
    }

    /**
     * Initializes the form.
     */
    public String initForm(SessionRecordType recordType, HttpServletRequest request, ModelMap model) {

        NewSessionRecord command = new NewSessionRecord();

        SessionRecord record = new SessionRecord();
        command.setRecord(record);

        MessageSource messageSource = this.getMessageSource();
        command.setNewType(Localization.findLocaleMessage(messageSource, request,
                recordType.getLocalizationCode()));

        this.getSendNotificationModelFiller().populateSendNotificationInInitForm(command, request,
                messageSource);

        model.addAttribute(AbstractNewEditRecordController.COMMAND, command);
        return this.getViews().getNeww();
    }

    /**
     * If values in the form are OK, the new record is stored to the repository. Otherwise, returns back to
     * the form.
     * 
     * @return The name of the view which is to be shown.
     */
    @Transactional
    public String processSubmittedForm(NewSessionRecord command, BindingResult result, SessionStatus status,
            HttpServletRequest request, ModelMap model) {

        MessageSource messageSource = this.getMessageSource();
        this.getSendNotificationModelFiller().populateSendNotificationInSubmitForm(command, request,
                messageSource);

        this.getValidator().validate(command, result);
        if (result.hasErrors()) {
            return this.getViews().getNeww();
        }

        SessionRecord newRecord = command.getRecord();
        try {
            this.getRecordService().saveAndNotifyUsers(newRecord, command.getNewType(), command.getNewFile(),
                    command.getSendNotification().isStatus(), request, messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE,
                    String.format("%s%s", this.getBaseUrl(), CommonUrlParts.CREATED_EXTENSION));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (IllegalStateException e) {
            this.logger.error(newRecord, e);
            result.reject(AbstractNewEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (IOException e) {
            this.logger.error(newRecord, e);
            result.reject(AbstractNewEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (SerialException e) {
            this.logger.error(newRecord, e);
            result.reject(AbstractNewEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (SQLException e) {
            this.logger.error(newRecord, e);
            result.reject(AbstractNewEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(newRecord, e);
            result.reject(AbstractNewRecordController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return this.getViews().getNeww();
    }
}
