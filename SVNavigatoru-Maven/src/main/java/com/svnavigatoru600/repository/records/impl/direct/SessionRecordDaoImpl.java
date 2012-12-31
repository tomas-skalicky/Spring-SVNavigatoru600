package com.svnavigatoru600.repository.records.impl.direct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.repository.records.impl.DocumentRecordField;
import com.svnavigatoru600.repository.records.impl.SessionRecordField;
import com.svnavigatoru600.service.util.OrderType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class SessionRecordDaoImpl extends SimpleJdbcDaoSupport implements SessionRecordDao {

    private static final String TABLE_NAME = PersistedClass.SessionRecord.getTableName();

    /**
     * The SELECT command for the return of a single document together with its BLOB file.
     * 
     * Join is necessary in all SELECT queries since {@link SessionRecord} inherits from
     * {@link AbstractDocumentRecord}.
     */
    private static final String SELECT_FROM_CLAUSE_WITH_FILE = String.format(
            "SELECT r.*, d.%s, d.%s FROM %s r INNER JOIN %s d ON d.%s = r.%s",
            DocumentRecordField.fileName.getColumnName(), DocumentRecordField.file.getColumnName(),
            SessionRecordDaoImpl.TABLE_NAME, PersistedClass.DocumentRecord.getTableName(),
            DocumentRecordField.id.getColumnName(), SessionRecordField.id.getColumnName());
    /**
     * The SELECT command for the return of a single or multiple documents without their BLOB files.
     * 
     * Join is necessary in all SELECT queries since
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord} inherits from
     * {@link AbstractDocumentRecord}.
     */
    private static final String SELECT_FROM_CLAUSE_WITHOUT_FILE = String.format(
            "SELECT r.*, d.%s FROM %s r INNER JOIN %s d ON d.%s = r.%s",
            DocumentRecordField.fileName.getColumnName(), SessionRecordDaoImpl.TABLE_NAME,
            PersistedClass.DocumentRecord.getTableName(), DocumentRecordField.id.getColumnName(),
            SessionRecordField.id.getColumnName());

    private DocumentRecordDaoImpl documentRecordDao = new DocumentRecordDaoImpl();

    @Override
    public SessionRecord findById(int recordId) {
        return this.findById(recordId, true);
    }

    @Override
    public SessionRecord findById(int recordId, boolean loadFile) {
        String selectClause;
        SessionRecordRowMapper rowMapper;
        if (loadFile) {
            selectClause = SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITH_FILE;
            rowMapper = new SessionRecordRowMapper(true);
        } else {
            selectClause = SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE;
            rowMapper = new SessionRecordRowMapper(false);
        }

        String query = String
                .format("%s WHERE r.%s = ?", selectClause, SessionRecordField.id.getColumnName());
        return this.getSimpleJdbcTemplate().queryForObject(query, rowMapper, recordId);
    }

    @Override
    public SessionRecord findByFileName(String fileName) {
        String query = String.format("%s WHERE r.%s = ?", SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITH_FILE,
                DocumentRecordField.fileName.getColumnName());
        return this.getSimpleJdbcTemplate().queryForObject(query, new SessionRecordRowMapper(), fileName);
    }

    @Override
    public List<SessionRecord> findAllOrdered(OrderType order) {
        String query = String.format("%s ORDER BY r.%s %s",
                SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE,
                SessionRecordField.sessionDate.getColumnName(), order.getDatabaseCode());
        return this.getSimpleJdbcTemplate().query(query, new SessionRecordRowMapper(false));
    }

    @Override
    public List<SessionRecord> findAllOrdered(SessionRecordType type, OrderType order) {
        String query = String.format("%s WHERE r.%s = ? ORDER BY r.%s %s",
                SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE,
                SessionRecordField.type.getColumnName(), SessionRecordField.sessionDate.getColumnName(),
                order.getDatabaseCode());
        return this.getSimpleJdbcTemplate().query(query, new SessionRecordRowMapper(false), type.name());
    }

    @Override
    public void update(SessionRecord record) {
        // NOTE: updates the record in both tables 'document_records' and
        // 'session_records'.
        this.documentRecordDao.update(record, this.getDataSource());

        String query = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?",
                SessionRecordDaoImpl.TABLE_NAME, SessionRecordField.type.getColumnName(),
                SessionRecordField.sessionDate.getColumnName(),
                SessionRecordField.discussedTopics.getColumnName(), SessionRecordField.id.getColumnName());
        this.getSimpleJdbcTemplate().update(query, record.getType(), record.getSessionDate(),
                record.getDiscussedTopics(), record.getId());
    }

    /**
     * Used during the save of the given <code>record</code>.
     */
    private Map<String, Object> getNamedParameters(SessionRecord record) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(SessionRecordField.id.getColumnName(), record.getId());
        parameters.put(SessionRecordField.type.getColumnName(), record.getType());
        parameters.put(SessionRecordField.sessionDate.getColumnName(), record.getSessionDate());
        parameters.put(SessionRecordField.discussedTopics.getColumnName(), record.getDiscussedTopics());
        return parameters;
    }

    @Override
    public int save(SessionRecord record) {
        // NOTE: inserts the new record into two tables: 'document_records' and
        // 'session_records'.
        int recordId = this.documentRecordDao.save(record, this.getDataSource());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource()).withTableName(
                SessionRecordDaoImpl.TABLE_NAME).usingColumns(SessionRecordField.id.getColumnName(),
                SessionRecordField.type.getColumnName(), SessionRecordField.sessionDate.getColumnName(),
                SessionRecordField.discussedTopics.getColumnName());

        Map<String, Object> parameters = this.getNamedParameters(record);
        parameters.put(SessionRecordField.id.getColumnName(), recordId);
        insert.execute(parameters);

        return recordId;
    }

    @Override
    public void delete(AbstractDocumentRecord record) {
        // The 'ON DELETE CASCADE' clause is used.
        this.documentRecordDao.delete(record, this.getDataSource());
    }
}
