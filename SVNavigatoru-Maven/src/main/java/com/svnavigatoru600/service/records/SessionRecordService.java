package com.svnavigatoru600.service.records;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordTypeEnum;
import com.svnavigatoru600.domain.users.AuthorityTypeEnum;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.File;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderTypeEnum;

/**
 * Provides convenient methods to work with {@link SessionRecord} objects.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class SessionRecordService extends AbstractDocumentRecordService {

    /**
     * The object which provides a persistence.
     */
    private final SessionRecordDao sessionRecordDao;
    /**
     * Assembles notification emails and sends them to authorized {@link User users}.
     */
    private SessionRecordNotificationEmailService emailService;

    @Inject
    public SessionRecordService(final SessionRecordDao sessionRecordDao) {
        super(sessionRecordDao);
        this.sessionRecordDao = sessionRecordDao;
    }

    @Inject
    public void setEmailService(final SessionRecordNotificationEmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public SessionRecord findById(final int recordId) {
        return sessionRecordDao.findById(recordId);
    }

    @Override
    public SessionRecord findById(final int recordId, final boolean loadFile) {
        return sessionRecordDao.findById(recordId, loadFile);
    }

    @Override
    public SessionRecord findByIdWithoutFile(final int recordId) {
        return this.findById(recordId, false);
    }

    /**
     * Returns all {@link SessionRecord SessionRecords} stored in the repository arranged according to their
     * {@link SessionRecord#getSessionDate() sessionDates} in the given {@link OrderTypeEnum order}.
     */
    public List<SessionRecord> findAllOrdered(final OrderTypeEnum order) {
        return sessionRecordDao.findAllOrdered(order);
    }

    /**
     * Returns all {@link SessionRecord SessionRecords} stored in the repository which are of the given
     * {@link SessionRecordTypeEnum type}. The records are arranged according to their {@link SessionRecord#getSessionDate()
     * sessionDates} in the given {@link OrderTypeEnum order}.
     */
    public List<SessionRecord> findAllOrdered(final SessionRecordTypeEnum type, final OrderTypeEnum order) {
        return sessionRecordDao.findAllOrdered(type, order);
    }

    /**
     * Returns {@link SessionRecord SessionRecords} stored in the repository arranged according to their
     * {@link SessionRecord#getSessionDate() sessionDates} descending.
     *
     * @param allRecordTypes
     *            If <code>true</code>, all session records are returned. Otherwise, only records which are of the given
     *            {@link SessionRecordTypeEnum recordType} are returned.
     * @param recordType
     *            see <code>allRecordTypes</code>
     */
    public List<SessionRecord> findAllOrdered(final boolean allRecordTypes, final SessionRecordTypeEnum recordType) {
        final OrderTypeEnum order = OrderTypeEnum.DESCENDING;
        if (allRecordTypes) {
            return this.findAllOrdered(order);
        } else {
            return this.findAllOrdered(recordType, order);
        }
    }

    /**
     * Updates the given {@link SessionRecord} in the repository. The old version of this record should be already
     * stored there.
     */
    public void update(final SessionRecord record) {
        sessionRecordDao.update(record);
    }

    /**
     * Updates properties of the {@link SessionRecord} specified by <code>recordToUpdateId</code>, localizes the
     * <code>newType</code>, converts the <code>newAttachedFile</code> to {@link java.sql.Blob Blob} (if
     * <code>isFileReplaced == true</code>) and persists this record into the repository. The old version of this record
     * should be already stored there.
     *
     * @param recordToUpdateId
     *            ID of the persisted {@link SessionRecord}
     * @param newRecord
     *            {@link SessionRecord} which contains new values of properties of the persisted record. These values
     *            are copied to it.
     * @param newType
     *            New type of the persisted record
     * @param isFileReplaced
     *            Indicates whether the attached file of the persisted record has been replaced by other file.
     * @param newAttachedFile
     *            New attached file of the persisted record
     */
    public void update(final int recordToUpdateId, final SessionRecord newRecord, final String newType, final boolean isFileReplaced,
            final MultipartFile newAttachedFile, final HttpServletRequest request, final MessageSource messageSource)
            throws SQLException, IOException {
        final SessionRecord recordToUpdate = findByIdWithoutFile(recordToUpdateId);
        recordToUpdate.setSessionDate(newRecord.getSessionDate());
        recordToUpdate.setDiscussedTopics(newRecord.getDiscussedTopics());
        recordToUpdate.setType(SessionRecordTypeEnum.valueOfAccordingLocalization(newType, messageSource, request));

        updateRecordWithSaveFileToDatabase(recordToUpdate, isFileReplaced, newAttachedFile);
    }

    /**
     * Updates {@link SessionRecord recordToUpdate's} properties related to the {@link MultipartFile newAttachedFile} if
     * <code>isFileReplaced == true</code>. Then, persists the record into the repository. The old version of this
     * record should be already stored there.
     * <p>
     * NOTE: Analogy to the {@link #updateRecordWithSaveFileToFileSystem(SessionRecord, boolean, MultipartFile)
     * updateRecordWithSaveFileToFileSystem} method.
     *
     * @param recordToUpdate
     *            New persisted {@link SessionRecord}
     * @param isFileReplaced
     *            Indicates whether the attached file of the persisted record has been replaced by other file.
     * @param newAttachedFile
     *            New attached file of the persisted record
     */
    private void updateRecordWithSaveFileToDatabase(final SessionRecord recordToUpdate, final boolean isFileReplaced,
            final MultipartFile newAttachedFile) throws SQLException, IOException {
        if (isFileReplaced) {
            final String newFileName = newAttachedFile.getOriginalFilename();
            recordToUpdate.setFileName(newFileName);
            recordToUpdate.setFile(File.convertToBlob(newAttachedFile.getBytes()));
        }

        this.update(recordToUpdate);
    }

    /**
     * Updates {@link SessionRecord recordToUpdate's} properties related to the {@link MultipartFile newAttachedFile} if
     * <code>isFileReplaced == true</code>. Then, persists the record into the repository. The old version of this
     * record should be already stored there.
     * <p>
     * If <code>isFileReplaced == true</code>, deletes the old file just before the persisting of the record and copies
     * the new file to the target folder just after the persisting.
     * <p>
     * NOTE: It is necessary to secure the uniqueness of its filename since all files of records are in one common
     * directory.
     * <p>
     * This method is not used since we were not able to find out where in the MochaHost directory hierarchy we were.
     * <p>
     * NOTE: Analogy to the {@link #updateRecordWithSaveFileToDatabase(SessionRecord, boolean, MultipartFile)
     * updateRecordWithSaveFileToDatabase} method.
     *
     * @param recordToUpdate
     *            New persisted {@link SessionRecord}
     * @param isFileReplaced
     *            Indicates whether the attached file of the persisted record has been replaced by other file.
     * @param newAttachedFile
     *            New attached file of the persisted record
     */
    @SuppressWarnings("unused")
    private void updateRecordWithSaveFileToFileSystem(final SessionRecord recordToUpdate, final boolean isFileReplaced,
            final MultipartFile newAttachedFile) throws SQLException, IOException {
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
     * {@link #update(int, SessionRecord, String, boolean, MultipartFile, HttpServletRequest, MessageSource) update}
     * method. Then, this method notifies all users which have corresponding rights by email.
     *
     * @param recordToUpdateId
     *            ID of the persisted {@link SessionRecord}
     * @param newRecord
     *            {@link SessionRecord} which contains new values of properties of the persisted record. These values
     *            are copied to it.
     * @param newType
     *            New type of the persisted record
     * @param isFileReplaced
     *            Indicates whether the attached file of the persisted record has been replaced by other file.
     * @param newAttachedFile
     *            New attached file of the persisted record
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void updateAndNotifyUsers(final int recordToUpdateId, final SessionRecord newRecord, final String newType,
            final boolean isFileReplaced, final MultipartFile newAttachedFile, final boolean sendNotification, final HttpServletRequest request,
            final MessageSource messageSource) throws SQLException, IOException {
        this.update(recordToUpdateId, newRecord, newType, isFileReplaced, newAttachedFile, request, messageSource);

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
    public void notifyUsersOfUpdate(final Object updatedRecord, final HttpServletRequest request, final MessageSource messageSource) {
        emailService.sendEmailOnUpdate(updatedRecord, gainUsersToNotify(), request, messageSource);
    }

    /**
     * Stores the given {@link SessionRecord} to the repository. If there is already a record with the same
     * {@link SessionRecord#getFileName() filename}, throws an exception.
     * <p>
     * Note that the method assigns the new ID to the session record as well.
     *
     * @return The new ID of the given {@link SessionRecord} generated by the repository
     */
    public int save(final SessionRecord record) {
        final int newRecordId = sessionRecordDao.save(record);
        record.setId(newRecordId);
        return newRecordId;
    }

    /**
     * Localizes the <code>recordType</code> of the given {@link SessionRecord newRecord}, converts the
     * <code>attachedFile</code> to {@link java.sql.Blob Blob} and stores the record to the repository. If there is
     * already a record with the same {@link SessionRecord#getFileName() filename}, throws an exception.
     * <p>
     * Note that the method assigns the new ID to the session record as well.
     *
     * @param newRecord
     *            New session record
     * @param recordType
     *            Type of the new record
     * @param attachedFile
     *            File which is attached to the new record.
     */
    public void save(final SessionRecord newRecord, final String recordType, final MultipartFile attachedFile, final HttpServletRequest request,
            final MessageSource messageSource) throws SQLException, IOException {
        newRecord.setType(SessionRecordTypeEnum.valueOfAccordingLocalization(recordType, messageSource, request));

        prepareForSaveFileToDatabase(newRecord, attachedFile);
        this.save(newRecord);
    }

    /**
     * Prepares the {@link SessionRecord newRecord} for saving the {@link MultipartFile attachedFile} to the database.
     * <p>
     * NOTE: Analogy to the {@link #preparedAndSaveFileToFileSystem(SessionRecord, MultipartFile)
     * preparedAndSaveFileToFileSystem} method.
     *
     * @param newRecord
     *            New session record
     * @param attachedFile
     *            File which is attached to the new record.
     */
    private void prepareForSaveFileToDatabase(final SessionRecord newRecord, final MultipartFile attachedFile)
            throws SQLException, IOException {
        final String fileName = attachedFile.getOriginalFilename();
        newRecord.setFileName(fileName);
        newRecord.setFile(File.convertToBlob(attachedFile.getBytes()));
    }

    /**
     * Prepares the {@link SessionRecord newRecord} for saving and saves the {@link MultipartFile attachedFile} to the
     * file system.
     * <p>
     * NOTE: It is necessary to secure the uniqueness of its filename since all files of records are in one common
     * directory.
     * <p>
     * This method is not used since we were not able to find out where in the MochaHost directory hierarchy we were.
     * <p>
     * NOTE: Analogy to the {@link #prepareForSaveFileToDatabase(SessionRecord, MultipartFile)
     * prepareForSaveFileToDatabase} method.
     *
     * @param newRecord
     *            New session record
     * @param attachedFile
     *            File which is attached to the new record.
     */
    @SuppressWarnings("unused")
    private void preparedAndSaveFileToFileSystem(final SessionRecord newRecord, final MultipartFile attachedFile)
            throws SQLException, IOException {
        final String fileName = File.getUniqueFileName(attachedFile.getOriginalFilename());
        newRecord.setFileName(fileName);
        attachedFile.transferTo(File.getUploadedFile(fileName));
    }

    /**
     * First, the method performs the
     * {@link #save(SessionRecord, String, MultipartFile, HttpServletRequest, MessageSource) save} method. Then, this
     * method notifies all users which have corresponding rights by email.
     *
     * @param newRecord
     *            New session record
     * @param recordType
     *            Type of the new record
     * @param attachedFile
     *            File which is attached to the new record.
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void saveAndNotifyUsers(final SessionRecord newRecord, final String recordType, final MultipartFile attachedFile,
            final boolean sendNotification, final HttpServletRequest request, final MessageSource messageSource)
            throws SQLException, IOException {
        this.save(newRecord, recordType, attachedFile, request, messageSource);

        if (sendNotification) {
            notifyUsersOfCreation(newRecord, request, messageSource);
        }
    }

    @Override
    public void notifyUsersOfCreation(final Object newRecord, final HttpServletRequest request, final MessageSource messageSource) {
        emailService.sendEmailOnCreation(newRecord, gainUsersToNotify(), request, messageSource);
    }

    /**
     * Deletes the specified {@link SessionRecord} together with all its {@link SessionRecordTypeEnum types} from the
     * repository. Moreover, deletes the associated {@link java.io.File file}.
     *
     * @param recordId
     *            ID of the session record
     */
    public void delete(final int recordId) {
        final SessionRecord record = findByIdWithoutFile(recordId);
        this.delete(record);
    }

    /**
     * Gets a {@link Map} which for each input {@link SessionRecord} contains a localized title of its
     * {@link SessionRecordTypeEnum type}.
     */
    public static Map<SessionRecord, String> getLocalizedTypeTitles(final List<SessionRecord> records,
            final HttpServletRequest request, final MessageSource messageSource) {
        final Map<SessionRecord, String> recordLocalizedTypeMap = new HashMap<>();

        for (final SessionRecord record : records) {
            final String localizationCode = record.getTypedType().getLocalizationCode();
            recordLocalizedTypeMap.put(record,
                    Localization.findLocaleMessage(messageSource, request, localizationCode));
        }
        return recordLocalizedTypeMap;
    }

    /**
     * Gets a {@link Map} which for each input {@link SessionRecord} contains its localized
     * {@link SessionRecord#getSessionDate() session date}.
     */
    public static Map<SessionRecord, String> getLocalizedSessionDates(final List<SessionRecord> records,
            final HttpServletRequest request) {
        final Locale locale = Localization.getLocale(request);
        final Map<SessionRecord, String> sessionDates = new HashMap<>();

        for (final SessionRecord record : records) {
            final String date = DateUtils.format(record.getSessionDate(), DateUtils.LONG_DATE_FORMATS.get(locale), locale);
            sessionDates.put(record, date);
        }
        return sessionDates;
    }

    /**
     * Gets a {@link Map} which for each input {@link SessionRecord} contains a corresponding localized delete question
     * which is asked before deletion of that record.
     */
    public static Map<SessionRecord, String> getLocalizedDeleteQuestions(final List<SessionRecord> records,
            final HttpServletRequest request, final Map<SessionRecord, String> localizedSessionDates, final MessageSource messageSource) {
        final String messageCode = "session-records.do-you-really-want-to-delete-record";
        final Map<SessionRecord, String> questions = new HashMap<>();

        for (final SessionRecord record : records) {
            final Object[] messageParams = new Object[] { localizedSessionDates.get(record) };
            questions.put(record, Localization.findLocaleMessage(messageSource, request, messageCode, messageParams));
        }
        return questions;
    }

    /**
     * Gets the {@link List} of all localized {@link SessionRecordTypeEnum SessionRecordTypes}.
     */
    public static List<String> getLocalizedTypes(final HttpServletRequest request, final MessageSource messageSource) {
        final List<String> localizedTypes = Lists.newArrayList();

        for (final SessionRecordTypeEnum type : SessionRecordTypeEnum.values()) {
            localizedTypes.add(Localization.findLocaleMessage(messageSource, request, type.getLocalizationCode()));
        }
        return localizedTypes;
    }
}
