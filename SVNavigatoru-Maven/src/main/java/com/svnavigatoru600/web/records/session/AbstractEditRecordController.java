package com.svnavigatoru600.web.records.session;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.service.records.session.validator.EditSessionRecordValidator;
import com.svnavigatoru600.service.util.File;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.viewmodel.records.session.EditSessionRecord;
import com.svnavigatoru600.web.Configuration;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which edit the {@link SessionRecord}s.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractEditRecordController extends AbstractNewEditRecordController {

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";

    /**
     * Constructs a controller which considers all {@link SessionRecord}s of all {@link SessionRecordType}s.
     */
    public AbstractEditRecordController(String baseUrl, AbstractPageViews views, SessionRecordDao recordDao,
            EditSessionRecordValidator validator, MessageSource messageSource) {
        super(baseUrl, views, recordDao, validator, messageSource);
    }

    /**
     * Constructs a controller which considers all {@link SessionRecord}s of the given <code>recordType</code>
     * .
     */
    public AbstractEditRecordController(String baseUrl, AbstractPageViews views,
            SessionRecordType recordType, SessionRecordDao recordDao, EditSessionRecordValidator validator,
            MessageSource messageSource) {
        super(baseUrl, views, recordType, recordDao, validator, messageSource);
    }

    /**
     * Initializes the form.
     * 
     * @param recordId
     *            ID of the modified {@link SessionRecord}
     */
    public String initForm(int recordId, HttpServletRequest request, ModelMap model) {

        EditSessionRecord command = new EditSessionRecord();

        SessionRecord record = this.recordDao.findById(recordId, false);
        command.setRecord(record);

        command.setNewType(Localization.findLocaleMessage(this.messageSource, request, record.getTypedType()
                .getLocalizationCode()));

        model.addAttribute(AbstractEditRecordController.COMMAND, command);
        return this.views.edit;
    }

    /**
     * Initializes the form after the modified data were successfully saved to the repository and the new file
     * (if it exists) was uploaded.
     */
    public String initFormAfterSave(final int recordId, final HttpServletRequest request, final ModelMap model) {
        final String view = this.initForm(recordId, request, model);
        ((EditSessionRecord) model.get(AbstractEditRecordController.COMMAND)).setDataSaved(true);
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

        this.validator.validate(command, result);
        if (result.hasErrors()) {
            command.setFileChanged(false);
            return this.views.edit;
        }

        // Updates the original data. Modifies the filename to make it unique.
        SessionRecord oldRecord = this.recordDao.findById(recordId, false);
        SessionRecord newRecord = command.getRecord();
        oldRecord.setSessionDate(newRecord.getSessionDate());
        oldRecord.setDiscussedTopics(newRecord.getDiscussedTopics());
        oldRecord.setType(SessionRecordType.valueOfAccordingLocalization(command.getNewType(),
                this.messageSource, request));
        boolean isFileChanged = command.isFileChanged();
        MultipartFile newAttachedFile = null;
        String newFileName = null;

        try {
            // /////////////////////////////////////////////////////////////////
            // Store in the FILESYSTEM
            // --------------------------------------------------------------
            // String oldFileName = oldRecord.getFileName();
            // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            if (isFileChanged) {
                newAttachedFile = command.getNewFile();
                newFileName = newAttachedFile.getOriginalFilename();
                // /////////////////////////////////////////////////////////////////
                // Store in the FILESYSTEM
                // --------------------------------------------------------------
                // newFileName = File.getUniqueFileName(newFileName);
                // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

                // /////////////////////////////////////////////////////////////////
                // Store in the DB
                // --------------------------------------------------------------
                Blob blobFile = File.convertToBlob(newAttachedFile.getBytes());
                oldRecord.setFile(blobFile);
                // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
                oldRecord.setFileName(newFileName);
            }

            // If the file has changed, deletes the old one and copies the new
            // file to the target folder. Afterwards, updates the record in the
            // repository.
            // /////////////////////////////////////////////////////////////////
            // Store in the FILESYSTEM
            // --------------------------------------------------------------
            // if (isFileChanged) {
            // java.io.File destinationFile = File.getUploadedFile(newFileName);
            // newAttachedFile.transferTo(destinationFile);
            // }
            // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            this.recordDao.update(oldRecord);
            // /////////////////////////////////////////////////////////////////
            // Store in the FILESYSTEM
            // --------------------------------------------------------------
            // if (isFileChanged) {
            // java.io.File oldFile = File.getUploadedFile(oldFileName);
            // oldFile.delete();
            // }
            // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE,
                    String.format("%s%d/ulozeno/", this.baseUrl, recordId));
            return Configuration.REDIRECTION_PAGE;

        } catch (IllegalStateException e) {
            this.logger.error(command, e);
            result.reject(AbstractEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (IOException e) {
            this.logger.error(command, e);
            result.reject(AbstractEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (SerialException e) {
            this.logger.error(command, e);
            result.reject(AbstractEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (SQLException e) {
            this.logger.error(command, e);
            result.reject(AbstractEditRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(command, e);
            result.reject(AbstractEditRecordController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return this.views.edit;
    }
}
