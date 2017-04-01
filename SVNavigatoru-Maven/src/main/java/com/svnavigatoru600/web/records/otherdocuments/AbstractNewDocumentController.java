package com.svnavigatoru600.web.records.otherdocuments;

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

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.service.util.OtherDocumentRecordUtils;
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.viewmodel.records.otherdocuments.NewRecord;
import com.svnavigatoru600.viewmodel.records.otherdocuments.validator.NewRecordValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.SendNotificationModelFiller;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which create the {@link OtherDocumentRecord OtherDocumentRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractNewDocumentController extends AbstractNewEditDocumentController {

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "other-documents.adding-failed-due-to-database-error";
    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord OtherDocumentRecords} of all
     * {@link OtherDocumentRecordType OtherDocumentRecordTypes}.
     */
    public AbstractNewDocumentController(final String baseUrl, final AbstractPageViews views,
            final OtherDocumentRecordService recordService, final SendNotificationModelFiller sendNotificationModelFiller,
            final NewRecordValidator validator, final MessageSource messageSource) {
        super(baseUrl, views, recordService, sendNotificationModelFiller, validator, messageSource);
    }

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord OtherDocumentRecords} of the given
     * <code>recordType</code>.
     */
    public AbstractNewDocumentController(final String baseUrl, final AbstractPageViews views, final OtherDocumentRecordType recordType,
            final OtherDocumentRecordService recordService, final SendNotificationModelFiller sendNotificationModelFiller,
            final NewRecordValidator validator, final MessageSource messageSource) {
        super(baseUrl, views, recordType, recordService, sendNotificationModelFiller, validator, messageSource);
    }

    /**
     * Initializes the form.
     */
    public String initForm(final HttpServletRequest request, final ModelMap model) {

        final NewRecord command = new NewRecord();

        final OtherDocumentRecord record = new OtherDocumentRecord();
        command.setRecord(record);

        // Collection of types is converted to a map. If this controller
        // considers only one special type of records, checkbox of this type
        // is pre-checked.
        final boolean[] newTypes = OtherDocumentRecordUtils.getDefaultArrayOfCheckIndicators();
        if (!isAllRecordTypes()) {
            newTypes[getRecordType().ordinal()] = true;
        }
        command.setNewTypes(newTypes);

        // Sets up all auxiliary (but necessary) maps.
        command.setTypeCheckboxId(OtherDocumentRecordService.getTypeCheckboxId());
        final MessageSource messageSource = getMessageSource();
        command.setLocalizedTypeCheckboxTitles(
                OtherDocumentRecordService.getLocalizedTypeTitles(request, messageSource));

        getSendNotificationModelFiller().populateSendNotificationInInitForm(command, request, messageSource);

        model.addAttribute(AbstractNewEditDocumentController.COMMAND, command);
        return getViews().getNeww();
    }

    public static void displayIt(final java.io.File node) {

        System.out.println(node.getAbsoluteFile());

        if (node.isDirectory()) {
            final String[] subNote = node.list();
            for (final String fileName : subNote) {
                displayIt(new java.io.File(node, fileName));
            }
        }

    }

    /**
     * If values in the form are OK, the new record is stored to the repository. Otherwise, returns back to the form.
     * 
     * @return The name of the view which is to be shown.
     */
    @Transactional
    public String processSubmittedForm(final NewRecord command, final BindingResult result, final SessionStatus status,
            final HttpServletRequest request, final ModelMap model) {

        // Sets up all auxiliary (but necessary) maps.
        command.setTypeCheckboxId(OtherDocumentRecordService.getTypeCheckboxId());
        final MessageSource messageSource = getMessageSource();
        command.setLocalizedTypeCheckboxTitles(
                OtherDocumentRecordService.getLocalizedTypeTitles(request, messageSource));
        getSendNotificationModelFiller().populateSendNotificationInSubmitForm(command, request, messageSource);

        getValidator().validate(command, result);
        if (result.hasErrors()) {
            return getViews().getNeww();
        }

        final OtherDocumentRecord newRecord = command.getRecord();
        try {
            getRecordService().saveAndNotifyUsers(newRecord, command.getNewTypes(), command.getNewFile(),
                    command.getSendNotification().isStatus(), request, messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE,
                    String.format("%s%s", getBaseUrl(), CommonUrlParts.CREATED_EXTENSION));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final IllegalStateException e) {
            logger.error(newRecord, e);
            result.reject(AbstractNewEditDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (final IOException e) {
            logger.error(newRecord, e);
            result.reject(AbstractNewEditDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (final SerialException e) {
            logger.error(newRecord, e);
            result.reject(AbstractNewEditDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (final SQLException e) {
            logger.error(newRecord, e);
            result.reject(AbstractNewEditDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (final DataAccessException e) {
            // We encountered a database problem.
            logger.error(newRecord, e);
            result.reject(AbstractNewDocumentController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return getViews().getNeww();
    }
}
