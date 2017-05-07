package com.svnavigatoru600.repository.records.impl.direct;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;
import com.svnavigatoru600.repository.AbstractDaoImpl;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.repository.records.OtherDocumentRecordTypeRelationDao;
import com.svnavigatoru600.repository.records.impl.DocumentRecordFieldEnum;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordFieldEnum;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordTypeRelationFieldEnum;
import com.svnavigatoru600.service.util.OrderTypeEnum;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("otherDocumentRecordDao")
@Transactional
public class OtherDocumentRecordDaoImpl extends AbstractDaoImpl implements OtherDocumentRecordDao {

    /**
     * Database table which provides a persistence of {@link OtherDocumentRecord OtherDocumentRecords}.
     */
    private static final String TABLE_NAME = "other_document_records";

    /**
     * The SELECT command for the return of a single document together with its BLOB file.
     *
     * Join is necessary in all SELECT queries since {@link OtherDocumentRecord} inherits from
     * {@link AbstractDocumentRecord}.
     */
    private static final String SELECT_FROM_CLAUSE_WITH_FILE = String.format(
            "SELECT r.*, d.%s, d.%s FROM %s r INNER JOIN %s d ON d.%s = r.%s",
            DocumentRecordFieldEnum.FILE_NAME.getColumnName(), DocumentRecordFieldEnum.FILE.getColumnName(),
            OtherDocumentRecordDaoImpl.TABLE_NAME, DocumentRecordDaoImpl.TABLE_NAME,
            DocumentRecordFieldEnum.ID.getColumnName(), OtherDocumentRecordFieldEnum.ID.getColumnName());
    /**
     * The SELECT command for the return of a single or multiple documents without their BLOB files.
     *
     * Join is necessary in all SELECT queries since {@link OtherDocumentRecord} inherits from
     * {@link AbstractDocumentRecord}.
     */
    private static final String SELECT_FROM_CLAUSE_WITHOUT_FILE = String.format(
            "SELECT r.*, d.%s FROM %s r INNER JOIN %s d ON d.%s = r.%s",
            DocumentRecordFieldEnum.FILE_NAME.getColumnName(), OtherDocumentRecordDaoImpl.TABLE_NAME,
            DocumentRecordDaoImpl.TABLE_NAME, DocumentRecordFieldEnum.ID.getColumnName(),
            OtherDocumentRecordFieldEnum.ID.getColumnName());

    private final DocumentRecordDaoImpl documentRecordDao;
    private final OtherDocumentRecordTypeRelationDao typeDao;

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public OtherDocumentRecordDaoImpl(final DocumentRecordDaoImpl documentRecordDao,
            final OtherDocumentRecordTypeRelationDao typeDao) {
        this.documentRecordDao = documentRecordDao;
        this.typeDao = typeDao;
    }

    /**
     * Populates the <code>types</code> property of the given <code>record</code>.
     */
    private void populateTypes(final OtherDocumentRecord record) {
        final List<OtherDocumentRecordTypeRelation> types = typeDao.findByRecordId(record.getId());
        record.setTypes(Sets.newHashSet(types));
    }

    /**
     * Populates the <code>types</code> property of all the given <code>records</code>.
     */
    private void populateTypes(final List<OtherDocumentRecord> records) {
        for (final OtherDocumentRecord record : records) {
            this.populateTypes(record);
        }
    }

    @Override
    public OtherDocumentRecord findById(final int recordId) {
        return this.findById(recordId, true);
    }

    @Override
    public OtherDocumentRecord findById(final int recordId, final boolean loadFile) {
        String selectClause;
        if (loadFile) {
            selectClause = OtherDocumentRecordDaoImpl.SELECT_FROM_CLAUSE_WITH_FILE;
        } else {
            selectClause = OtherDocumentRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE;
        }

        final String idColumn = OtherDocumentRecordFieldEnum.ID.getColumnName();
        final String query = String.format("%s WHERE r.%s = :%s", selectClause, idColumn, idColumn);

        final Map<String, Integer> args = Collections.singletonMap(idColumn, recordId);

        final OtherDocumentRecord record = getNamedParameterJdbcTemplate().queryForObject(query, args,
                new OtherDocumentRecordRowMapper(loadFile));
        if (record == null) {
            throw new DataRetrievalFailureException(String.format("No record with the ID '%s' exists.", recordId));
        }

        this.populateTypes(record);
        return record;
    }

    @Override
    public List<OtherDocumentRecord> findAllOrdered(final OrderTypeEnum order) {
        final String query = String.format("%s ORDER BY r.%s %s",
                OtherDocumentRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE,
                OtherDocumentRecordFieldEnum.CREATION_TIME.getColumnName(), order.getDatabaseCode());

        final List<OtherDocumentRecord> records = getJdbcTemplate().query(query,
                new OtherDocumentRecordRowMapper(false));

        this.populateTypes(records);
        return records;
    }

