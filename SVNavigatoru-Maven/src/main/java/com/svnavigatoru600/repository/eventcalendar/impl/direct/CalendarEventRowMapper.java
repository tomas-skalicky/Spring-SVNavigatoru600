package com.svnavigatoru600.repository.eventcalendar.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.repository.users.impl.direct.UserRowMapper;

/**
 * For more information, see {@link UserRowMapper}.
 * 
 * @author Tomas Skalicky
 */
public class CalendarEventRowMapper implements RowMapper<CalendarEvent> {

    private static final Map<String, String> PROPERTY_COLUMN_MAP;

    static {
        PROPERTY_COLUMN_MAP = new HashMap<String, String>();
        PROPERTY_COLUMN_MAP.put("id", "id");
        PROPERTY_COLUMN_MAP.put("name", "name");
        PROPERTY_COLUMN_MAP.put("date", "date");
        PROPERTY_COLUMN_MAP.put("description", "description");
        PROPERTY_COLUMN_MAP.put("priority", "priority");
    }

    public static String getColumn(String propertyName) {
        return CalendarEventRowMapper.PROPERTY_COLUMN_MAP.get(propertyName);
    }

    public CalendarEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
        CalendarEvent event = new CalendarEvent();
        event.setId(rs.getInt(CalendarEventRowMapper.getColumn("id")));
        event.setName(rs.getString(CalendarEventRowMapper.getColumn("name")));
        event.setDate(new Date(rs.getTimestamp(CalendarEventRowMapper.getColumn("date")).getTime()));
        event.setDescription(rs.getString(CalendarEventRowMapper.getColumn("description")));
        event.setPriority(rs.getString(CalendarEventRowMapper.getColumn("priority")));
        return event;
    }
}
