package com.svnavigatoru600.repository.records.impl.direct;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelationId;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.records.OtherDocumentRecordTypeRelationDao;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordTypeRelationField;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("otherDocumentRecordTypeRelationDao")
public class OtherDocumentRecordTypeRelationDaoImpl extends NamedParameterJdbcDaoSupport implements
        OtherDocumentRecordTypeRelationDao {

    /**
     * Database table which provides a persistence of {@link OtherDocumentRecordTypeRelation
     * OtherDocumentRecordTypeRelations}.
     */
    private static final String TABLE_NAME = PersistedClass.OtherDocumentRecordTypeRelation.getTableName();

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public OtherDocumentRecordTypeRelationDaoImpl(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }

    @Override
    public List<OtherDocumentRecordTypeRelation> findAll(int recordId) {
        String recordIdColumn = OtherDocumentRecordTypeRelationField.recordId.getColumnName();
        String query = String.format("SELECT * FROM %s r WHERE r.%s = :%s",
                OtherDocumentRecordTypeRelationDaoImpl.TABLE_NAME, recordIdColumn, recordIdColumn);

        Map<String, Integer> args = Collections.singletonMap(recordIdColumn, recordId);

        return getNamedParameterJdbcTemplate().query(query, args,
                new OtherDocumentRecordTypeRelationRowMapper());
    }

    /**
     * Maps properties of the given {@link OtherDocumentRecordTypeRelation typeRelation} to names of the
     * corresponding database column.
     */
    private Map<String, Object> getNamedParameters(OtherDocumentRecordTypeRelation typeRelation) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        OtherDocumentRecordTypeRelationId id = typeRelation.getId();
        parameters.put(OtherDocumentRecordTypeRelationField.recordId.getColumnName(), id.getRecordId());
        parameters.put(OtherDocumentRecordTypeRelationField.type.getColumnName(), id.getType());
        return parameters;
    }

    /**
     * Saves the given {@link OtherDocumentRecordTypeRelation typeRelation} to the repository.
     */
    public void save(OtherDocumentRecordTypeRelation typeRelation) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource()).withTableName(
                OtherDocumentRecordTypeRelationDaoImpl.TABLE_NAME).usingColumns(
                OtherDocumentRecordTypeRelationField.recordId.getColumnName(),
                OtherDocumentRecordTypeRelationField.type.getColumnName());

        insert.execute(getNamedParameters(typeRelation));
    }

    @Override
    public void save(Collection<OtherDocumentRecordTypeRelation> typeRelations) {
        for (OtherDocumentRecordTypeRelation typeRelation : typeRelations) {
            this.save(typeRelation);
        }
    }

    @Override
    public void delete(int recordId) {
        String idColumn = OtherDocumentRecordTypeRelationField.recordId.getColumnName();
        String query = String.format("DELETE FROM %s WHERE %s = :%s",
                OtherDocumentRecordTypeRelationDaoImpl.TABLE_NAME, idColumn, idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, recordId);

        getNamedParameterJdbcTemplate().update(query, args);
    }
}
