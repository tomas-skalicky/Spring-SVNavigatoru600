package com.svnavigatoru600.repository.records.impl.direct;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.records.impl.DocumentRecordField;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("documentRecordDao")
public class DocumentRecordDaoImpl extends NamedParameterJdbcDaoSupport {

    /**
     * Database table which provides a persistence of {@link AbstractDocumentRecord AbstractDocumentRecords}.
     */
    private static final String TABLE_NAME = PersistedClass.AbstractDocumentRecord.getTableName();

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public DocumentRecordDaoImpl(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }

    /**
     * Maps properties of the given {@link AbstractDocumentRecord} to names of the corresponding database
     * column.
     */
    private Map<String, Object> getNamedParameters(AbstractDocumentRecord record) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(DocumentRecordField.id.getColumnName(), record.getId());
        parameters.put(DocumentRecordField.fileName.getColumnName(), record.getFileName());
        parameters.put(DocumentRecordField.file.getColumnName(), record.getFile());
        return parameters;
    }

    public void update(AbstractDocumentRecord record, DataSource dataSource) {
        if (record.getFile() == null) {
            return;
        }

        String idColumn = DocumentRecordField.id.getColumnName();
        String fileNameColumn = DocumentRecordField.fileName.getColumnName();
        String fileColumn = DocumentRecordField.file.getColumnName();
        String query = String.format("UPDATE %s SET %s = :%s, %s = :%s WHERE %s = :%s",
                DocumentRecordDaoImpl.TABLE_NAME, fileNameColumn, fileNameColumn, fileColumn, fileColumn,
                idColumn, idColumn);

        // this.getSimpleJdbcTemplate() cannot be used here since the dataSource
        // is not set (i.e. equals null).
        (new NamedParameterJdbcTemplate(dataSource)).update(query, getNamedParameters(record));
    }

    public int save(AbstractDocumentRecord record, DataSource dataSource) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource)
                .withTableName(DocumentRecordDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(DocumentRecordField.id.getColumnName())
                .usingColumns(DocumentRecordField.fileName.getColumnName(),
                        DocumentRecordField.file.getColumnName());

        return insert.executeAndReturnKey(getNamedParameters(record)).intValue();
    }

    public void delete(AbstractDocumentRecord record, DataSource dataSource) {
        String idColumn = DocumentRecordField.id.getColumnName();
        String query = String.format("DELETE FROM %s WHERE %s = :%s", DocumentRecordDaoImpl.TABLE_NAME,
                idColumn, idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, record.getId());

        // See the update function.
        (new NamedParameterJdbcTemplate(dataSource)).update(query, args);
    }
}