    @Override
    public List<OtherDocumentRecord> findAllOrdered(final OtherDocumentRecordTypeEnum type, final OrderTypeEnum order) {
        final String typeColumn = OtherDocumentRecordTypeRelationFieldEnum.TYPE.getColumnName();
        final String query = String.format("%s INNER JOIN %s t ON t.%s = r.%s WHERE t.%s = :%s ORDER BY r.%s %s",
                OtherDocumentRecordDaoImpl.SELECT_FROM_CLAUSE_WITHOUT_FILE,
                OtherDocumentRecordTypeRelationDaoImpl.TABLE_NAME,
                OtherDocumentRecordTypeRelationFieldEnum.RECORD_ID.getColumnName(),
                OtherDocumentRecordFieldEnum.ID.getColumnName(), typeColumn, typeColumn,
                OtherDocumentRecordFieldEnum.CREATION_TIME.getColumnName(), order.getDatabaseCode());

        final Map<String, String> args = Collections.singletonMap(typeColumn, type.name());

        final List<OtherDocumentRecord> records = getNamedParameterJdbcTemplate().query(query, args,
                new OtherDocumentRecordRowMapper(false));

        this.populateTypes(records);
        return records;
    }

    /**
     * Maps properties of the given {@link OtherDocumentRecord} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(final OtherDocumentRecord record) {
        final Map<String, Object> parameters = Maps.newHashMap();
        parameters.put(OtherDocumentRecordFieldEnum.ID.getColumnName(), record.getId());
        parameters.put(OtherDocumentRecordFieldEnum.NAME.getColumnName(), record.getName());
        parameters.put(OtherDocumentRecordFieldEnum.DESCRIPTION.getColumnName(), record.getDescription());
        parameters.put(OtherDocumentRecordFieldEnum.CREATION_TIME.getColumnName(), record.getCreationTime());
        parameters.put(OtherDocumentRecordFieldEnum.LAST_SAVE_TIME.getColumnName(), record.getLastSaveTime());
        return parameters;
    }

    @Override
    public void update(final OtherDocumentRecord record) {
        final Date now = new Date();
        record.setLastSaveTime(now);

        // NOTE: updates the record in both tables 'document_records' and
        // 'other_document_records'.
        documentRecordDao.update(record, getDataSource());

        final String idColumn = OtherDocumentRecordFieldEnum.ID.getColumnName();
        final String nameColumn = OtherDocumentRecordFieldEnum.NAME.getColumnName();
        final String descriptionColumn = OtherDocumentRecordFieldEnum.DESCRIPTION.getColumnName();
        final String lastSaveTimeColumn = OtherDocumentRecordFieldEnum.LAST_SAVE_TIME.getColumnName();
        final String query = String.format("UPDATE %s SET %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                OtherDocumentRecordDaoImpl.TABLE_NAME, nameColumn, nameColumn, descriptionColumn, descriptionColumn,
                lastSaveTimeColumn, lastSaveTimeColumn, idColumn, idColumn);

        doUpdate(query, getNamedParameters(record));

        // Updates types.
        typeDao.delete(record.getId());
        typeDao.save(record.getTypes());
    }

    @Override
    public int save(final OtherDocumentRecord record) {
        final Date now = new Date();
        record.setCreationTime(now);
        record.setLastSaveTime(now);

        // NOTE: inserts the new record into two tables: 'document_records' and
        // 'other_document_records'.
        final int recordId = documentRecordDao.save(record, getDataSource());

        final SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource())
                .withTableName(OtherDocumentRecordDaoImpl.TABLE_NAME)
                .usingColumns(OtherDocumentRecordFieldEnum.ID.getColumnName(),
                        OtherDocumentRecordFieldEnum.NAME.getColumnName(),
                        OtherDocumentRecordFieldEnum.DESCRIPTION.getColumnName(),
                        OtherDocumentRecordFieldEnum.CREATION_TIME.getColumnName(),
                        OtherDocumentRecordFieldEnum.LAST_SAVE_TIME.getColumnName());

        final Map<String, Object> parameters = getNamedParameters(record);
        parameters.put(OtherDocumentRecordFieldEnum.ID.getColumnName(), recordId);
        insert.execute(parameters);

        final Set<OtherDocumentRecordTypeRelation> types = record.getTypes();
        if (types != null) {
            // This IF is valid since the types may be saved later on when the
            // generated identifier is available. See
            // com.svnavigatoru600.web.records.otherdocuments.NewDocumentController.

            // NOTE: explicit save of the record's types.
            typeDao.save(record.getTypes());
        }

        return recordId;
    }

    @Override
    public void delete(final AbstractDocumentRecord record) {
        // The 'ON DELETE CASCADE' clause is used.
        documentRecordDao.delete(record, getDataSource());
    }
}
