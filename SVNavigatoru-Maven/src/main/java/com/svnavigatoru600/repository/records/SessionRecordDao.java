package com.svnavigatoru600.repository.records;

import java.util.List;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.MapperInterface;
import com.svnavigatoru600.service.util.OrderType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@MapperInterface
public interface SessionRecordDao extends DocumentRecordDao {

    @Override
    SessionRecord findById(int recordId);

    @Override
    SessionRecord findById(int recordId, boolean loadFile);

    @Override
    SessionRecord findByFileName(String fileName);

    /**
     * Returns all {@link SessionRecord SessionRecords} stored in the repository arranged according to their
     * {@link SessionRecord#getSessionDate() sessionDates} in the given {@link OrderType order}.
     */
    List<SessionRecord> findAllOrdered(OrderType order);

    /**
     * Returns all {@link SessionRecord SessionRecords} stored in the repository which are of the given
     * {@link SessionRecordType type}. The records are arranged according to their
     * {@link SessionRecord#getSessionDate() sessionDates} in the given {@link OrderType order}.
     */
    List<SessionRecord> findAllOrdered(SessionRecordType type, OrderType order);

    /**
     * Updates the given {@link SessionRecord} in the repository. The old version of this record should be
     * already stored there.
     */
    void update(SessionRecord record);

    /**
     * Stores the given {@link SessionRecord} to the repository. If there is already a {@link SessionRecord}
     * with the same {@link SessionRecord#getFileName() filename}, throws an exception.
     * 
     * @return the generated identifier
     */
    int save(SessionRecord record);
}
