package com.svnavigatoru600.repository.records.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.repository.records.impl.DocumentRecordField;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordField;

/**
 * For more information, see {@link com.svnavigatoru600.repository.users.impl.direct.UserRowMapper UserRowMapper}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class OtherDocumentRecordRowMapper extends AbstractDocumentRecordRowMapper
        implements RowMapper<OtherDocumentRecord> {

    public OtherDocumentRecordRowMapper() {
        super();
    }

    /**
     * @param loadFile
     *            Flag whether the Blob file will be set up.
     */
    public OtherDocumentRecordRowMapper(final boolean loadFile) {
        super(loadFile);
    }

    @Override
    public OtherDocumentRecord mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final OtherDocumentRecord record = new OtherDocumentRecord();
        record.setId(rs.getInt(OtherDocumentRecordField.id.getColumnName()));
        record.setFileName(rs.getString(DocumentRecordField.fileName.getColumnName()));

        if (isLoadFile()) {
            record.setFile(rs.getBlob(DocumentRecordField.file.getColumnName()));
        } else {
            record.setFile(null);
        }

        record.setName(rs.getString(OtherDocumentRecordField.name.getColumnName()));
        record.setDescription(rs.getString(OtherDocumentRecordField.description.getColumnName()));
        record.setCreationTime(
                new Date(rs.getTimestamp(OtherDocumentRecordField.creationTime.getColumnName()).getTime()));
        record.setLastSaveTime(
                new Date(rs.getTimestamp(OtherDocumentRecordField.lastSaveTime.getColumnName()).getTime()));
        return record;
    }
}
