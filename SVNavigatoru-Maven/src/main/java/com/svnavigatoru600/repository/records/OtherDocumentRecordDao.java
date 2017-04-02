package com.svnavigatoru600.repository.records;

import java.util.List;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.service.util.OrderTypeEnum;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public interface OtherDocumentRecordDao extends DocumentRecordDao {

    @Override
    OtherDocumentRecord findById(int recordId);

    @Override
    OtherDocumentRecord findById(int recordId, boolean loadFile);

    /**
     * Returns all {@link OtherDocumentRecord OtherDocumentRecords} stored in the repository arranged according to their
     * {@link OtherDocumentRecord#getCreationTime() creationTimes} in the given {@link OrderTypeEnum order}.
     */
    List<OtherDocumentRecord> findAllOrdered(OrderTypeEnum order);

    /**
     * Returns all {@link OtherDocumentRecord OtherDocumentRecords} stored in the repository which are of the given
     * {@link OtherDocumentRecordTypeEnum type}. The {@link OtherDocumentRecord} are arranged according to their
     * {@link OtherDocumentRecord#getCreationTime() creationTimes} in the given {@link OrderTypeEnum order}.
     */
    List<OtherDocumentRecord> findAllOrdered(OtherDocumentRecordTypeEnum type, OrderTypeEnum order);

    /**
     * Updates the given {@link OtherDocumentRecord} in the repository. The old version of this document record should
     * be already stored there.
     */
    void update(OtherDocumentRecord record);

    /**
     * Stores the given {@link OtherDocumentRecord} to the repository. If there is already a document record with the
     * same {@link OtherDocumentRecord#getFileName() filename}, throws an exception.
     * 
     * @return The new ID of the given {@link OtherDocumentRecord} generated by the repository
     */
    int save(OtherDocumentRecord record);
}
