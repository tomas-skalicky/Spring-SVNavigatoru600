package com.svnavigatoru600.repository.records;

import java.util.List;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordTypeEnum;
import com.svnavigatoru600.service.util.OrderTypeEnum;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public interface SessionRecordDao extends DocumentRecordDao {

    @Override
    SessionRecord findById(int recordId);

    @Override
    SessionRecord findById(int recordId, boolean loadFile);

    /**
     * Returns all {@link SessionRecord SessionRecords} stored in the repository arranged according to their
     * {@link SessionRecord#getSessionDate() sessionDates} in the given {@link OrderTypeEnum order}.
     * <p>
     * Records' associated {@link SessionRecord#getFile() files} are NOT loaded.
     */
    List<SessionRecord> findAllOrdered(OrderTypeEnum order);

    /**
     * Returns all {@link SessionRecord SessionRecords} stored in the repository which are of the given
     * {@link SessionRecordTypeEnum type}. The records are arranged according to their {@link SessionRecord#getSessionDate()
     * sessionDates} in the given {@link OrderTypeEnum order}.
     * <p>
     * Records' associated {@link SessionRecord#getFile() files} are NOT loaded.
     */
    List<SessionRecord> findAllOrdered(SessionRecordTypeEnum type, OrderTypeEnum order);

    /**
     * Updates the given {@link SessionRecord} in the repository. The old version of this record should be already
     * stored there.
     */
    void update(SessionRecord record);

    /**
     * Stores the given {@link SessionRecord} to the repository. If there is already a record with the same
     * {@link SessionRecord#getFileName() filename}, throws an exception.
     * 
     * @return The new ID of the given {@link SessionRecord} generated by the repository
     */
    int save(SessionRecord record);
}
