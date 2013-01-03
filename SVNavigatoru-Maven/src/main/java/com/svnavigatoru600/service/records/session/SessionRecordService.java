package com.svnavigatoru600.service.records.session;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.service.records.AbstractDocumentRecordService;
import com.svnavigatoru600.service.util.OrderType;

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
     * Constructor.
     */
    @Inject
    public SessionRecordService(SessionRecordDao documentDao) {
        this.sessionRecordDao = documentDao;
    }

    @Override
    public SessionRecord findById(int recordId) {
        return this.sessionRecordDao.findById(recordId);
    }

    @Override
    public SessionRecord findById(int recordId, boolean loadFile) {
        return this.sessionRecordDao.findById(recordId, loadFile);
    }

    @Override
    public SessionRecord findByFileName(String fileName) {
        return this.sessionRecordDao.findByFileName(fileName);
    }

    /**
     * Returns all {@link SessionRecord SessionRecords} stored in the repository arranged according to their
     * {@link SessionRecord#getSessionDate() sessionDates} in the given {@link OrderType order}.
     */
    public List<SessionRecord> findAllOrdered(OrderType order) {
        return this.sessionRecordDao.findAllOrdered(order);
    }

    /**
     * Returns all {@link SessionRecord SessionRecords} stored in the repository which are of the given
     * {@link SessionRecordType type}. The records are arranged according to their
     * {@link SessionRecord#getSessionDate() sessionDates} in the given {@link OrderType order}.
     */
    public List<SessionRecord> findAllOrdered(SessionRecordType type, OrderType order) {
        return this.sessionRecordDao.findAllOrdered(type, order);
    }

    /**
     * Updates the given {@link SessionRecord} in the repository. The old version of this record should be
     * already stored there.
     */
    public void update(SessionRecord record) {
        this.sessionRecordDao.update(record);
    }

    /**
     * Stores the given {@link SessionRecord} to the repository. If there is already a record with the same
     * {@link SessionRecord#getFileName() filename}, throws an exception.
     * 
     * @return The new ID of the given {@link SessionRecord} generated by the repository
     */
    public int save(SessionRecord record) {
        return this.sessionRecordDao.save(record);
    }
}