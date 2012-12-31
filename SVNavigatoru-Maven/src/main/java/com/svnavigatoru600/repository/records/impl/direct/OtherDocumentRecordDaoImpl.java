package com.svnavigatoru600.repository.records.impl.direct;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.repository.records.OtherDocumentRecordTypeRelationDao;
import com.svnavigatoru600.repository.records.impl.DocumentRecordField;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordField;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordTypeRelationField;
import com.svnavigatoru600.service.util.OrderType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class OtherDocumentRecordDaoImpl extends SimpleJdbcDaoSupport implements OtherDocumentRecordDao {

    private static final String TABLE_NAME = PersistedClass.OtherDocumentRecord.getTableName();

    /**
     * The SELECT command for the return of a single document together with its BLOB file.
     * 
     * Join is necessary in all SELECT queries since {@link OtherDocumentRecord} inherits from
     * {@link AbstractDocumentRecord}.
     */
    private static final String SELECT_FROM_CLAUSE_WITH_FILE = String.format(
            "SELECT r.*, d.%s, d.%s FROM %s r INNER JOIN %s d ON d.%s = r.%s",
            DocumentRecordField.fileName.getColumnName(), DocumentRecordField.file.getColumnName(),
            OtherDocumentRecordDaoImpl.TABLE_NAME, PersistedClass.DocumentRecord.getTableName(),
            DocumentRecordField.id.getColumnName(), OtherDocumentRecordField.id.getColumnName());
    /**
     * The SELECT command for the return of a single or multiple documents without their BLOB files.
     * 
     * Join is necessary in all SELECT queries since {@link OtherDocumentRecord} inherits from
     * {@link AbstractDocumentRecord}.
     */
    private static final String SELECT_FROM_CLAUSE_WITHOUT_FILE = String.format(
            "SELECT r.*, d.%s FROM %s r INNER JOIN %s d ON d.%s = r.%s",
            DocumentRecordField.fileName.getColumnName(), OtherDocumentRecordDaoImpl.TABLE_NAME,
            PersistedClass.DocumentRecord.getTableName(), DocumentRecordField.id.getColumnName(),
            OtherDocumentRecordField.id.getColumnName());

    private DocumentRecordDaoImpl documentRecordDao = new DocumentRecordDaoImpl();
    private OtherDocumentRecordTypeRelationDao typeDao;

    @Inject
    public void setOtherDocumentRecordTypeRelationDao(OtherDocumentRecordTypeRelationDao typeDao) {
        this.typeDao = typeDao;
    }

    /**
     * Populates the <code>types</code> property of the given <code>record</code>.
     */
    private void populateTypes(OtherDocumentRecord record) {
        List<OtherDocumentRecordTypeRelation> types = this.typeDao.find(record.getId());
        record.setTypes(new HashSet<OtherDocumentRecordTypeRelation>(types));
    }

    /**
     * Populates the <code>types</code> property of all the given <code>records</code>.
     */
    private void populateTypes(List<OtherDocumentRecord> records) {
        for (OtherDocumentRecord record : records) {
            this.populateTypes(record);
        }
    }

    @Override
    public OtherDocumentRecord findById(int recordId) {
        return this.findById(recordId, true);
    }

    @Override
    public OtherDocumentRecord findById(int recordId, boolean loadFile) {
        String selectClause;
        OtherDocumentRecordRowMapper rowMapper;
        if (loadFile) {
            selectClause = OtherDocumentRecordDaoImpl.SELECT_FROM_CLAUSE_WITH_FILE;
            rowMapper = new OtherDocumentRecordRowMapper(true);
        } else {
            selectClause = OtherDocumentRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE;
            rowMapper = new OtherDocumentRecordRowMapper(false);
        }

        String query = String.format("%s WHERE r.%s = ?", selectClause,
                OtherDocumentRecordField.id.getColumnName());
        OtherDocumentRecord record = this.getSimpleJdbcTemplate().queryForObject(query, rowMapper, recordId);
        if (record == null) {
            throw new DataRetrievalFailureException(String.format("No record with the ID '%s' exists.",
                    recordId));
        }

        this.populateTypes(record);
        return record;
    }

    @Override
    public OtherDocumentRecord findByFileName(String fileName) {
        String query = String.format("%s WHERE r.%s = ?",
                OtherDocumentRecordDaoImpl.SELECT_FROM_CLAUSE_WITH_FILE,
                DocumentRecordField.fileName.getColumnName());
        OtherDocumentRecord record = this.getSimpleJdbcTemplate().queryForObject(query,
                new OtherDocumentRecordRowMapper(), fileName);
        if (record == null) {
            throw new DataRetrievalFailureException(String.format("No record with the filename '%s' exists.",
                    fileName));
        }

        this.populateTypes(record);
        return record;
    }

    @Override
    public List<OtherDocumentRecord> findOrdered(OrderType order) {
        String query = String.format("%s ORDER BY r.%s %s",
                OtherDocumentRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE,
                OtherDocumentRecordField.creationTime.getColumnName(), order.getDatabaseCode());
        List<OtherDocumentRecord> records = this.getSimpleJdbcTemplate().query(query,
                new OtherDocumentRecordRowMapper(false));

        this.populateTypes(records);
        return records;
    }

    @Override
    public List<OtherDocumentRecord> findOrdered(OtherDocumentRecordType type, OrderType order) {
        String query = String.format("%s INNER JOIN %s t ON t.%s = r.%s WHERE t.%s = ? ORDER BY r.%s %s",
                OtherDocumentRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE,
                PersistedClass.OtherDocumentRecordTypeRelation.getTableName(),
                OtherDocumentRecordTypeRelationField.recordId.getColumnName(),
                OtherDocumentRecordField.id.getColumnName(),
                OtherDocumentRecordTypeRelationField.type.getColumnName(),
                OtherDocumentRecordField.creationTime.getColumnName(), order.getDatabaseCode());
        List<OtherDocumentRecord> records = this.getSimpleJdbcTemplate().query(query,
                new OtherDocumentRecordRowMapper(false), type.name());

        this.populateTypes(records);
        return records;
    }

    @Override
    public void update(OtherDocumentRecord record) {
        Date now = new Date();
        record.setLastSaveTime(now);

        // NOTE: updates the record in both tables 'document_records' and
        // 'other_document_records'.
        this.documentRecordDao.update(record, this.getDataSource());

        String query = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
                OtherDocumentRecordDaoImpl.TABLE_NAME, OtherDocumentRecordField.name.getColumnName(),
                OtherDocumentRecordField.description.getColumnName(),
                OtherDocumentRecordField.creationTime.getColumnName(),
                OtherDocumentRecordField.lastSaveTime.getColumnName(),
                OtherDocumentRecordField.id.getColumnName());
        this.getSimpleJdbcTemplate().update(query, record.getName(), record.getDescription(),
                record.getCreationTime(), record.getLastSaveTime(), record.getId());

        // Updates types.
        this.typeDao.delete(record.getId());
        this.typeDao.save(record.getTypes());
    }

    /**
     * Used during the save of the given <code>record</code>.
     */
    private Map<String, Object> getNamedParameters(OtherDocumentRecord record) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(OtherDocumentRecordField.id.getColumnName(), record.getId());
        parameters.put(OtherDocumentRecordField.name.getColumnName(), record.getName());
        parameters.put(OtherDocumentRecordField.description.getColumnName(), record.getDescription());
        parameters.put(OtherDocumentRecordField.creationTime.getColumnName(), record.getCreationTime());
        parameters.put(OtherDocumentRecordField.lastSaveTime.getColumnName(), record.getLastSaveTime());
        return parameters;
    }

    @Override
    public int save(OtherDocumentRecord record) {
        Date now = new Date();
        record.setCreationTime(now);
        record.setLastSaveTime(now);

        // NOTE: inserts the new record into two tables: 'document_records' and
        // 'other_document_records'.
        int recordId = this.documentRecordDao.save(record, this.getDataSource());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource()).withTableName(
                OtherDocumentRecordDaoImpl.TABLE_NAME).usingColumns(
                OtherDocumentRecordField.id.getColumnName(), OtherDocumentRecordField.name.getColumnName(),
                OtherDocumentRecordField.description.getColumnName(),
                OtherDocumentRecordField.creationTime.getColumnName(),
                OtherDocumentRecordField.lastSaveTime.getColumnName());

        Map<String, Object> parameters = this.getNamedParameters(record);
        parameters.put(OtherDocumentRecordField.id.getColumnName(), recordId);
        insert.execute(parameters);

        Set<OtherDocumentRecordTypeRelation> types = record.getTypes();
        if (types != null) {
            // This IF is valid since the types may be saved later on when the
            // generated identifier is available. See
            // com.svnavigatoru600.web.records.otherdocuments.NewDocumentController.

            // NOTE: explicit save of the record's types.
            this.typeDao.save(record.getTypes());
        }

        return recordId;
    }

    @Override
    public void delete(AbstractDocumentRecord record) {
        // The 'ON DELETE CASCADE' clause is used.
        this.documentRecordDao.delete(record, this.getDataSource());
    }
}
