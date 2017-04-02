package com.svnavigatoru600.repository.records.impl.direct;

import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.repository.QueryUtil;
import com.svnavigatoru600.repository.records.impl.DocumentRecordFieldEnum;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("documentRecordDao")
@Transactional
public class DocumentRecordDaoImpl extends NamedParameterJdbcDaoSupport {

    /**
     * Database table which provides a persistence of {@link AbstractDocumentRecord AbstractDocumentRecords}.
     */
    static final String TABLE_NAME = "document_records";

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public DocumentRecordDaoImpl(final DataSource dataSource) {
        setDataSource(dataSource);
    }

    /**
     * Maps properties of the given {@link AbstractDocumentRecord} to names of the corresponding database column.
     */
    private Map<String, Object> getNamedParameters(final AbstractDocumentRecord record) {
        final Map<String, Object> parameters = Maps.newHashMap();
        parameters.put(DocumentRecordFieldEnum.ID.getColumnName(), record.getId());
        parameters.put(DocumentRecordFieldEnum.FILE_NAME.getColumnName(), record.getFileName());
        parameters.put(DocumentRecordFieldEnum.FILE.getColumnName(), record.getFile());
        return parameters;
    }

    public void update(final AbstractDocumentRecord record, final DataSource dataSource) {
        if (record.getFile() == null) {
            return;
        }

        final String idColumn = DocumentRecordFieldEnum.ID.getColumnName();
        final String fileNameColumn = DocumentRecordFieldEnum.FILE_NAME.getColumnName();
        final String fileColumn = DocumentRecordFieldEnum.FILE.getColumnName();
        final String query = String.format("UPDATE %s SET %s = :%s, %s = :%s WHERE %s = :%s",
                DocumentRecordDaoImpl.TABLE_NAME, fileNameColumn, fileNameColumn, fileColumn, fileColumn, idColumn,
                idColumn);

        // this.getSimpleJdbcTemplate() cannot be used here since the dataSource
        // is not set (i.e. equals null).
        (new NamedParameterJdbcTemplate(dataSource)).update(query, getNamedParameters(record));
    }

    public int save(final AbstractDocumentRecord record, final DataSource dataSource) {
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource).withTableName(DocumentRecordDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(DocumentRecordFieldEnum.ID.getColumnName())
                .usingColumns(DocumentRecordFieldEnum.FILE_NAME.getColumnName(), DocumentRecordFieldEnum.FILE.getColumnName());

        return insert.executeAndReturnKey(getNamedParameters(record)).intValue();
    }

    public void delete(final AbstractDocumentRecord record, final DataSource dataSource) {
        final String idColumn = DocumentRecordFieldEnum.ID.getColumnName();
        final String query = QueryUtil.deleteQuery(DocumentRecordDaoImpl.TABLE_NAME, idColumn, idColumn);

        final Map<String, Integer> args = Collections.singletonMap(idColumn, record.getId());

        // See the update function.
        (new NamedParameterJdbcTemplate(dataSource)).update(query, args);
    }
}
