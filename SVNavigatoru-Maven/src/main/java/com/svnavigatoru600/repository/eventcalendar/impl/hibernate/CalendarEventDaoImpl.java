package com.svnavigatoru600.repository.eventcalendar.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.repository.eventcalendar.impl.CalendarEventField;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.service.util.OrderType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class CalendarEventDaoImpl extends HibernateDaoSupport implements CalendarEventDao {

    @Override
    public CalendarEvent findById(int eventId) {
        return getHibernateTemplate().load(CalendarEvent.class, eventId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CalendarEvent> findAllFutureEventsOrdered(Date earliestDate, OrderType sortDirection) {
        String query = String.format("FROM %s e WHERE e.%s >= ? ORDER BY e.%s %s",
                PersistedClass.CalendarEvent.name(), CalendarEventField.date.name(),
                CalendarEventField.date.name(), sortDirection.getDatabaseCode());
        return (List<CalendarEvent>) getHibernateTemplate().find(query, earliestDate);
    }

    @Override
    public void update(CalendarEvent event) {
        getHibernateTemplate().update(event);
    }

    @Override
    public int save(CalendarEvent event) {
        return (Integer) getHibernateTemplate().save(event);
    }

    @Override
    public void delete(CalendarEvent event) {
        getHibernateTemplate().delete(event);
    }
}
