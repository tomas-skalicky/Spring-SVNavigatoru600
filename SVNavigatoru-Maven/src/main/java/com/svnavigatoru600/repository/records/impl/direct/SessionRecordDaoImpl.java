package com.svnavigatoru600.repository.records.impl.direct;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
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
public class SessionRecordDaoImpl extends NamedParameterJdbcDaoSupport implements SessionRecordDao {

    /**
     * Database table which provides a persistence of {@link SessionRecord SessionRecords}.
     */
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
            SessionRecordDaoImpl.TABLE_NAME, PersistedClass.AbstractDocumentRecord.getTableName(),
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
            PersistedClass.AbstractDocumentRecord.getTableName(), DocumentRecordField.id.getColumnName(),
            SessionRecordField.id.getColumnName());

    private DocumentRecordDaoImpl documentRecordDao = new DocumentRecordDaoImpl();

    @Override
    public SessionRecord findById(int recordId) {
        return this.findById(recordId, true);
    }

    @Override
    public SessionRecord findById(int recordId, boolean loadFile) {
        final String selectClause;
        if (loadFile) {
            selectClause = SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITH_FILE;
        } else {
            selectClause = SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE;
        }

        String idColumn = SessionRecordField.id.getColumnName();
        String query = String.format("%s WHERE r.%s = :%s", selectClause, idColumn, idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, recordId);

        return this.getNamedParameterJdbcTemplate().queryForObject(query, args,
                new SessionRecordRowMapper(loadFile));
    }

    @Override
    public SessionRecord findByFileName(String fileName) {
        String fileNameColumn = DocumentRecordField.fileName.getColumnName();
        String query = String.format("%s WHERE r.%s = :%s",
                SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITH_FILE, fileNameColumn, fileNameColumn);

        Map<String, String> args = Collections.singletonMap(fileNameColumn, fileName);

        return this.getNamedParameterJdbcTemplate().queryForObject(query, args, new SessionRecordRowMapper());
    }

    @Override
    public List<SessionRecord> findAllOrdered(OrderType order) {
        String query = String.format("%s ORDER BY r.%s %s",
                SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE,
                SessionRecordField.sessionDate.getColumnName(), order.getDatabaseCode());

        return this.getJdbcTemplate().query(query, new SessionRecordRowMapper(false));
    }

    @Override
    public List<SessionRecord> findAllOrdered(SessionRecordType type, OrderType order) {
        String typeColumn = SessionRecordField.type.getColumnName();
        String query = String.format("%s WHERE r.%s = :%s ORDER BY r.%s %s",
                SessionRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE, typeColumn, typeColumn,
                SessionRecordField.sessionDate.getColumnName(), order.getDatabaseCode());

        Map<String, String> args = Collections.singletonMap(typeColumn, type.name());

        return this.getNamedParameterJdbcTemplate().query(query, args, new SessionRecordRowMapper(false));
    }

    /**
     * Maps properties of the given {@link SessionRecord} to names of the corresponding database columns.
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
    public void update(SessionRecord record) {
        // NOTE: updates the record in both tables 'document_records' and
        // 'session_records'.
        this.documentRecordDao.update(record, this.getDataSource());

        String idColumn = SessionRecordField.id.getColumnName();
        String typeColumn = SessionRecordField.type.getColumnName();
        String sessionDateColumn = SessionRecordField.sessionDate.getColumnName();
        String discussedTopicsColumn = SessionRecordField.discussedTopics.getColumnName();
        String query = String.format("UPDATE %s SET %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                SessionRecordDaoImpl.TABLE_NAME, typeColumn, typeColumn, sessionDateColumn,
                sessionDateColumn, discussedTopicsColumn, discussedTopicsColumn, idColumn, idColumn);

        this.getNamedParameterJdbcTemplate().update(query, this.getNamedParameters(record));
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
