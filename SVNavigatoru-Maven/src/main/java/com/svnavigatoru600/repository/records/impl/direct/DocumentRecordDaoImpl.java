package com.svnavigatoru600.repository.records.impl.direct;

import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.svnavigatoru600.domain.records.DocumentRecord;

public class DocumentRecordDaoImpl extends SimpleJdbcDaoSupport {

    static final String TABLE_NAME = "document_records";

    public void update(DocumentRecord record, DataSource dataSource) {
        Blob file = record.getFile();
        if (file == null) {
            return;
        }

        String query = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
                DocumentRecordDaoImpl.TABLE_NAME, DocumentRecordRowMapper.getColumn("fileName"),
                DocumentRecordRowMapper.getColumn("file"), DocumentRecordRowMapper.getColumn("id"));

        // this.getSimpleJdbcTemplate() cannot be used here since the dataSource
        // is not set (i.e. equals null).
        (new SimpleJdbcTemplate(dataSource)).update(query, record.getFileName(), file, record.getId());
    }

    /**
     * Used during the save of the given <code>record</code>.
     */
    private Map<String, Object> getNamedParameters(DocumentRecord record) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(DocumentRecordRowMapper.getColumn("id"), record.getId());
        parameters.put(DocumentRecordRowMapper.getColumn("fileName"), record.getFileName());
        parameters.put(DocumentRecordRowMapper.getColumn("file"), record.getFile());
        return parameters;
    }

    public int save(DocumentRecord record, DataSource dataSource) {
        String idColumn = DocumentRecordRowMapper.getColumn("id");

        SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource)
                .withTableName(DocumentRecordDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(idColumn)
                .usingColumns(DocumentRecordRowMapper.getColumn("fileName"),
                        DocumentRecordRowMapper.getColumn("file"));

        return insert.executeAndReturnKey(this.getNamedParameters(record)).intValue();
    }

    public void delete(DocumentRecord record, DataSource dataSource) {
        String query = String.format("DELETE FROM %s WHERE %s = ?", DocumentRecordDaoImpl.TABLE_NAME,
                DocumentRecordRowMapper.getColumn("id"));

        // See the update function.
        (new SimpleJdbcTemplate(dataSource)).update(query, record.getId());
    }
}
