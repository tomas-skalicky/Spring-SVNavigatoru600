package com.svnavigatoru600.service.records;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.service.util.File;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderType;
import com.svnavigatoru600.service.util.OtherDocumentRecordUtils;

/**
 * Provides convenient methods to work with {@link OtherDocumentRecord} objects.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class OtherDocumentRecordService extends AbstractDocumentRecordService {

    /**
     * The object which provides a persistence.
     */
    private final OtherDocumentRecordDao otherDocumentDao;

    /**
     * Constructor.
     */
    @Inject
    public OtherDocumentRecordService(OtherDocumentRecordDao otherDocumentDao) {
        super(otherDocumentDao);
        this.otherDocumentDao = otherDocumentDao;
    }

    @Override
    public OtherDocumentRecord findById(int recordId) {
        return this.otherDocumentDao.findById(recordId);
    }

    @Override
    public OtherDocumentRecord findById(int recordId, boolean loadFile) {
        return this.otherDocumentDao.findById(recordId, loadFile);
    }

    @Override
    public OtherDocumentRecord findByIdWithoutFile(int recordId) {
        return this.findById(recordId, false);
    }

    /**
     * Returns all {@link OtherDocumentRecord OtherDocumentRecords} stored in the repository arranged
     * according to their {@link OtherDocumentRecord#getCreationTime() creationTimes} in the given
     * {@link OrderType order}.
     */
    public List<OtherDocumentRecord> findAllOrdered(OrderType order) {
        return this.otherDocumentDao.findAllOrdered(order);
    }

    /**
     * Returns all {@link OtherDocumentRecord OtherDocumentRecords} stored in the repository which are of the
     * given {@link OtherDocumentRecordType type}. The {@link OtherDocumentRecord} are arranged according to
     * their {@link OtherDocumentRecord#getCreationTime() creationTimes} in the given {@link OrderType order}.
     */
    public List<OtherDocumentRecord> findAllOrdered(OtherDocumentRecordType type, OrderType order) {
        return this.otherDocumentDao.findAllOrdered(type, order);
    }

    /**
     * Returns {@link OtherDocumentRecord OtherDocumentRecords} stored in the repository arranged according to
     * their {@link OtherDocumentRecord#getCreationTime() creationTimes} descending.
     * 
     * @param allRecordTypes
     *            If <code>true</code>, all document records are returned. Otherwise, only records which are
     *            of the given {@link OtherDocumentRecordType recordType} are returned.
     * @param recordType
     *            see <code>allRecordTypes</code>
     */
    public List<OtherDocumentRecord> findAllOrdered(boolean allRecordTypes, OtherDocumentRecordType recordType) {
        OrderType order = OrderType.DESCENDING;
        if (allRecordTypes) {
            return this.findAllOrdered(order);
        } else {
            return this.findAllOrdered(recordType, order);
        }
    }

    /**
     * Updates the given {@link OtherDocumentRecord} in the repository. The old version of this document
     * record should be already stored there.
     */
    public void update(OtherDocumentRecord record) {
        this.otherDocumentDao.update(record);
    }

    /**
     * Updates properties of the {@link OtherDocumentRecord} specified by <code>recordToUpdateId</code>,
     * localizes the <code>newType</code>, converts the <code>newAttachedFile</code> to {@link java.sql.Blob
     * Blob} (if <code>isFileReplaced == true</code>) and persists this record into the repository. The old
     * version of this record should be already stored there.
     * 
     * @param recordToUpdateId
     *            ID of the persisted {@link OtherDocumentRecord}
     * @param newRecord
     *            The {@link OtherDocumentRecord} which contains new values of properties of the persisted
     *            record. These values are copied to it.
     * @param newTypes
     *            New types of the persisted record
     * @param isFileReplaced
     *            Indicates whether the attached file of the persisted record has been replaced by other file.
     * @param newAttachedFile
     *            New attached file of the persisted record
     */
    public void update(int recordToUpdateId, OtherDocumentRecord newRecord, boolean[] newTypes,
            boolean isFileReplaced, MultipartFile newAttachedFile, HttpServletRequest request,
            MessageSource messageSource) throws SQLException, IOException {
        OtherDocumentRecord recordToUpdate = this.findByIdWithoutFile(recordToUpdateId);
        recordToUpdate.setName(newRecord.getName());
        recordToUpdate.setDescription(newRecord.getDescription());
        recordToUpdate.setTypes(OtherDocumentRecordUtils.convertIndicatorsToRelations(newTypes,
                recordToUpdateId));

        this.updateRecordWithSaveFileToDatabase(recordToUpdate, isFileReplaced, newAttachedFile);
    }

    /**
     * Updates {@link OtherDocumentRecord recordToUpdate's} properties related to the {@link MultipartFile
     * newAttachedFile} if <code>isFileReplaced == true</code>. Then, persists the record into the repository.
     * The old version of this record should be already stored there.
     * <p>
     * NOTE: Analogy to the
     * {@link #updateRecordWithSaveFileToFileSystem(OtherDocumentRecord, boolean, MultipartFile)
     * updateRecordWithSaveFileToFileSystem} method.
     * 
     * @param recordToUpdate
     *            New persisted {@link OtherDocumentRecord}
     * @param isFileReplaced
     *            Indicates whether the attached file of the persisted record has been replaced by other file.
     * @param newAttachedFile
     *            New attached file of the persisted record
     */
    private void updateRecordWithSaveFileToDatabase(OtherDocumentRecord recordToUpdate,
            boolean isFileReplaced, MultipartFile newAttachedFile) throws SQLException, IOException {
        if (isFileReplaced) {
            String newFileName = newAttachedFile.getOriginalFilename();
            recordToUpdate.setFileName(newFileName);
            recordToUpdate.setFile(File.convertToBlob(newAttachedFile.getBytes()));
        }

        this.update(recordToUpdate);
    }

    /**
     * Updates {@link OtherDocumentRecord recordToUpdate's} properties related to the {@link MultipartFile
     * newAttachedFile} if <code>isFileReplaced == true</code>. Then, persists the record into the repository.
     * The old version of this record should be already stored there.
     * <p>
     * If <code>isFileReplaced == true</code>, deletes the old file just before the persisting of the record
     * and copies the new file to the target folder just after the persisting.
     * <p>
     * NOTE: It is necessary to secure the uniqueness of its filename since all files of records are in one
     * common directory.
     * <p>
     * This method is not used since we were not able to find out where in the MochaHost directory hierarchy
     * we were.
     * <p>
     * NOTE: Analogy to the
     * {@link #updateRecordWithSaveFileToDatabase(OtherDocumentRecord, boolean, MultipartFile)
     * updateRecordWithSaveFileToDatabase} method.
     * 
     * @param recordToUpdate
     *            New persisted {@link OtherDocumentRecord}
     * @param isFileReplaced
     *            Indicates whether the attached file of the persisted record has been replaced by other file.
     * @param newAttachedFile
     *            New attached file of the persisted record
     */
    @SuppressWarnings("unused")
    private void updateRecordWithSaveFileToFileSystem(OtherDocumentRecord recordToUpdate,
            boolean isFileReplaced, MultipartFile newAttachedFile) throws SQLException, IOException {
        if (isFileReplaced) {
            String newFileName = File.getUniqueFileName(newAttachedFile.getOriginalFilename());
            recordToUpdate.setFileName(newFileName);
            newAttachedFile.transferTo(File.getUploadedFile(newFileName));
        }

        this.update(recordToUpdate);

        if (isFileReplaced) {
            String oldFileName = recordToUpdate.getFileName();
            File.getUploadedFile(oldFileName).delete();
        }
    }

    /**
     * Stores the given {@link OtherDocumentRecord} to the repository. If there is already a document record
     * with the same {@link OtherDocumentRecord#getFileName() filename}, throws an exception.
     * 
     * @return The new ID of the given {@link OtherDocumentRecord} generated by the repository
     */
    public int save(OtherDocumentRecord record) {
        return this.otherDocumentDao.save(record);
    }

    /**
     * Since we need ID of the {@link OtherDocumentRecord newRecord} for the creation of
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation
     * OtherDocumentRecordTypeRelations}, we save the new record in two steps.
     * <p>
     * (1) The method converts the <code>attachedFile</code> to {@link java.sql.Blob Blob} and stores the
     * record to the repository. If there is already a record with the same
     * {@link OtherDocumentRecord#getFileName() filename}, throws an exception.
     * <p>
     * (2) Then, the method localizes the <code>recordTypes</code> of the new record, assigns them to the
     * record and persists these modifications.
     * 
     * @param newRecord
     *            New document record
     * @param recordTypes
     *            Types of the new record
     * @param attachedFile
     *            File which is attached to the new record.
     */
    public void save(OtherDocumentRecord newRecord, boolean[] recordTypes, MultipartFile attachedFile,
            HttpServletRequest request, MessageSource messageSource) throws SQLException, IOException {
        String fileName = this.prepareForSaveFileToDatabase(newRecord, attachedFile);

        int recordId = this.save(newRecord);
        LogFactory.getLog(this.getClass()).info(
                String.format("The file '%s' has been successfully uploaded", fileName));

        newRecord.setTypes(OtherDocumentRecordUtils.convertIndicatorsToRelations(recordTypes, recordId));
        this.update(newRecord);
    }

    /**
     * Prepares the {@link OtherDocumentRecord newRecord} for saving the {@link MultipartFile attachedFile} to
     * the database.
     * <p>
     * NOTE: Analogy to the {@link #preparedAndSaveFileToFileSystem(OtherDocumentRecord, MultipartFile)
     * preparedAndSaveFileToFileSystem} method.
     * 
     * @param newRecord
     *            New document record
     * @param attachedFile
     *            File which is attached to the new record.
     * @return New value of the new record's {@link OtherDocumentRecord#getFileName() file name}
     */
    private String prepareForSaveFileToDatabase(OtherDocumentRecord newRecord, MultipartFile attachedFile)
            throws SQLException, IOException {
        String fileName = attachedFile.getOriginalFilename();
        newRecord.setFileName(fileName);
        newRecord.setFile(File.convertToBlob(attachedFile.getBytes()));
        return fileName;
    }

    /**
     * Prepares the {@link OtherDocumentRecord newRecord} for saving and saves the {@link MultipartFile
     * attachedFile} to the file system.
     * <p>
     * NOTE: It is necessary to secure the uniqueness of its filename since all files of records are in one
     * common directory.
     * <p>
     * This method is not used since we were not able to find out where in the MochaHost directory hierarchy
     * we were.
     * <p>
     * NOTE: Analogy to the {@link #prepareForSaveFileToDatabase(OtherDocumentRecord, MultipartFile)
     * prepareForSaveFileToDatabase} method.
     * 
     * @param newRecord
     *            New document record
     * @param attachedFile
     *            File which is attached to the new record.
     * @return New value of the new record's {@link OtherDocumentRecord#getFileName() file name}
     */
    @SuppressWarnings("unused")
    private String preparedAndSaveFileToFileSystem(OtherDocumentRecord newRecord, MultipartFile attachedFile)
            throws SQLException, IOException {
        String fileName = File.getUniqueFileName(attachedFile.getOriginalFilename());
        newRecord.setFileName(fileName);
        attachedFile.transferTo(File.getUploadedFile(fileName));
        return fileName;
    }

    /**
     * Deletes the specified {@link OtherDocumentRecord} together with all its {@link OtherDocumentRecordType
     * types} from the repository. Moreover, deletes the associated {@link java.io.File file}.
     * 
     * @param recordId
     *            The ID of the document record
     */
    public void delete(int recordId) {
        OtherDocumentRecord record = this.findByIdWithoutFile(recordId);
        this.delete(record);
    }

    /**
     * Gets a {@link Map} which for each input {@link OtherDocumentRecord} contains a corresponding localized
     * delete question which is asked before deletion of that record.
     */
    public static Map<OtherDocumentRecord, String> getLocalizedDeleteQuestions(
            List<OtherDocumentRecord> records, HttpServletRequest request, MessageSource messageSource) {
        String messageCode = "other-documents.do-you-really-want-to-delete-document";
        Map<OtherDocumentRecord, String> questions = new HashMap<OtherDocumentRecord, String>();

        for (OtherDocumentRecord record : records) {
            Object[] messageParams = new Object[] { record.getName() };
            questions.put(record,
                    Localization.findLocaleMessage(messageSource, request, messageCode, messageParams));
        }
        return questions;
    }
}
