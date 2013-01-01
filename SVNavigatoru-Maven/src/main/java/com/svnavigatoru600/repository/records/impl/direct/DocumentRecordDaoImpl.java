package com.svnavigatoru600.repository.records.impl.direct;

import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.records.impl.DocumentRecordField;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class DocumentRecordDaoImpl extends SimpleJdbcDaoSupport {

    /**
     * Database table which provides a persistence of {@link AbstractDocumentRecord AbstractDocumentRecords}.
     */
    private static final String TABLE_NAME = PersistedClass.AbstractDocumentRecord.getTableName();

    public void update(AbstractDocumentRecord record, DataSource dataSource) {
        Blob file = record.getFile();
        if (file == null) {
            return;
        }

        String query = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
                DocumentRecordDaoImpl.TABLE_NAME, DocumentRecordField.fileName.getColumnName(),
                DocumentRecordField.file.getColumnName(), DocumentRecordField.id.getColumnName());

        // this.getSimpleJdbcTemplate() cannot be used here since the dataSource
        // is not set (i.e. equals null).
        (new SimpleJdbcTemplate(dataSource)).update(query, record.getFileName(), file, record.getId());
    }

    /**
     * Used during the save of the given <code>record</code>.
     */
    private Map<String, Object> getNamedParameters(AbstractDocumentRecord record) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(DocumentRecordField.id.getColumnName(), record.getId());
        parameters.put(DocumentRecordField.fileName.getColumnName(), record.getFileName());
        parameters.put(DocumentRecordField.file.getColumnName(), record.getFile());
        return parameters;
    }

    public int save(AbstractDocumentRecord record, DataSource dataSource) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource)
                .withTableName(DocumentRecordDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(DocumentRecordField.id.getColumnName())
                .usingColumns(DocumentRecordField.fileName.getColumnName(),
                        DocumentRecordField.file.getColumnName());

        return insert.executeAndReturnKey(this.getNamedParameters(record)).intValue();
    }

    public void delete(AbstractDocumentRecord record, DataSource dataSource) {
        String query = String.format("DELETE FROM %s WHERE %s = ?", DocumentRecordDaoImpl.TABLE_NAME,
                DocumentRecordField.id.getColumnName());

        // See the update function.
        (new SimpleJdbcTemplate(dataSource)).update(query, record.getId());
    }
}
