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
import com.svnavigatoru600.service.records.session.validator.NewSessionRecordValidator;
import com.svnavigatoru600.service.util.File;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.viewmodel.records.session.NewSessionRecord;
import com.svnavigatoru600.web.Configuration;
import com.svnavigatoru600.web.records.PageViews;

/**
 * Parent of all controllers which create the {@link SessionRecord}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class NewRecordController extends NewEditRecordController {

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "session-records.adding-failed-due-to-database-error";

    /**
     * Constructs a controller which considers all {@link SessionRecord}s of all {@link SessionRecordType}s.
     */
    public NewRecordController(String baseUrl, PageViews views, SessionRecordDao recordDao,
            NewSessionRecordValidator validator, MessageSource messageSource) {
        super(baseUrl, views, recordDao, validator, messageSource);
    }

    /**
     * Constructs a controller which considers all {@link SessionRecord}s of the given <code>recordType</code>
     * .
     */
    public NewRecordController(String baseUrl, PageViews views, SessionRecordType recordType,
            SessionRecordDao recordDao, NewSessionRecordValidator validator, MessageSource messageSource) {
        super(baseUrl, views, recordType, recordDao, validator, messageSource);
    }

    /**
     * Initializes the form.
     */
    public String initForm(SessionRecordType recordType, HttpServletRequest request, ModelMap model) {

        NewSessionRecord command = new NewSessionRecord();

        SessionRecord record = new SessionRecord();
        command.setRecord(record);

        command.setNewType(Localization.findLocaleMessage(this.messageSource, request,
                recordType.getLocalizationCode()));

        model.addAttribute(NewRecordController.COMMAND, command);
        return VIEWS.NEW;
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

        this.validator.validate(command, result);
        if (result.hasErrors()) {
            return VIEWS.NEW;
        }

        // Updates the data of the new record. Modifies the filename to make
        // it unique.
        SessionRecord newRecord = command.getRecord();
        newRecord.setType(SessionRecordType.valueOfAccordingLocalization(command.getNewType(),
                this.messageSource, request));
        MultipartFile attachedFile = command.getNewFile();
        String fileName = attachedFile.getOriginalFilename();
        // /////////////////////////////////////////////////////////////////
        // Store in the FILESYSTEM
        // --------------------------------------------------------------
        // If the file is stored to the filesystem, we have to secure the
        // uniqueness of its filename (all files are in one common directory).
        // If the file is stored in the DB, this operation is not necessary.
        // --------------------------------------------------------------
        // fileName =
        // File.getUniqueFileName(attachedFile.getOriginalFilename());
        // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        newRecord.setFileName(fileName);

        try {
            // /////////////////////////////////////////////////////////////////
            // Store in the FILESYSTEM
            // --------------------------------------------------------------
            // Copies the file to the target folder and creates the record in
            // the repository.
            // The direct copy is disabled since we were not able to find out
            // where in the MochaHost directory hierarchy we were.
            // --------------------------------------------------------------
            // java.io.File destinationFile = File.getUploadedFile(fileName);
            // attachedFile.transferTo(destinationFile);
            // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

            // /////////////////////////////////////////////////////////////////
            // Store in the DB
            // --------------------------------------------------------------
            Blob blobFile = File.convertToBlob(attachedFile.getBytes());
            newRecord.setFile(blobFile);
            // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

            this.recordDao.save(newRecord);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE,
                    String.format("%svytvoreno/", this.BASE_URL));
            return Configuration.REDIRECTION_PAGE;

        } catch (IllegalStateException e) {
            this.logger.error(newRecord, e);
            result.reject(NewRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (IOException e) {
            this.logger.error(newRecord, e);
            result.reject(NewRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (SerialException e) {
            this.logger.error(newRecord, e);
            result.reject(NewRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (SQLException e) {
            this.logger.error(newRecord, e);
            result.reject(NewRecordController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(newRecord, e);
            result.reject(NewRecordController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return VIEWS.NEW;
    }
}
