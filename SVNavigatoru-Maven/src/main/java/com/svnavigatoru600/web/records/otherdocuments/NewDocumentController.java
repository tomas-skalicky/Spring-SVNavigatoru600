package com.svnavigatoru600.web.records.otherdocuments;

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

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.service.records.otherdocuments.NewRecord;
import com.svnavigatoru600.service.records.otherdocuments.validator.NewRecordValidator;
import com.svnavigatoru600.service.util.File;
import com.svnavigatoru600.service.util.OtherDocumentRecordUtils;
import com.svnavigatoru600.web.Configuration;
import com.svnavigatoru600.web.records.PageViews;

/**
 * Parent of all controllers which create the {@link OtherDocumentRecord}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class NewDocumentController extends NewEditDocumentController {

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "other-documents.adding-failed-due-to-database-error";

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord}s of all
     * {@link OtherDocumentRecordType}s.
     */
    public NewDocumentController(String baseUrl, PageViews views, OtherDocumentRecordDao recordDao,
            NewRecordValidator validator, MessageSource messageSource) {
        super(baseUrl, views, recordDao, validator, messageSource);
    }

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord}s of the given
     * <code>recordType</code>.
     */
    public NewDocumentController(String baseUrl, PageViews views, OtherDocumentRecordType recordType,
            OtherDocumentRecordDao recordDao, NewRecordValidator validator, MessageSource messageSource) {
        super(baseUrl, views, recordType, recordDao, validator, messageSource);
    }

    /**
     * Initializes the form.
     */
    public String initForm(HttpServletRequest request, ModelMap model) {

        NewRecord command = new NewRecord();

        OtherDocumentRecord record = new OtherDocumentRecord();
        command.setRecord(record);

        // Collection of types is converted to a map. If this controller
        // considers only one special type of records, checkbox of this type
        // is pre-checked.
        boolean[] newTypes = OtherDocumentRecordUtils.getDefaultArrayOfCheckIndicators();
        if (!this.allRecordTypes) {
            newTypes[this.RECORD_TYPE.ordinal()] = true;
        }
        command.setNewTypes(newTypes);

        // Sets up all auxiliary (but necessary) maps.
        command.setTypeCheckboxId(this.getTypeCheckboxId());
        command.setLocalizedTypeCheckboxTitles(this.getLocalizedTypeCheckboxTitles(request));

        model.addAttribute(NewDocumentController.COMMAND, command);
        return VIEWS.NEW;
    }

    public static void displayIt(java.io.File node) {

        System.out.println(node.getAbsoluteFile());

        if (node.isDirectory()) {
            final String[] subNote = node.list();
            for (String filename : subNote) {
                displayIt(new java.io.File(node, filename));
            }
        }

    }

    /**
     * If values in the form are OK, the new record is stored to the repository. Otherwise, returns back to
     * the form.
     * 
     * @return The name of the view which is to be shown.
     */
    @Transactional
    public String processSubmittedForm(NewRecord command, BindingResult result, SessionStatus status,
            HttpServletRequest request, ModelMap model) {

        // Sets up all auxiliary (but necessary) maps.
        command.setTypeCheckboxId(this.getTypeCheckboxId());
        command.setLocalizedTypeCheckboxTitles(this.getLocalizedTypeCheckboxTitles(request));

        this.validator.validate(command, result);
        if (result.hasErrors()) {
            return VIEWS.NEW;
        }

        // Updates the data of the new record. Modifies the filename to make
        // it unique.
        OtherDocumentRecord newRecord = command.getRecord();
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

            int recordId = this.recordDao.save(newRecord);
            this.logger.info(String.format("The file '%s' has been successfully uploaded", fileName));

            // Since we need ID of the new record for the creation of
            // OtherDocumentRecordTypeRelations, we save the record in two
            // steps. The second step (the following one) is necessary because
            // of the types.
            newRecord.setTypes(OtherDocumentRecordUtils.convertIndicatorsToRelations(command.getNewTypes(),
                    recordId));
            this.recordDao.update(newRecord);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE,
                    String.format("%svytvoreno/", this.BASE_URL));
            return Configuration.REDIRECTION_PAGE;

        } catch (IllegalStateException e) {
            this.logger.error(newRecord, e);
            result.reject(NewDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (IOException e) {
            this.logger.error(newRecord, e);
            result.reject(NewDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (SerialException e) {
            this.logger.error(newRecord, e);
            result.reject(NewDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (SQLException e) {
            this.logger.error(newRecord, e);
            result.reject(NewDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(newRecord, e);
            result.reject(NewDocumentController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return VIEWS.NEW;
    }
}
