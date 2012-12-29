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
import com.svnavigatoru600.repository.eventcalendar.impl.FindFutureEventsOrderedArguments;

/**
 * The "@Repository" annotation cannot be used. Otherwise, the XML descriptor
 * model-beans/event-calendar/CalendarEvent-direct.xml is not used and the dataSource attribute is not set up.
 * If we want to use the annotation, we cannot use the XML descriptor at all for a particular bean.
 */
public class CalendarEventDaoImpl extends NamedParameterJdbcDaoSupport implements CalendarEventDao {

    private static final String TABLE_NAME = "calendar_events";

    @Override
    public CalendarEvent findById(int eventId) {
        String idProperty = CalendarEventField.id.getColumnName();
        String query = String.format("SELECT * FROM %s e WHERE e.%s = :%s", CalendarEventDaoImpl.TABLE_NAME,
                idProperty, idProperty);

        Map<String, Integer> args = Collections.singletonMap(idProperty, eventId);

        return this.getNamedParameterJdbcTemplate().queryForObject(query, args, new CalendarEventRowMapper());
    }

    @Override
    public List<CalendarEvent> findFutureEventsOrdered(FindFutureEventsOrderedArguments arguments) {
        final String dateColumn = arguments.getSortField().getColumnName();
        String query = String.format("SELECT * FROM %s e WHERE e.%s >= :%s ORDER BY e.%s %s",
                CalendarEventDaoImpl.TABLE_NAME, dateColumn, dateColumn, dateColumn, arguments
                        .getSortDirection().getDatabaseCode());

        Map<String, Date> args = Collections.singletonMap(dateColumn, arguments.getEarliestDate());

        return this.getNamedParameterJdbcTemplate().query(query, args, new CalendarEventRowMapper());
    }

    @Override
    public void update(CalendarEvent event) {
        String nameProperty = CalendarEventField.name.getColumnName();
        String dateProperty = CalendarEventField.date.getColumnName();
        String descriptionProperty = CalendarEventField.description.getColumnName();
        String priorityProperty = CalendarEventField.priority.getColumnName();
        String idProperty = CalendarEventField.id.getColumnName();
        String query = String.format(
                "UPDATE %s SET %s = :name, %s = :date, %s = :description, %s = :priority WHERE %s = :id",
                CalendarEventDaoImpl.TABLE_NAME, nameProperty, dateProperty, descriptionProperty,
                priorityProperty, idProperty);

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
        String idColumn = CalendarEventField.id.getColumnName();

        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource())
                .withTableName(CalendarEventDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(idColumn)
                .usingColumns(CalendarEventField.name.getColumnName(),
                        CalendarEventField.date.getColumnName(),
                        CalendarEventField.description.getColumnName(),
                        CalendarEventField.priority.getColumnName());

        return insert.executeAndReturnKey(this.getNamedParameters(event)).intValue();
    }

    @Override
    public void delete(CalendarEvent event) {
        String idProperty = CalendarEventField.id.getColumnName();
        String query = String.format("DELETE FROM %s WHERE %s = :%s", CalendarEventDaoImpl.TABLE_NAME,
                idProperty, idProperty);

        Map<String, Integer> args = Collections.singletonMap(idProperty, event.getId());

        this.getNamedParameterJdbcTemplate().update(query, args);
    }
}
