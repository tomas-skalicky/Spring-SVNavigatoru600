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
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.domain.users.AuthorityTypeEnum;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.service.util.File;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderTypeEnum;
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
     * Assembles notification emails and sends them to authorized {@link User users}.
     */
    private OtherDocumentRecordNotificationEmailService emailService;

    @Inject
    public OtherDocumentRecordService(final OtherDocumentRecordDao otherDocumentDao) {
        super(otherDocumentDao);
        this.otherDocumentDao = otherDocumentDao;
    }

    @Inject
    public void setEmailService(final OtherDocumentRecordNotificationEmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public OtherDocumentRecord findById(final int recordId) {
        return otherDocumentDao.findById(recordId);
    }

    @Override
    public OtherDocumentRecord findById(final int recordId, final boolean loadFile) {
        return otherDocumentDao.findById(recordId, loadFile);
    }

    @Override
    public OtherDocumentRecord findByIdWithoutFile(final int recordId) {
        return this.findById(recordId, false);
    }

    /**
     * Returns all {@link OtherDocumentRecord OtherDocumentRecords} stored in the repository arranged according to their
     * {@link OtherDocumentRecord#getCreationTime() creationTimes} in the given {@link OrderTypeEnum order}.
     */
    public List<OtherDocumentRecord> findAllOrdered(final OrderTypeEnum order) {
        return otherDocumentDao.findAllOrdered(order);
    }

    /**
     * Returns all {@link OtherDocumentRecord OtherDocumentRecords} stored in the repository which are of the given
     * {@link OtherDocumentRecordTypeEnum type}. The {@link OtherDocumentRecord} are arranged according to their
     * {@link OtherDocumentRecord#getCreationTime() creationTimes} in the given {@link OrderTypeEnum order}.
     */
    public List<OtherDocumentRecord> findAllOrdered(final OtherDocumentRecordTypeEnum type, final OrderTypeEnum order) {
        return otherDocumentDao.findAllOrdered(type, order);
    }

    /**
     * Returns {@link OtherDocumentRecord OtherDocumentRecords} stored in the repository arranged according to their
     * {@link OtherDocumentRecord#getCreationTime() creationTimes} descending.
     *
     * @param allRecordTypes
     *            If <code>true</code>, all document records are returned. Otherwise, only records which are of the
     *            given {@link OtherDocumentRecordTypeEnum recordType} are returned.
     * @param recordType
     *            see <code>allRecordTypes</code>
     */
    public List<OtherDocumentRecord> findAllOrdered(final boolean allRecordTypes,
            final OtherDocumentRecordTypeEnum recordType) {
        final OrderTypeEnum order = OrderTypeEnum.DESCENDING;
        if (allRecordTypes) {
            return this.findAllOrdered(order);
        } else {
            return this.findAllOrdered(recordType, order);
        }
    }

    /**
     * Updates the given {@link OtherDocumentRecord} in the repository. The old version of this document record should
     * be already stored there.
     */
    public void update(final OtherDocumentRecord record) {
        otherDocumentDao.update(record);
    }

    /**
     * Updates properties of the {@link OtherDocumentRecord} specified by <code>recordToUpdateId</code>, localizes the
     * <code>newType</code>, converts the <code>newAttachedFile</code> to {@link java.sql.Blob Blob} (if
     * <code>isFileReplaced == true</code>) and persists this record into the repository. The old version of this record
     * should be already stored there.
     *
     * @param recordToUpdateId
     *            ID of the persisted {@link OtherDocumentRecord}
     * @param newRecord
     *            {@link OtherDocumentRecord} which contains new values of properties of the persisted record. These
     *            values are copied to it.
     * @param newTypes
     *            New types of the persisted record
     * @param isFileReplaced
     *            Indicates whether the attached file of the persisted record has been replaced by other file.
     * @param newAttachedFile
     *            New attached file of the persisted record
     */
    public void update(final int recordToUpdateId, final OtherDocumentRecord newRecord, final boolean[] newTypes,
            final boolean isFileReplaced, final MultipartFile newAttachedFile, final HttpServletRequest request,
            final MessageSource messageSource) throws SQLException, IOException {
        final OtherDocumentRecord recordToUpdate = findByIdWithoutFile(recordToUpdateId);
        recordToUpdate.setName(newRecord.getName());
        recordToUpdate.setDescription(newRecord.getDescription());
        recordToUpdate.setTypes(OtherDocumentRecordUtils.convertIndicatorsToRelations(newTypes, recordToUpdateId));

        updateRecordWithSaveFileToDatabase(recordToUpdate, isFileReplaced, newAttachedFile);
    }

    /**
     * Updates {@link OtherDocumentRecord recordToUpdate's} properties related to the {@link MultipartFile
     * newAttachedFile} if <code>isFileReplaced == true</code>. Then, persists the record into the repository. The old
     * version of this record should be already stored there.
     * <p>
     * NOTE: Analogy to the {@link #updateRecordWithSaveFileToFileSystem(OtherDocumentRecord, boolean, MultipartFile)
     * updateRecordWithSaveFileToFileSystem} method.
     *
     * @param recordToUpdate
     *            New persisted {@link OtherDocumentRecord}
     * @param isFileReplaced
     *            Indicates whether the attached file of the persisted record has been replaced by other file.
     * @param newAttachedFile
     *            New attached file of the persisted record
     */
    private void updateRecordWithSaveFileToDatabase(final OtherDocumentRecord recordToUpdate,
            final boolean isFileReplaced, final MultipartFile newAttachedFile) throws SQLException, IOException {
        if (isFileReplaced) {
            final String newFileName = newAttachedFile.getOriginalFilename();
            recordToUpdate.setFileName(newFileName);
            recordToUpdate.setFile(File.convertToBlob(newAttachedFile.getBytes()));
        }

        this.update(recordToUpdate);
    }

    /**
     * Updates {@link OtherDocumentRecord recordToUpdate's} properties related to the {@link MultipartFile
     * newAttachedFile} if <code>isFileReplaced == true</code>. Then, persists the record into the repository. The old
     * version of this record should be already stored there.
     * <p>
     * If <code>isFileReplaced == true</code>, deletes the old file just before the persisting of the record and copies
     * the new file to the target folder just after the persisting.
     * <p>
     * NOTE: It is necessary to secure the uniqueness of its filename since all files of records are in one common
     * directory.
     * <p>
     * This method is not used since we were not able to find out where in the MochaHost directory hierarchy we were.
     * <p>
     * NOTE: Analogy to the {@link #updateRecordWithSaveFileToDatabase(OtherDocumentRecord, boolean, MultipartFile)
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
    private void updateRecordWithSaveFileToFileSystem(final OtherDocumentRecord recordToUpdate,
            final boolean isFileReplaced, final MultipartFile newAttachedFile) throws SQLException, IOException {
        if (isFileReplaced) {
            final String newFileName = File.getUniqueFileName(newAttachedFile.getOriginalFilename());
            recordToUpdate.setFileName(newFileName);
            newAttachedFile.transferTo(File.getUploadedFile(newFileName));
        }

        this.update(recordToUpdate);

        if (isFileReplaced) {
            final String oldFileName = recordToUpdate.getFileName();
            File.getUploadedFile(oldFileName).delete();
        }
    }

    /**
     * First, the method performs the
     * {@link #update(int, OtherDocumentRecord, boolean[], boolean, MultipartFile, HttpServletRequest, MessageSource)
     * update} method. Then, this method notifies all users which have corresponding rights by email.
     *
     * @param recordToUpdateId
     *            ID of the persisted {@link OtherDocumentRecord}
     * @param newRecord
     *            {@link OtherDocumentRecord} which contains new values of properties of the persisted record. These
     *            values are copied to it.
     * @param newTypes
     *            New types of the persisted record
     * @param isFileReplaced
     *            Indicates whether the attached file of the persisted record has been replaced by other file.
     * @param newAttachedFile
     *            New attached file of the persisted record
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void updateAndNotifyUsers(final int recordToUpdateId, final OtherDocumentRecord newRecord,
            final boolean[] newTypes, final boolean isFileReplaced, final MultipartFile newAttachedFile,
            final boolean sendNotification, final HttpServletRequest request, final MessageSource messageSource)
            throws SQLException, IOException {
        this.update(recordToUpdateId, newRecord, newTypes, isFileReplaced, newAttachedFile, request, messageSource);

        if (sendNotification) {
            notifyUsersOfUpdate(findByIdWithoutFile(recordToUpdateId), request, messageSource);
        }
    }

    @Override
    public List<User> gainUsersToNotify() {
        return getUserService().findAllWithEmailByAuthorityAndSubscription(AuthorityTypeEnum.ROLE_MEMBER_OF_SV,
                emailService.getNotificationType());
    }

    @Override
    public void notifyUsersOfUpdate(final Object updatedRecord, final HttpServletRequest request,
            final MessageSource messageSource) {
        emailService.sendEmailOnUpdate(updatedRecord, gainUsersToNotify(), request, messageSource);
    }

    /**
     * Stores the given {@link OtherDocumentRecord} to the repository. If there is already a document record with the
     * same {@link OtherDocumentRecord#getFileName() filename}, throws an exception.
     * <p>
     * Note that the method assigns the new ID to the document record as well.
     *
     * @return New ID of the given {@link OtherDocumentRecord} generated by the repository
     */
    public int save(final OtherDocumentRecord record) {
        final int newRecordId = otherDocumentDao.save(record);
        record.setId(newRecordId);
        return newRecordId;
    }

    /**
     * Since we need ID of the {@link OtherDocumentRecord newRecord} for the creation of
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation OtherDocumentRecordTypeRelations}, we
     * save the new record in two steps.
     * <p>
     * (1) The method converts the <code>attachedFile</code> to {@link java.sql.Blob Blob} and stores the record to the
     * repository. If there is already a record with the same {@link OtherDocumentRecord#getFileName() filename}, throws
     * an exception.
     * <p>
     * (2) Then, the method localizes the <code>recordTypes</code> of the new record, assigns them to the record and
     * persists these modifications.
     * <p>
     * Note that the method assigns the new ID to the document record as well.
     *
     * @param newRecord
     *            New document record
     * @param recordTypes
     *            Types of the new record
     * @param attachedFile
     *            File which is attached to the new record.
     */
    public void save(final OtherDocumentRecord newRecord, final boolean[] recordTypes, final MultipartFile attachedFile,
            final HttpServletRequest request, final MessageSource messageSource) throws SQLException, IOException {
        final String fileName = prepareForSaveFileToDatabase(newRecord, attachedFile);

        final int recordId = this.save(newRecord);
        LogFactory.getLog(this.getClass())
                .info(String.format("The file '%s' has been successfully uploaded", fileName));

        newRecord.setTypes(OtherDocumentRecordUtils.convertIndicatorsToRelations(recordTypes, recordId));
        this.update(newRecord);
    }

    /**
     * Prepares the {@link OtherDocumentRecord newRecord} for saving the {@link MultipartFile attachedFile} to the
     * database.
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
    private String prepareForSaveFileToDatabase(final OtherDocumentRecord newRecord, final MultipartFile attachedFile)
            throws SQLException, IOException {
        final String fileName = attachedFile.getOriginalFilename();
        newRecord.setFileName(fileName);
        newRecord.setFile(File.convertToBlob(attachedFile.getBytes()));
        return fileName;
    }

    /**
     * Prepares the {@link OtherDocumentRecord newRecord} for saving and saves the {@link MultipartFile attachedFile} to
     * the file system.
     * <p>
     * NOTE: It is necessary to secure the uniqueness of its filename since all files of records are in one common
     * directory.
     * <p>
     * This method is not used since we were not able to find out where in the MochaHost directory hierarchy we were.
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
    private String preparedAndSaveFileToFileSystem(final OtherDocumentRecord newRecord,
            final MultipartFile attachedFile) throws SQLException, IOException {
        final String fileName = File.getUniqueFileName(attachedFile.getOriginalFilename());
        newRecord.setFileName(fileName);
        attachedFile.transferTo(File.getUploadedFile(fileName));
        return fileName;
    }

    /**
     * First, the method performs the
     * {@link #save(OtherDocumentRecord, boolean[], MultipartFile, HttpServletRequest, MessageSource) save} method.
     * Then, this method notifies all users which have corresponding rights by email.
     *
     * @param newRecord
     *            New document record
     * @param recordTypes
     *            Types of the new record
     * @param attachedFile
     *            File which is attached to the new record.
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void saveAndNotifyUsers(final OtherDocumentRecord newRecord, final boolean[] recordTypes,
            final MultipartFile attachedFile, final boolean sendNotification, final HttpServletRequest request,
            final MessageSource messageSource) throws SQLException, IOException {
        this.save(newRecord, recordTypes, attachedFile, request, messageSource);

        if (sendNotification) {
            notifyUsersOfCreation(newRecord, request, messageSource);
        }
    }

    @Override
    public void notifyUsersOfCreation(final Object newRecord, final HttpServletRequest request,
            final MessageSource messageSource) {
        emailService.sendEmailOnCreation(newRecord, gainUsersToNotify(), request, messageSource);
    }

    /**
     * Deletes the specified {@link OtherDocumentRecord} together with all its {@link OtherDocumentRecordTypeEnum types}
     * from the repository. Moreover, deletes the associated {@link java.io.File file}.
     *
     * @param recordId
     *            ID of the document record
     */
    public void delete(final int recordId) {
        final OtherDocumentRecord record = findByIdWithoutFile(recordId);
        this.delete(record);
    }

    /**
     * Gets a {@link Map} which for each input {@link OtherDocumentRecord} contains a corresponding localized delete
     * question which is asked before deletion of that record.
     */
    public static Map<OtherDocumentRecord, String> getLocalizedDeleteQuestions(final List<OtherDocumentRecord> records,
            final HttpServletRequest request, final MessageSource messageSource) {
        final String messageCode = "other-documents.do-you-really-want-to-delete-document";
        final Map<OtherDocumentRecord, String> questions = new HashMap<>();

        for (final OtherDocumentRecord record : records) {
            final Object[] messageParams = new Object[] { record.getName() };
            questions.put(record, Localization.findLocaleMessage(messageSource, request, messageCode, messageParams));
        }
        return questions;
    }

    /**
     * Gets a {@link Map} which for each constant of the {@link OtherDocumentRecordTypeEnum} enumeration contains a pair
     * of its {@link OtherDocumentRecordTypeEnum#getOrdinal() ordinal} and ID of its checkbox.
     */
    public static Map<Long, String> getTypeCheckboxId() {
        final String commonIdFormat = "newTypes[%s]";
        final Map<Long, String> checkboxIds = new HashMap<>();

        for (final OtherDocumentRecordTypeEnum type : OtherDocumentRecordTypeEnum.values()) {
            final long typeOrdinal = type.getOrdinal();
            checkboxIds.put(typeOrdinal, String.format(commonIdFormat, typeOrdinal));
        }
        return checkboxIds;
    }

    /**
     * Gets a {@link Map} which for each constant of the {@link OtherDocumentRecordTypeEnum} enumeration contains a pair
     * of its {@link OtherDocumentRecordTypeEnum#getOrdinal() ordinal} and its localized title.
     */
    public static Map<Long, String> getLocalizedTypeTitles(final HttpServletRequest request,
            final MessageSource messageSource) {
        final Map<Long, String> checkboxTitles = new HashMap<>();

        for (final OtherDocumentRecordTypeEnum type : OtherDocumentRecordTypeEnum.values()) {
            final String localizedTitle = Localization.findLocaleMessage(messageSource, request,
                    type.getTitleLocalizationCode());
            checkboxTitles.put(type.getOrdinal(), localizedTitle);
        }
        return checkboxTitles;
    }

}
