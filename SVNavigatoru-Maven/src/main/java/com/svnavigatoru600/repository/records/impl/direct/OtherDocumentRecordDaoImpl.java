package com.svnavigatoru600.repository.records.impl.direct;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

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
@Repository("otherDocumentRecordDao")
public class OtherDocumentRecordDaoImpl extends NamedParameterJdbcDaoSupport implements
        OtherDocumentRecordDao {

    /**
     * Database table which provides a persistence of {@link OtherDocumentRecord OtherDocumentRecords}.
     */
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
            OtherDocumentRecordDaoImpl.TABLE_NAME, PersistedClass.AbstractDocumentRecord.getTableName(),
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
            PersistedClass.AbstractDocumentRecord.getTableName(), DocumentRecordField.id.getColumnName(),
            OtherDocumentRecordField.id.getColumnName());

    @Inject
    private DocumentRecordDaoImpl documentRecordDao;

    @Inject
    private OtherDocumentRecordTypeRelationDao typeDao;

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public OtherDocumentRecordDaoImpl(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }

    /**
     * Populates the <code>types</code> property of the given <code>record</code>.
     */
    private void populateTypes(OtherDocumentRecord record) {
        List<OtherDocumentRecordTypeRelation> types = this.typeDao.findAll(record.getId());
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
        if (loadFile) {
            selectClause = OtherDocumentRecordDaoImpl.SELECT_FROM_CLAUSE_WITH_FILE;
        } else {
            selectClause = OtherDocumentRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE;
        }

        String idColumn = OtherDocumentRecordField.id.getColumnName();
        String query = String.format("%s WHERE r.%s = :%s", selectClause, idColumn, idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, recordId);

        OtherDocumentRecord record = getNamedParameterJdbcTemplate().queryForObject(query, args,
                new OtherDocumentRecordRowMapper(loadFile));
        if (record == null) {
            throw new DataRetrievalFailureException(String.format("No record with the ID '%s' exists.",
                    recordId));
        }

        this.populateTypes(record);
        return record;
    }

    @Override
    public List<OtherDocumentRecord> findAllOrdered(OrderType order) {
        String query = String.format("%s ORDER BY r.%s %s",
                OtherDocumentRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE,
                OtherDocumentRecordField.creationTime.getColumnName(), order.getDatabaseCode());

        List<OtherDocumentRecord> records = getJdbcTemplate().query(query,
                new OtherDocumentRecordRowMapper(false));

        this.populateTypes(records);
        return records;
    }

    @Override
    public List<OtherDocumentRecord> findAllOrdered(OtherDocumentRecordType type, OrderType order) {
        String typeColumn = OtherDocumentRecordTypeRelationField.type.getColumnName();
        String query = String.format("%s INNER JOIN %s t ON t.%s = r.%s WHERE t.%s = :%s ORDER BY r.%s %s",
                OtherDocumentRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE,
                PersistedClass.OtherDocumentRecordTypeRelation.getTableName(),
                OtherDocumentRecordTypeRelationField.recordId.getColumnName(),
                OtherDocumentRecordField.id.getColumnName(), typeColumn, typeColumn,
                OtherDocumentRecordField.creationTime.getColumnName(), order.getDatabaseCode());

        Map<String, String> args = Collections.singletonMap(typeColumn, type.name());

        List<OtherDocumentRecord> records = getNamedParameterJdbcTemplate().query(query, args,
                new OtherDocumentRecordRowMapper(false));

        this.populateTypes(records);
        return records;
    }

    /**
     * Maps properties of the given {@link OtherDocumentRecord} to names of the corresponding database
     * columns.
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
    public void update(OtherDocumentRecord record) {
        Date now = new Date();
        record.setLastSaveTime(now);

        // NOTE: updates the record in both tables 'document_records' and
        // 'other_document_records'.
        this.documentRecordDao.update(record, getDataSource());

        String idColumn = OtherDocumentRecordField.id.getColumnName();
        String nameColumn = OtherDocumentRecordField.name.getColumnName();
        String descriptionColumn = OtherDocumentRecordField.description.getColumnName();
        String lastSaveTimeColumn = OtherDocumentRecordField.lastSaveTime.getColumnName();
        String query = String.format("UPDATE %s SET %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                OtherDocumentRecordDaoImpl.TABLE_NAME, nameColumn, nameColumn, descriptionColumn,
                descriptionColumn, lastSaveTimeColumn, lastSaveTimeColumn, idColumn, idColumn);

        getNamedParameterJdbcTemplate().update(query, getNamedParameters(record));

        // Updates types.
        this.typeDao.delete(record.getId());
        this.typeDao.save(record.getTypes());
    }

    @Override
    public int save(OtherDocumentRecord record) {
        Date now = new Date();
        record.setCreationTime(now);
        record.setLastSaveTime(now);

        // NOTE: inserts the new record into two tables: 'document_records' and
        // 'other_document_records'.
        int recordId = this.documentRecordDao.save(record, getDataSource());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource()).withTableName(
                OtherDocumentRecordDaoImpl.TABLE_NAME).usingColumns(
                OtherDocumentRecordField.id.getColumnName(), OtherDocumentRecordField.name.getColumnName(),
                OtherDocumentRecordField.description.getColumnName(),
                OtherDocumentRecordField.creationTime.getColumnName(),
                OtherDocumentRecordField.lastSaveTime.getColumnName());

        Map<String, Object> parameters = getNamedParameters(record);
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
        this.documentRecordDao.delete(record, getDataSource());
    }
}
