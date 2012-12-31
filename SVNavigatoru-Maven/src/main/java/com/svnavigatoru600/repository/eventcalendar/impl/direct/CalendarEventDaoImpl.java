package com.svnavigatoru600.repository.eventcalendar.impl.direct;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.repository.eventcalendar.impl.CalendarEventField;
import com.svnavigatoru600.repository.eventcalendar.impl.FindAllFutureEventsOrderedArguments;
import com.svnavigatoru600.repository.impl.PersistedClass;

/**
 * The "@Repository" annotation cannot be used. Otherwise, the XML descriptor
 * model-beans/event-calendar/CalendarEvent-direct.xml is not used and the dataSource attribute is not set up.
 * If we want to use the annotation, we cannot use the XML descriptor at all for a particular bean.
 */
public class CalendarEventDaoImpl extends NamedParameterJdbcDaoSupport implements CalendarEventDao {

    private static final String TABLE_NAME = PersistedClass.CalendarEvent.getTableName();

    @Override
    public CalendarEvent findById(int eventId) {
        String idColumn = CalendarEventField.id.getColumnName();
        String query = String.format("SELECT * FROM %s e WHERE e.%s = :%s", CalendarEventDaoImpl.TABLE_NAME,
                idColumn, idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, eventId);

        return this.getNamedParameterJdbcTemplate().queryForObject(query, args, new CalendarEventRowMapper());
    }

    @Override
    public List<CalendarEvent> findAllFutureEventsOrdered(FindAllFutureEventsOrderedArguments arguments) {
        final String dateColumn = arguments.getSortField().getColumnName();
        String query = String.format("SELECT * FROM %s e WHERE e.%s >= :%s ORDER BY e.%s %s",
                CalendarEventDaoImpl.TABLE_NAME, dateColumn, dateColumn, dateColumn, arguments
                        .getSortDirection().getDatabaseCode());

        Map<String, Date> args = Collections.singletonMap(dateColumn, arguments.getEarliestDate());

        return this.getNamedParameterJdbcTemplate().query(query, args, new CalendarEventRowMapper());
    }

    @Override
    public void update(CalendarEvent event) {
        String query = String.format("UPDATE %s SET %s = :%s, %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                CalendarEventDaoImpl.TABLE_NAME, CalendarEventField.name.getColumnName(),
                CalendarEventField.name.name(), CalendarEventField.date.getColumnName(),
                CalendarEventField.date.name(), CalendarEventField.description.getColumnName(),
                CalendarEventField.description.name(), CalendarEventField.priority.getColumnName(),
                CalendarEventField.priority.name(), CalendarEventField.id.getColumnName(),
                CalendarEventField.id.name());

        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(event);
        this.getNamedParameterJdbcTemplate().update(query, namedParameters);
    }

    /**
     * Used during the save of the given <code>event</code>.
     */
    private Map<String, Object> getNamedParameters(CalendarEvent event) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(CalendarEventField.id.getColumnName(), event.getId());
        parameters.put(CalendarEventField.name.getColumnName(), event.getName());
        parameters.put(CalendarEventField.date.getColumnName(), event.getDate());
        parameters.put(CalendarEventField.description.getColumnName(), event.getDescription());
        parameters.put(CalendarEventField.priority.getColumnName(), event.getPriority());
        return parameters;
    }

    @Override
    public int save(CalendarEvent event) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource())
                .withTableName(CalendarEventDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(CalendarEventField.id.getColumnName())
                .usingColumns(CalendarEventField.name.getColumnName(),
                        CalendarEventField.date.getColumnName(),
                        CalendarEventField.description.getColumnName(),
                        CalendarEventField.priority.getColumnName());

        return insert.executeAndReturnKey(this.getNamedParameters(event)).intValue();
    }

    @Override
    public void delete(CalendarEvent event) {
        String idColumn = CalendarEventField.id.getColumnName();
        String query = String.format("DELETE FROM %s WHERE %s = :%s", CalendarEventDaoImpl.TABLE_NAME,
                idColumn, idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, event.getId());

        this.getNamedParameterJdbcTemplate().update(query, args);
    }
}
