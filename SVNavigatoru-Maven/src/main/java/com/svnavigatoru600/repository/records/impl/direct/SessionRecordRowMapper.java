package com.svnavigatoru600.repository.records.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.repository.records.impl.DocumentRecordFieldEnum;
import com.svnavigatoru600.repository.records.impl.SessionRecordFieldEnum;

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
        record.setId(rs.getInt(SessionRecordFieldEnum.ID.getColumnName()));
        record.setFileName(rs.getString(DocumentRecordFieldEnum.FILE_NAME.getColumnName()));

        if (isLoadFile()) {
            record.setFile(rs.getBlob(DocumentRecordFieldEnum.FILE.getColumnName()));
        } else {
            record.setFile(null);
        }

        record.setType(rs.getString(SessionRecordFieldEnum.TYPE.getColumnName()));
        record.setSessionDate(new Date(rs.getTimestamp(SessionRecordFieldEnum.SESSION_DATE.getColumnName()).getTime()));
        record.setDiscussedTopics(rs.getString(SessionRecordFieldEnum.DISCUSSED_TOPICS.getColumnName()));
        return record;
    }
}
