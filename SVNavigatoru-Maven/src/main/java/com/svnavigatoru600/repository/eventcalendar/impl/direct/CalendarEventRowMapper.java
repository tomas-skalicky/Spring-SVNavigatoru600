package com.svnavigatoru600.repository.eventcalendar.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.repository.eventcalendar.impl.CalendarEventFieldEnum;

/**
 * For more information, see {@link com.svnavigatoru600.repository.users.impl.direct.UserRowMapper UserRowMapper}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class CalendarEventRowMapper implements RowMapper<CalendarEvent> {

    @Override
    public CalendarEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
        CalendarEvent event = new CalendarEvent();
        event.setId(rs.getInt(CalendarEventFieldEnum.ID.getColumnName()));
        event.setName(rs.getString(CalendarEventFieldEnum.NAME.getColumnName()));
        event.setDate(new Date(rs.getTimestamp(CalendarEventFieldEnum.DATE.getColumnName()).getTime()));
        event.setDescription(rs.getString(CalendarEventFieldEnum.DESCRIPTION.getColumnName()));
        event.setPriority(rs.getString(CalendarEventFieldEnum.PRIORITY.getColumnName()));
        return event;
    }
}
