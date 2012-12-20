package com.svnavigatoru600.repository.records;

import java.util.List;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.service.util.OrderType;

public interface SessionRecordDao extends DocumentRecordDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.svnavigatoru600.repository.DocumentRecordDao#findById(int)
     */
    @Override
    SessionRecord findById(int recordId);

    /*
     * (non-Javadoc)
     * 
     * @see com.svnavigatoru600.repository.records.DocumentRecordDao#findById(int, boolean)
     */
    @Override
    SessionRecord findById(int recordId, boolean loadFile);

    /*
     * (non-Javadoc)
     * 
     * @see com.svnavigatoru600.repository.DocumentRecordDao#findByFileName(java.lang.String )
     */
    @Override
    SessionRecord findByFileName(String fileName);

    /**
     * Returns all {@link SessionRecord}s stored in the repository arranged according to their
     * <code>sessionDate</code>s in the given <code>order</code>.
     */
    List<SessionRecord> findOrdered(OrderType order);

    /**
     * Returns all {@link SessionRecord}s stored in the repository which are of the given <code>type</code>.
     * The {@link SessionRecord} are arranged according to their <code>sessionDate</code>s in the given
     * <code>order</code>.
     */
    List<SessionRecord> findOrdered(SessionRecordType type, OrderType order);

    /**
     * Updates the given <code>record</code> in the repository. The old version of the <code>record</code>
     * should be already stored there.
     */
    void update(SessionRecord record);

    /**
     * Stores the given <code>record</code> to the repository. If there is already a {@link SessionRecord}
     * with the same filename, throws an exception.
     * 
     * @return the generated identifier
     */
    int save(SessionRecord record);
}
