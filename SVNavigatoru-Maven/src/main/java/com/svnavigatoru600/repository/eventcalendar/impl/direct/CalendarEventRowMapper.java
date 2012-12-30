package com.svnavigatoru600.repository.eventcalendar.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.repository.eventcalendar.impl.CalendarEventField;

/**
 * For more information, see {@link com.svnavigatoru600.repository.users.impl.direct.UserRowMapper
 * UserRowMapper}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class CalendarEventRowMapper implements RowMapper<CalendarEvent> {

    @Override
    public CalendarEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
        CalendarEvent event = new CalendarEvent();
        event.setId(rs.getInt(CalendarEventField.id.getColumnName()));
        event.setName(rs.getString(CalendarEventField.name.getColumnName()));
        event.setDate(new Date(rs.getTimestamp(CalendarEventField.date.getColumnName()).getTime()));
        event.setDescription(rs.getString(CalendarEventField.description.getColumnName()));
        event.setPriority(rs.getString(CalendarEventField.priority.getColumnName()));
        return event;
    }
}
