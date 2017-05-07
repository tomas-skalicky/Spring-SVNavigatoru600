package com.svnavigatoru600.repository.records.impl.direct;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordTypeEnum;
import com.svnavigatoru600.repository.AbstractDaoImpl;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.repository.records.impl.DocumentRecordFieldEnum;
import com.svnavigatoru600.repository.records.impl.SessionRecordFieldEnum;
import com.svnavigatoru600.service.util.OrderTypeEnum;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("sessionRecordDao")
@Transactional
public class SessionRecordDaoImpl extends AbstractDaoImpl implements SessionRecordDao {

    /**
     * Database table which provides a persistence of {@link SessionRecord SessionRecords}.
     */
    private static final String TABLE_NAME = "session_records";

    /**
     * The SELECT command for the return of a single document together with its BLOB file.
     *
     * Join is necessary in all SELECT queries since {@link SessionRecord} inherits from {@link AbstractDocumentRecord}.
     */
    private static final String SELECT_FROM_CLAUSE_WITH_FILE = String.format(
            "SELECT r.*, d.%s, d.%s FROM %s r INNER JOIN %s d ON d.%s = r.%s",
            DocumentRecordFieldEnum.FILE_NAME.getColumnName(), DocumentRecordFieldEnum.FILE.getColumnName(),
            SessionRecordDaoImpl.TABLE_NAME, DocumentRecordDaoImpl.TABLE_NAME, DocumentRecordFieldEnum.ID.getColumnName(),
            SessionRecordFieldEnum.ID.getColumnName());
    /**
     * The SELECT command for the return of a single or multiple documents without their BLOB files.
     *
     * Join is necessary in all SELECT queries since {@link com.svnavigatoru600.domain.records.OtherDocumentRecord
     * OtherDocumentRecord} inherits from {@link AbstractDocumentRecord}.
     */
    private static final String SELECT_FROM_CLAUSE_WITHOUT_FILE = String.format(
            "SELECT r.*, d.%s FROM %s r INNER JOIN %s d ON d.%s = r.%s", DocumentRecordFieldEnum.FILE_NAME.getColumnName(),
            SessionRecordDaoImpl.TABLE_NAME, DocumentRecordDaoImpl.TABLE_NAME, DocumentRecordFieldEnum.ID.getColumnName(),
            SessionRecordFieldEnum.ID.getColumnName());

    private final DocumentRecordDaoImpl documentRecordDao;

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public SessionRecordDaoImpl(final DocumentRecordDaoImpl documentRecordDao) {
        this.documentRecordDao = documentRecordDao;
    }

    @Override
    public SessionRecord findById(final int recordId) {
        return this.findById(recordId, true);
    }

    @Override
    public SessionRecord findById(final int recordId, final boolean loadFile) {
        String selectClause;
        if (loadFile) {
            selectClause = SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITH_FILE;
        } else {
            selectClause = SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE;
        }

        final String idColumn = SessionRecordFieldEnum.ID.getColumnName();
        final String query = String.format("%s WHERE r.%s = :%s", selectClause, idColumn, idColumn);

        final Map<String, Integer> args = Collections.singletonMap(idColumn, recordId);

        return getNamedParameterJdbcTemplate().queryForObject(query, args, new SessionRecordRowMapper(loadFile));
    }

    @Override
    public List<SessionRecord> findAllOrdered(final OrderTypeEnum order) {
        final String query = String.format("%s ORDER BY r.%s %s", SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE,
                SessionRecordFieldEnum.SESSION_DATE.getColumnName(), order.getDatabaseCode());

        return getJdbcTemplate().query(query, new SessionRecordRowMapper(false));
    }

    @Override
    public List<SessionRecord> findAllOrdered(final SessionRecordTypeEnum type, final OrderTypeEnum order) {
        final String typeColumn = SessionRecordFieldEnum.TYPE.getColumnName();
        final String query = String.format("%s WHERE r.%s = :%s ORDER BY r.%s %s",
                SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE, typeColumn, typeColumn,
                SessionRecordFieldEnum.SESSION_DATE.getColumnName(), order.getDatabaseCode());

        final Map<String, String> args = Collections.singletonMap(typeColumn, type.name());

        return getNamedParameterJdbcTemplate().query(query, args, new SessionRecordRowMapper(false));
    }

    /**
     * Maps properties of the given {@link SessionRecord} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(final SessionRecord record) {
        final Map<String, Object> parameters = Maps.newHashMap();
        parameters.put(SessionRecordFieldEnum.ID.getColumnName(), record.getId());
        parameters.put(SessionRecordFieldEnum.TYPE.getColumnName(), record.getType());
        parameters.put(SessionRecordFieldEnum.SESSION_DATE.getColumnName(), record.getSessionDate());
        parameters.put(SessionRecordFieldEnum.DISCUSSED_TOPICS.getColumnName(), record.getDiscussedTopics());
        return parameters;
    }

    @Override
    public void update(final SessionRecord record) {
        // NOTE: updates the record in both tables 'document_records' and
        // 'session_records'.
        documentRecordDao.update(record, getDataSource());

        final String idColumn = SessionRecordFieldEnum.ID.getColumnName();
        final String typeColumn = SessionRecordFieldEnum.TYPE.getColumnName();
        final String sessionDateColumn = SessionRecordFieldEnum.SESSION_DATE.getColumnName();
        final String discussedTopicsColumn = SessionRecordFieldEnum.DISCUSSED_TOPICS.getColumnName();
        final String query = String.format("UPDATE %s SET %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                SessionRecordDaoImpl.TABLE_NAME, typeColumn, typeColumn, sessionDateColumn, sessionDateColumn,
                discussedTopicsColumn, discussedTopicsColumn, idColumn, idColumn);

        doUpdate(query, getNamedParameters(record));
    }

    @Override
    public int save(final SessionRecord record) {
        // NOTE: inserts the new record into two tables: 'document_records' and
        // 'session_records'.
        final int recordId = documentRecordDao.save(record, getDataSource());

        final SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource())
                .withTableName(SessionRecordDaoImpl.TABLE_NAME).usingColumns(SessionRecordFieldEnum.ID.getColumnName(),
                        SessionRecordFieldEnum.TYPE.getColumnName(), SessionRecordFieldEnum.SESSION_DATE.getColumnName(),
                        SessionRecordFieldEnum.DISCUSSED_TOPICS.getColumnName());

        final Map<String, Object> parameters = getNamedParameters(record);
        parameters.put(SessionRecordFieldEnum.ID.getColumnName(), recordId);
        insert.execute(parameters);

        return recordId;
    }

    @Override
    public void delete(final AbstractDocumentRecord record) {
        // The 'ON DELETE CASCADE' clause is used.
        documentRecordDao.delete(record, getDataSource());
    }
}
