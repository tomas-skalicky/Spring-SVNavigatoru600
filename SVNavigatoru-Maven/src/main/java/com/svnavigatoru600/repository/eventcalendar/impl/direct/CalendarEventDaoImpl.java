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
import org.springframework.transaction.annotation.Transactional;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.repository.QueryUtil;
import com.svnavigatoru600.repository.eventcalendar.impl.CalendarEventFieldEnum;
import com.svnavigatoru600.service.util.OrderTypeEnum;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("calendarEventDao")
@Transactional
public class CalendarEventDaoImpl extends NamedParameterJdbcDaoSupport implements CalendarEventDao {

    /**
     * Database table which provides a persistence of {@link CalendarEvent CalendarEvents}.
     */
    private static final String TABLE_NAME = "calendar_events";

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public CalendarEventDaoImpl(final DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public CalendarEvent findById(final int eventId) {
        final String idColumn = CalendarEventFieldEnum.ID.getColumnName();
        final String query = QueryUtil.selectQuery(CalendarEventDaoImpl.TABLE_NAME, idColumn, idColumn);

        final Map<String, Integer> args = Collections.singletonMap(idColumn, eventId);

        return getNamedParameterJdbcTemplate().queryForObject(query, args, new CalendarEventRowMapper());
    }

    @Override
    public List<CalendarEvent> findAllFutureEventsOrdered(final Date earliestDate, final OrderTypeEnum sortDirection) {
        final String dateColumn = CalendarEventFieldEnum.DATE.getColumnName();
        final String query = String.format("SELECT * FROM %s e WHERE e.%s >= :%s ORDER BY e.%s %s",
                CalendarEventDaoImpl.TABLE_NAME, dateColumn, dateColumn, dateColumn, sortDirection.getDatabaseCode());

        final Map<String, Date> args = Collections.singletonMap(dateColumn, earliestDate);

        return getNamedParameterJdbcTemplate().query(query, args, new CalendarEventRowMapper());
    }

    /**
     * Maps properties of the given {@link CalendarEvent} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(final CalendarEvent event) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(CalendarEventFieldEnum.ID.getColumnName(), event.getId());
        parameters.put(CalendarEventFieldEnum.NAME.getColumnName(), event.getName());
        parameters.put(CalendarEventFieldEnum.DATE.getColumnName(), event.getDate());
        parameters.put(CalendarEventFieldEnum.DESCRIPTION.getColumnName(), event.getDescription());
        parameters.put(CalendarEventFieldEnum.PRIORITY.getColumnName(), event.getPriority());
        return parameters;
    }

    @Override
    public void update(final CalendarEvent event) {
        final String idColumn = CalendarEventFieldEnum.ID.getColumnName();
        final String nameColumn = CalendarEventFieldEnum.NAME.getColumnName();
        final String dateColumn = CalendarEventFieldEnum.DATE.getColumnName();
        final String descriptionColumn = CalendarEventFieldEnum.DESCRIPTION.getColumnName();
        final String priorityColumn = CalendarEventFieldEnum.PRIORITY.getColumnName();
        final String query = String.format("UPDATE %s SET %s = :%s, %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                CalendarEventDaoImpl.TABLE_NAME, nameColumn, nameColumn, dateColumn, dateColumn, descriptionColumn,
                descriptionColumn, priorityColumn, priorityColumn, idColumn, idColumn);

        getNamedParameterJdbcTemplate().update(query, getNamedParameters(event));
    }

    @Override
    public int save(final CalendarEvent event) {
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource())
                .withTableName(CalendarEventDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(CalendarEventFieldEnum.ID.getColumnName())
                .usingColumns(CalendarEventFieldEnum.NAME.getColumnName(), CalendarEventFieldEnum.DATE.getColumnName(),
                        CalendarEventFieldEnum.DESCRIPTION.getColumnName(), CalendarEventFieldEnum.PRIORITY.getColumnName());

        return insert.executeAndReturnKey(getNamedParameters(event)).intValue();
    }

    @Override
    public void delete(final CalendarEvent event) {
        final String idColumn = CalendarEventFieldEnum.ID.getColumnName();
        final String query = QueryUtil.deleteQuery(CalendarEventDaoImpl.TABLE_NAME, idColumn, idColumn);

        final Map<String, Integer> args = Collections.singletonMap(idColumn, event.getId());

        getNamedParameterJdbcTemplate().update(query, args);
    }
}
