package com.svnavigatoru600.repository.records.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.repository.records.impl.DocumentRecordField;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordField;

/**
 * For more information, see {@link com.svnavigatoru600.repository.users.impl.direct.UserRowMapper
 * UserRowMapper}.
 * 
 * @author Tomas Skalicky
 */
public class OtherDocumentRecordRowMapper extends DocumentRecordRowMapper implements
        RowMapper<OtherDocumentRecord> {

    /**
     * Constructor.
     */
    public OtherDocumentRecordRowMapper() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param loadFile
     *            Indicator whether the Blob file will be set up.
     */
    public OtherDocumentRecordRowMapper(boolean loadFile) {
        super(loadFile);
    }

    @Override
    public OtherDocumentRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        OtherDocumentRecord record = new OtherDocumentRecord();
        record.setId(rs.getInt(OtherDocumentRecordField.id.getColumnName()));
        record.setFileName(rs.getString(DocumentRecordField.fileName.getColumnName()));

        if (this.isLoadFile()) {
            record.setFile(rs.getBlob(DocumentRecordField.file.getColumnName()));
        } else {
            record.setFile(null);
        }

        record.setName(rs.getString(OtherDocumentRecordField.name.getColumnName()));
        record.setDescription(rs.getString(OtherDocumentRecordField.description.getColumnName()));
        record.setCreationTime(new Date(rs
                .getTimestamp(OtherDocumentRecordField.creationTime.getColumnName()).getTime()));
        record.setLastSaveTime(new Date(rs
                .getTimestamp(OtherDocumentRecordField.lastSaveTime.getColumnName()).getTime()));
        return record;
    }
}
