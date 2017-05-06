package com.svnavigatoru600.repository.records.impl.direct;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelationId;
import com.svnavigatoru600.repository.AbstractDaoImpl;
import com.svnavigatoru600.repository.QueryUtil;
import com.svnavigatoru600.repository.records.OtherDocumentRecordTypeRelationDao;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordTypeRelationFieldEnum;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("otherDocumentRecordTypeRelationDao")
@Transactional
public class OtherDocumentRecordTypeRelationDaoImpl extends AbstractDaoImpl
        implements OtherDocumentRecordTypeRelationDao {

    /**
     * Database table which provides a persistence of {@link OtherDocumentRecordTypeRelation
     * OtherDocumentRecordTypeRelations}.
     */
    static final String TABLE_NAME = "other_document_record_type_relations";

    @Override
    public List<OtherDocumentRecordTypeRelation> findByRecordId(final int recordId) {
        final String recordIdColumn = OtherDocumentRecordTypeRelationFieldEnum.RECORD_ID.getColumnName();
        final String query = QueryUtil.selectQuery(OtherDocumentRecordTypeRelationDaoImpl.TABLE_NAME, recordIdColumn,
                recordIdColumn);

        final Map<String, Integer> args = Collections.singletonMap(recordIdColumn, recordId);

        return getNamedParameterJdbcTemplate().query(query, args, new OtherDocumentRecordTypeRelationRowMapper());
    }

    /**
     * Maps properties of the given {@link OtherDocumentRecordTypeRelation typeRelation} to names of the corresponding
     * database column.
     */
    private Map<String, Object> getNamedParameters(final OtherDocumentRecordTypeRelation typeRelation) {
        final Map<String, Object> parameters = Maps.newHashMap();
        final OtherDocumentRecordTypeRelationId id = typeRelation.getId();
        parameters.put(OtherDocumentRecordTypeRelationFieldEnum.RECORD_ID.getColumnName(), id.getRecordId());
        parameters.put(OtherDocumentRecordTypeRelationFieldEnum.TYPE.getColumnName(), id.getType());
        return parameters;
    }

    /**
     * Saves the given {@link OtherDocumentRecordTypeRelation typeRelation} to the repository.
     */
    public void save(final OtherDocumentRecordTypeRelation typeRelation) {
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource())
                .withTableName(OtherDocumentRecordTypeRelationDaoImpl.TABLE_NAME)
                .usingColumns(OtherDocumentRecordTypeRelationFieldEnum.RECORD_ID.getColumnName(),
                        OtherDocumentRecordTypeRelationFieldEnum.TYPE.getColumnName());

        insert.execute(getNamedParameters(typeRelation));
    }

    @Override
    public void save(final Collection<OtherDocumentRecordTypeRelation> typeRelations) {
        for (final OtherDocumentRecordTypeRelation typeRelation : typeRelations) {
            this.save(typeRelation);
        }
    }

    @Override
    public void delete(final int recordId) {
        final String idColumn = OtherDocumentRecordTypeRelationFieldEnum.RECORD_ID.getColumnName();
        final String query = QueryUtil.deleteQuery(OtherDocumentRecordTypeRelationDaoImpl.TABLE_NAME, idColumn,
                idColumn);

        final Map<String, Integer> args = Collections.singletonMap(idColumn, recordId);

        getNamedParameterJdbcTemplate().update(query, args);
    }
}
