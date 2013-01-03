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
import com.svnavigatoru600.viewmodel.records.session.EditSessionRecord;
import com.svnavigatoru600.viewmodel.records.session.validator.EditSessionRecordValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which edit the {@link SessionRecord SessionRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractEditRecordController extends AbstractNewEditRecordController {

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";
    private final Log logger = LogFactory.getLog(this.getClass());

    /**
     * Constructs a controller which considers all {@link SessionRecord SessionRecords} of all
     * {@link SessionRecordType SessionRecordTypes}.
     */
    public AbstractEditRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordService recordService, EditSessionRecordValidator validator,
            MessageSource messageSource) {
        super(baseUrl, views, recordService, validator, messageSource);
    }

    /**
     * Constructs a controller which considers all {@link SessionRecord SessionRecords} of the given
     * <code>recordType</code> .
     */
    public AbstractEditRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordType recordType, SessionRecordService recordService,
            EditSessionRecordValidator validator, MessageSource messageSource) {
        super(baseUrl, views, recordType, recordService, validator, messageSource);
    }

    /**
     * Initializes the form.
     * 
     * @param recordId
     *            ID of the modified {@link SessionRecord}
     */
    public String initForm(int recordId, HttpServletRequest request, ModelMap model) {

        EditSessionRecord command = new EditSessionRecord();

        SessionRecord record = this.getRecordService().findByIdWithoutFile(recordId);
        command.setRecord(record);

        command.setNewType(Localization.findLocaleMessage(this.getMessageSource(), request, record
                .getTypedType().getLocalizationCode()));

        model.addAttribute(AbstractNewEditRecordController.COMMAND, command);
        return this.getViews().getEdit();
    }

    /**
     * Initializes the form after the modified data were successfully saved to the repository and the new file
     * (if it exists) was uploaded.
     */
    public String initFormAfterSave(int recordId, HttpServletRequest request, ModelMap model) {
        String view = this.initForm(recordId, request, model);
        ((EditSessionRecord) model.get(AbstractNewEditRecordController.COMMAND)).setDataSaved(true);
        return view;
    }

    /**
     * If values in the form are OK, updates data of the record with the given <code>recordId</code>.
     * Otherwise, returns back to the form.
     * 
     * @return The name of the view which is to be shown.
     */
    @Transactional
    public String processSubmittedForm(EditSessionRecord command, BindingResult result, SessionStatus status,
            int recordId, HttpServletRequest request, ModelMap model) {

        this.getValidator().validate(command, result);
        if (result.hasErrors()) {
            command.setFileChanged(false);
            return this.getViews().getEdit();
        }

        try {
            this.getRecordService().update(recordId, command.getRecord(), command.getNewType(),
                    command.isFileChanged(), command.getNewFile(), request, this.getMessageSource());

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE,
                    String.format("%s%d/ulozeno/", this.getBaseUrl(), recordId));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (IllegalStateException e) {
            this.logger.error(command, e);
            result.reject(AbstractNewEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (IOException e) {
            this.logger.error(command, e);
            result.reject(AbstractNewEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (SerialException e) {
            this.logger.error(command, e);
            result.reject(AbstractNewEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (SQLException e) {
            this.logger.error(command, e);
            result.reject(AbstractNewEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(command, e);
            result.reject(AbstractEditRecordController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return this.getViews().getEdit();
    }
}
