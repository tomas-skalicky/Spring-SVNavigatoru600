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
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.service.util.OtherDocumentRecordUtils;
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.viewmodel.records.otherdocuments.EditRecord;
import com.svnavigatoru600.viewmodel.records.otherdocuments.validator.EditRecordValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.SendNotificationModelFiller;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which edit the {@link OtherDocumentRecord OtherDocumentRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractEditDocumentController extends AbstractNewEditDocumentController {

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";
    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord OtherDocumentRecords} of all
     * {@link OtherDocumentRecordTypeEnum OtherDocumentRecordTypes}.
     */
    public AbstractEditDocumentController(final String baseUrl, final AbstractPageViews views,
            final OtherDocumentRecordService recordService, final SendNotificationModelFiller sendNotificationModelFiller,
            final EditRecordValidator validator, final MessageSource messageSource) {
        super(baseUrl, views, recordService, sendNotificationModelFiller, validator, messageSource);
    }

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord OtherDocumentRecords} of the given
     * <code>recordType</code>.
     */
    public AbstractEditDocumentController(final String baseUrl, final AbstractPageViews views, final OtherDocumentRecordTypeEnum recordType,
            final OtherDocumentRecordService recordService, final SendNotificationModelFiller sendNotificationModelFiller,
            final EditRecordValidator validator, final MessageSource messageSource) {
        super(baseUrl, views, recordType, recordService, sendNotificationModelFiller, validator, messageSource);
    }

    /**
     * Initializes the form.
     * 
     * @param recordId
     *            ID of the modified {@link OtherDocumentRecord}
     */
    public String initForm(final int recordId, final HttpServletRequest request, final ModelMap model) {

        final EditRecord command = new EditRecord();

        final OtherDocumentRecord record = getRecordService().findByIdWithoutFile(recordId);
        command.setRecord(record);

        // Collection of types is converted to a map.
        command.setNewTypes(OtherDocumentRecordUtils.getArrayOfCheckIndicators(record.getTypes()));

        // Sets up all auxiliary (but necessary) maps.
        command.setTypeCheckboxId(OtherDocumentRecordService.getTypeCheckboxId());
        final MessageSource messageSource = getMessageSource();
        command.setLocalizedTypeCheckboxTitles(
                OtherDocumentRecordService.getLocalizedTypeTitles(request, messageSource));

        getSendNotificationModelFiller().populateSendNotificationInInitForm(command, request, messageSource);

        model.addAttribute(AbstractNewEditDocumentController.COMMAND, command);
        return getViews().getEdit();
    }

    /**
     * Initializes the form after the modified data were successfully saved to the repository and the new file (if it
     * exists) was uploaded.
     */
    public String initFormAfterSave(final int recordId, final HttpServletRequest request, final ModelMap model) {
        final String view = initForm(recordId, request, model);
        ((EditRecord) model.get(AbstractNewEditDocumentController.COMMAND)).setDataSaved(true);
        return view;
    }

    /**
     * If values in the form are OK, updates data of the record with the given <code>recordId</code>. Otherwise, returns
     * back to the form.
     * 
     * @return The name of the view which is to be shown.
     */
    @Transactional
    public String processSubmittedForm(final EditRecord command, final BindingResult result, final SessionStatus status, final int recordId,
            final HttpServletRequest request, final ModelMap model) {

        // Sets up all auxiliary (but necessary) maps.
        command.setTypeCheckboxId(OtherDocumentRecordService.getTypeCheckboxId());
        final MessageSource messageSource = getMessageSource();
        command.setLocalizedTypeCheckboxTitles(
                OtherDocumentRecordService.getLocalizedTypeTitles(request, messageSource));
        getSendNotificationModelFiller().populateSendNotificationInSubmitForm(command, request, messageSource);

        getValidator().validate(command, result);
        if (result.hasErrors()) {
            command.setFileChanged(false);
            return getViews().getEdit();
        }

        try {
            getRecordService().updateAndNotifyUsers(recordId, command.getRecord(), command.getNewTypes(),
                    command.isFileChanged(), command.getNewFile(), command.getSendNotification().isStatus(), request,
                    messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE,
                    String.format("%s%d/%s", getBaseUrl(), recordId, CommonUrlParts.SAVED_EXTENSION));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final IllegalStateException e) {
            logger.error(command, e);
            result.reject(AbstractNewEditDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (final IOException e) {
            logger.error(command, e);
            result.reject(AbstractNewEditDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (final SerialException e) {
            logger.error(command, e);
            result.reject(AbstractNewEditDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (final SQLException e) {
            logger.error(command, e);
            result.reject(AbstractNewEditDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (final DataAccessException e) {
            // We encountered a database problem.
            logger.error(command, e);
            result.reject(AbstractEditDocumentController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return getViews().getEdit();
    }
}
