package com.svnavigatoru600.repository.records.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.repository.records.impl.DocumentRecordFieldEnum;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordFieldEnum;

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
        record.setId(rs.getInt(OtherDocumentRecordFieldEnum.ID.getColumnName()));
        record.setFileName(rs.getString(DocumentRecordFieldEnum.FILE_NAME.getColumnName()));

        if (isLoadFile()) {
            record.setFile(rs.getBlob(DocumentRecordFieldEnum.FILE.getColumnName()));
        } else {
            record.setFile(null);
        }

        record.setName(rs.getString(OtherDocumentRecordFieldEnum.name.getColumnName()));
        record.setDescription(rs.getString(OtherDocumentRecordFieldEnum.DESCRIPTION.getColumnName()));
        record.setCreationTime(
                new Date(rs.getTimestamp(OtherDocumentRecordFieldEnum.CREATION_TIME.getColumnName()).getTime()));
        record.setLastSaveTime(
                new Date(rs.getTimestamp(OtherDocumentRecordFieldEnum.LAST_SAVE_TIME.getColumnName()).getTime()));
        return record;
    }
}
