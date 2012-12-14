package com.svnavigatoru600.repository.records.impl.direct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.svnavigatoru600.domain.records.DocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.service.util.OrderType;

public class SessionRecordDaoImpl extends SimpleJdbcDaoSupport implements SessionRecordDao {

    private static final String TABLE_NAME = "session_records";

    /**
     * The SELECT command for the return of a single document together with its BLOB file.
     * 
     * Join is necessary in all SELECT queries since {@link SessionRecord} inherits from
     * {@link DocumentRecord}.
     */
    private static final String SELECT_FROM_CLAUSE_WITH_FILE = String.format(
            "SELECT r.*, d.%s, d.%s FROM %s r INNER JOIN %s d ON d.%s = r.%s",
            DocumentRecordRowMapper.getColumn("fileName"), DocumentRecordRowMapper.getColumn("file"),
            SessionRecordDaoImpl.TABLE_NAME, DocumentRecordDaoImpl.TABLE_NAME,
            DocumentRecordRowMapper.getColumn("id"), SessionRecordRowMapper.getColumn("id"));
    /**
     * The SELECT command for the return of a single or multiple documents without their BLOB files.
     * 
     * Join is necessary in all SELECT queries since {@link OtherDocumentRecord} inherits from
     * {@link DocumentRecord}.
     */
    private static final String SELECT_FROM_CLAUSE_WITHOUT_FILE = String.format(
            "SELECT r.*, d.%s FROM %s r INNER JOIN %s d ON d.%s = r.%s",
            DocumentRecordRowMapper.getColumn("fileName"), SessionRecordDaoImpl.TABLE_NAME,
            DocumentRecordDaoImpl.TABLE_NAME, DocumentRecordRowMapper.getColumn("id"),
            SessionRecordRowMapper.getColumn("id"));

    private DocumentRecordDaoImpl documentRecordDao = new DocumentRecordDaoImpl();

    public SessionRecord findById(int recordId) {
        return this.findById(recordId, true);
    }

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

        String query = String.format("%s WHERE r.%s = ?", selectClause,
                SessionRecordRowMapper.getColumn("id"));
        return this.getSimpleJdbcTemplate().queryForObject(query, rowMapper, recordId);
    }

    public SessionRecord findByFileName(String fileName) {
        String query = String.format("%s WHERE r.%s = ?", SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITH_FILE,
                SessionRecordRowMapper.getColumn("fileName"));
        return this.getSimpleJdbcTemplate().queryForObject(query, new SessionRecordRowMapper(), fileName);
    }

    public List<SessionRecord> findOrdered(OrderType order) {
        String query = String.format("%s ORDER BY r.%s %s",
                SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE,
                SessionRecordRowMapper.getColumn("sessionDate"), order.getDatabaseCode());
        return this.getSimpleJdbcTemplate().query(query, new SessionRecordRowMapper(false));
    }

    public List<SessionRecord> findOrdered(SessionRecordType type, OrderType order) {
        String query = String.format("%s WHERE r.%s = ? ORDER BY r.%s %s",
                SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE,
                SessionRecordRowMapper.getColumn("type"), SessionRecordRowMapper.getColumn("sessionDate"),
                order.getDatabaseCode());
        return this.getSimpleJdbcTemplate().query(query, new SessionRecordRowMapper(false), type.name());
    }

    public void update(SessionRecord record) {
        // NOTE: updates the record in both tables 'document_records' and
        // 'session_records'.
        this.documentRecordDao.update(record, this.getDataSource());

        String query = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?",
                SessionRecordDaoImpl.TABLE_NAME, SessionRecordRowMapper.getColumn("type"),
                SessionRecordRowMapper.getColumn("sessionDate"),
                SessionRecordRowMapper.getColumn("discussedTopics"), SessionRecordRowMapper.getColumn("id"));
        this.getSimpleJdbcTemplate().update(query, record.getType(), record.getSessionDate(),
                record.getDiscussedTopics(), record.getId());
    }

    /**
     * Used during the save of the given <code>record</code>.
     */
    private Map<String, Object> getNamedParameters(SessionRecord record) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(SessionRecordRowMapper.getColumn("id"), record.getId());
        parameters.put(SessionRecordRowMapper.getColumn("type"), record.getType());
        parameters.put(SessionRecordRowMapper.getColumn("sessionDate"), record.getSessionDate());
        parameters.put(SessionRecordRowMapper.getColumn("discussedTopics"), record.getDiscussedTopics());
        return parameters;
    }

    public int save(SessionRecord record) {
        // NOTE: inserts the new record into two tables: 'document_records' and
        // 'session_records'.
        int recordId = this.documentRecordDao.save(record, this.getDataSource());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource()).withTableName(
                SessionRecordDaoImpl.TABLE_NAME).usingColumns(SessionRecordRowMapper.getColumn("id"),
                SessionRecordRowMapper.getColumn("type"), SessionRecordRowMapper.getColumn("sessionDate"),
                SessionRecordRowMapper.getColumn("discussedTopics"));

        Map<String, Object> parameters = this.getNamedParameters(record);
        parameters.put(SessionRecordRowMapper.getColumn("id"), recordId);
        insert.execute(parameters);

        return recordId;
    }

    public void delete(DocumentRecord record) {
        // The 'ON DELETE CASCADE' clause is used.
        this.documentRecordDao.delete(record, this.getDataSource());
    }
}
