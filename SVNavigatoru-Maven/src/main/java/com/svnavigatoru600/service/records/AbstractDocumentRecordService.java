package com.svnavigatoru600.service.records;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.repository.records.DocumentRecordDao;
import com.svnavigatoru600.service.SubjectOfNotificationService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.util.File;
import com.svnavigatoru600.service.util.HttpResponseUtils;

/**
 * Provides convenient methods to work with {@link AbstractDocumentRecord} objects.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractDocumentRecordService implements SubjectOfNotificationService {

    public static final String EXTENSION_DELIMITER = ".";

    /**
     * The object which provides a persistence.
     */
    private final DocumentRecordDao recordDao;
    /**
     * Does the work which concerns mainly notification of {@link com.svnavigatoru600.domain.users.User users}
     * .
     */
    private UserService userService;

    public AbstractDocumentRecordService(DocumentRecordDao recordDao) {
        this.recordDao = recordDao;
    }

    /**
     * Trivial getter
     * 
     * @return the userService
     */
    protected UserService getUserService() {
        return this.userService;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which has the given ID.
     * <p>
     * The {@link Blob} file is loaded as well.
     */
    public abstract AbstractDocumentRecord findById(int recordId);

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which has the given ID.
     * 
     * @param loadFile
     *            If <code>true</code>, the {@link Blob} file will be loaded as well; otherwise not.
     */
    public abstract AbstractDocumentRecord findById(int recordId, boolean loadFile);

    /**
     * Returns a {@link AbstractDocumentRecord} stored in the repository which has the given ID.
     * <p>
     * The {@link Blob} file is not loaded.
     */
    public abstract AbstractDocumentRecord findByIdWithoutFile(int recordId);

    /**
     * Deletes the given {@link AbstractDocumentRecord} together with all its types from the repository.
     * Moreover, deletes the associated {@link java.io.File file}.
     */
    public void delete(AbstractDocumentRecord record) {
        this.recordDao.delete(record);

        // /////////////////////////////////////////////////////////////////
        // Store in the FILESYSTEM: The following line should be commented out:
        // --------------------------------------------------------------
        File.getUploadedFile(record.getFileName()).delete();
        // \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    }

    public void retrieve(int recordId, HttpServletResponse response) throws SQLException, IOException {
        // Gets the record from the DB.
        AbstractDocumentRecord record = this.findById(recordId);
        Blob file = record.getFile();
        byte[] bytes = file.getBytes(1L, (int) file.length());

        // Sets the type of the response.
        String fileName = record.getFileName();
        String fileExtension = fileName.substring(
                fileName.lastIndexOf(AbstractDocumentRecordService.EXTENSION_DELIMITER) + 1,
                fileName.length()).trim();
        HttpResponseUtils.sendFile(response, bytes, fileName, fileExtension);
    }
}
