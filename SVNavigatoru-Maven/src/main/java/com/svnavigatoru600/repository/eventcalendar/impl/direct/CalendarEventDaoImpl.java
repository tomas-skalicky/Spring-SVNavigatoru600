package com.svnavigatoru600.repository.eventcalendar.impl.direct;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.repository.eventcalendar.impl.CalendarEventField;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.service.util.OrderType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("calendarEventDao")
public class CalendarEventDaoImpl extends NamedParameterJdbcDaoSupport implements CalendarEventDao {

    /**
     * Database table which provides a persistence of {@link CalendarEvent CalendarEvents}.
     */
    private static final String TABLE_NAME = PersistedClass.CalendarEvent.getTableName();

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public CalendarEventDaoImpl(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }

    @Override
    public CalendarEvent findById(int eventId) {
        String idColumn = CalendarEventField.id.getColumnName();
        String query = String.format("SELECT * FROM %s e WHERE e.%s = :%s", CalendarEventDaoImpl.TABLE_NAME, idColumn,
                idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, eventId);

        return getNamedParameterJdbcTemplate().queryForObject(query, args, new CalendarEventRowMapper());
    }

    @Override
    public List<CalendarEvent> findAllFutureEventsOrdered(Date earliestDate, OrderType sortDirection) {
        String dateColumn = CalendarEventField.date.getColumnName();
        String query = String.format("SELECT * FROM %s e WHERE e.%s >= :%s ORDER BY e.%s %s",
                CalendarEventDaoImpl.TABLE_NAME, dateColumn, dateColumn, dateColumn, sortDirection.getDatabaseCode());

        Map<String, Date> args = Collections.singletonMap(dateColumn, earliestDate);

        return getNamedParameterJdbcTemplate().query(query, args, new CalendarEventRowMapper());
    }

    /**
     * Maps properties of the given {@link CalendarEvent} to names of the corresponding database columns.
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
    public void update(CalendarEvent event) {
        String idColumn = CalendarEventField.id.getColumnName();
        String nameColumn = CalendarEventField.name.getColumnName();
        String dateColumn = CalendarEventField.date.getColumnName();
        String descriptionColumn = CalendarEventField.description.getColumnName();
        String priorityColumn = CalendarEventField.priority.getColumnName();
        String query = String.format("UPDATE %s SET %s = :%s, %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                CalendarEventDaoImpl.TABLE_NAME, nameColumn, nameColumn, dateColumn, dateColumn, descriptionColumn,
                descriptionColumn, priorityColumn, priorityColumn, idColumn, idColumn);

        getNamedParameterJdbcTemplate().update(query, getNamedParameters(event));
    }

    @Override
    public int save(CalendarEvent event) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource()).withTableName(CalendarEventDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(CalendarEventField.id.getColumnName())
                .usingColumns(CalendarEventField.name.getColumnName(), CalendarEventField.date.getColumnName(),
                        CalendarEventField.description.getColumnName(), CalendarEventField.priority.getColumnName());

        return insert.executeAndReturnKey(getNamedParameters(event)).intValue();
    }

    @Override
    public void delete(CalendarEvent event) {
        String idColumn = CalendarEventField.id.getColumnName();
        String query = String.format("DELETE FROM %s WHERE %s = :%s", CalendarEventDaoImpl.TABLE_NAME, idColumn,
                idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, event.getId());

        getNamedParameterJdbcTemplate().update(query, args);
    }
}
