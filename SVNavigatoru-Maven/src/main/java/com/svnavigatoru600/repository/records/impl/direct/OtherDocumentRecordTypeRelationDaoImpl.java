package com.svnavigatoru600.repository.records.impl.direct;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelationId;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.records.OtherDocumentRecordTypeRelationDao;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordTypeRelationField;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class OtherDocumentRecordTypeRelationDaoImpl extends SimpleJdbcDaoSupport implements
        OtherDocumentRecordTypeRelationDao {

    private static final String TABLE_NAME = PersistedClass.OtherDocumentRecordTypeRelation.getTableName();

    @Override
    public List<OtherDocumentRecordTypeRelation> findAll(int recordId) {
        String query = String.format("SELECT * FROM %s r WHERE r.%s = ?",
                OtherDocumentRecordTypeRelationDaoImpl.TABLE_NAME,
                OtherDocumentRecordTypeRelationField.recordId.getColumnName());
        return this.getSimpleJdbcTemplate().query(query, new OtherDocumentRecordTypeRelationRowMapper(),
                recordId);
    }

    /**
     * Used during the save of the given <code>relation</code>.
     */
    private Map<String, Object> getNamedParameters(OtherDocumentRecordTypeRelation relation) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        OtherDocumentRecordTypeRelationId id = relation.getId();
        parameters.put(OtherDocumentRecordTypeRelationField.recordId.getColumnName(), id.getRecordId());
        parameters.put(OtherDocumentRecordTypeRelationField.type.getColumnName(), id.getType());
        return parameters;
    }

    /**
     * Saves the given <code>relation</code> to the repository.
     */
    public void save(OtherDocumentRecordTypeRelation relation) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource()).withTableName(
                OtherDocumentRecordTypeRelationDaoImpl.TABLE_NAME).usingColumns(
                OtherDocumentRecordTypeRelationField.recordId.getColumnName(),
                OtherDocumentRecordTypeRelationField.type.getColumnName());
        insert.execute(this.getNamedParameters(relation));
    }

    @Override
    public void save(Collection<OtherDocumentRecordTypeRelation> types) {
        for (OtherDocumentRecordTypeRelation type : types) {
            this.save(type);
        }
    }

    @Override
    public void delete(int recordId) {
        String query = String.format("DELETE FROM %s WHERE %s = ?",
                OtherDocumentRecordTypeRelationDaoImpl.TABLE_NAME,
                OtherDocumentRecordTypeRelationField.recordId.getColumnName());
        this.getSimpleJdbcTemplate().update(query, recordId);
    }
}
