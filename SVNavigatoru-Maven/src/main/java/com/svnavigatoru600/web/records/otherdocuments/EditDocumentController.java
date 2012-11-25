package com.svnavigatoru600.web.records.otherdocuments;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.service.records.otherdocuments.EditRecord;
import com.svnavigatoru600.service.records.otherdocuments.EditRecordValidator;
import com.svnavigatoru600.service.util.File;
import com.svnavigatoru600.service.util.OtherDocumentRecordUtils;
import com.svnavigatoru600.web.Configuration;
import com.svnavigatoru600.web.records.PageViews;


/**
 * Parent of all controllers which edit the {@link OtherDocumentRecord}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class EditDocumentController extends NewEditDocumentController {

	/**
	 * Code of the error message used when the {@link DataAccessException} is
	 * thrown.
	 */
	public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";

	/**
	 * Constructs a controller which considers all {@link OtherDocumentRecord}s
	 * of all {@link OtherDocumentRecordType}s.
	 */
	public EditDocumentController(String baseUrl, PageViews views, OtherDocumentRecordDao recordDao,
			EditRecordValidator validator, MessageSource messageSource) {
		super(baseUrl, views, recordDao, validator, messageSource);
	}

	/**
	 * Constructs a controller which considers all {@link OtherDocumentRecord}s
	 * of the given <code>recordType</code>.
	 */
	public EditDocumentController(String baseUrl, PageViews views, OtherDocumentRecordType recordType,
			OtherDocumentRecordDao recordDao, EditRecordValidator validator, MessageSource messageSource) {
		super(baseUrl, views, recordType, recordDao, validator, messageSource);
	}

	/**
	 * Initializes the form.
	 * 
	 * @param recordId
	 *            ID of the modified {@link OtherDocumentRecord}
	 */
	public String initForm(int recordId, HttpServletRequest request, ModelMap model) {

		EditRecord command = new EditRecord();

		OtherDocumentRecord record = this.recordDao.findById(recordId, false);
		command.setRecord(record);

		// Collection of types is converted to a map.
		command.setNewTypes(OtherDocumentRecordUtils.getArrayOfCheckIndicators(record.getTypes()));

		// Sets up all auxiliary (but necessary) maps.
		command.setTypeCheckboxId(this.getTypeCheckboxId());
		command.setLocalizedTypeCheckboxTitles(this.getLocalizedTypeCheckboxTitles(request));

		model.addAttribute(EditDocumentController.COMMAND, command);
		return VIEWS.EDIT;
	}

	/**
	 * Initializes the form after the modified data were successfully saved to
	 * the repository and the new file (if it exists) was uploaded.
	 */
	public String initFormAfterSave(int recordId, HttpServletRequest request, ModelMap model) {
		String view = this.initForm(recordId, request, model);
		((EditRecord) model.get(EditDocumentController.COMMAND)).setDataSaved(true);
		return view;
	}

	/**
	 * If values in the form are OK, updates data of the record with the given
	 * <code>recordId</code>. Otherwise, returns back to the form.
	 * 
	 * @return The name of the view which is to be shown.
	 */
	public String processSubmittedForm(EditRecord command, BindingResult result, SessionStatus status, int recordId,
			HttpServletRequest request, ModelMap model) {

		// Sets up all auxiliary (but necessary) maps.
		command.setTypeCheckboxId(this.getTypeCheckboxId());
		command.setLocalizedTypeCheckboxTitles(this.getLocalizedTypeCheckboxTitles(request));

		this.validator.validate(command, result);
		if (result.hasErrors()) {
			command.setFileChanged(false);
			return VIEWS.EDIT;
		}

		// Updates the original data. Modifies the filename to make it unique.
		OtherDocumentRecord oldRecord = this.recordDao.findById(recordId, false);
		OtherDocumentRecord newRecord = command.getRecord();
		oldRecord.setName(newRecord.getName());
		oldRecord.setDescription(newRecord.getDescription());
		oldRecord.setTypes(OtherDocumentRecordUtils.convertIndicatorsToRelations(command.getNewTypes(), recordId));
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
					String.format("%s%d/ulozeno/", this.BASE_URL, recordId));
			return Configuration.REDIRECTION_PAGE;

		} catch (IllegalStateException e) {
			this.logger.error(command, e);
			result.reject(EditDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
		} catch (IOException e) {
			this.logger.error(command, e);
			result.reject(EditDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
		} catch (SerialException e) {
			this.logger.error(command, e);
			result.reject(EditDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
		} catch (SQLException e) {
			this.logger.error(command, e);
			result.reject(EditDocumentController.UPLOAD_FILE_ERROR_MESSAGE_CODE);
		} catch (DataAccessException e) {
			// We encountered a database problem.
			this.logger.error(command, e);
			result.reject(EditDocumentController.DATABASE_ERROR_MESSAGE_CODE);
		}
		return VIEWS.EDIT;
	}
}