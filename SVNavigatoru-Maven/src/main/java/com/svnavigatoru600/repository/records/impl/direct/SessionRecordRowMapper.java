package com.svnavigatoru600.repository.records.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.repository.records.impl.DocumentRecordField;
import com.svnavigatoru600.repository.records.impl.SessionRecordField;

/**
 * For more information, see {@link com.svnavigatoru600.repository.users.impl.direct.UserRowMapper UserRowMapper}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class SessionRecordRowMapper extends AbstractDocumentRecordRowMapper implements RowMapper<SessionRecord> {

    public SessionRecordRowMapper() {
        super();
    }

    public SessionRecordRowMapper(final boolean loadFile) {
        super(loadFile);
    }

    @Override
    public SessionRecord mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final SessionRecord record = new SessionRecord();
        record.setId(rs.getInt(SessionRecordField.id.getColumnName()));
        record.setFileName(rs.getString(DocumentRecordField.fileName.getColumnName()));

        if (isLoadFile()) {
            record.setFile(rs.getBlob(DocumentRecordField.file.getColumnName()));
        } else {
            record.setFile(null);
        }

        record.setType(rs.getString(SessionRecordField.type.getColumnName()));
        record.setSessionDate(new Date(rs.getTimestamp(SessionRecordField.sessionDate.getColumnName()).getTime()));
        record.setDiscussedTopics(rs.getString(SessionRecordField.discussedTopics.getColumnName()));
        return record;
    }
}
