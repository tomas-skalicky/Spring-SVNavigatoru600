package com.svnavigatoru600.repository.eventcalendar.impl.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.repository.eventcalendar.impl.CalendarEventField;
import com.svnavigatoru600.repository.eventcalendar.impl.FindAllFutureEventsOrderedArguments;
import com.svnavigatoru600.repository.impl.PersistedClass;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class CalendarEventDaoImpl extends HibernateDaoSupport implements CalendarEventDao {

    @Override
    public CalendarEvent findById(int eventId) {
        return this.getHibernateTemplate().load(CalendarEvent.class, eventId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CalendarEvent> findAllFutureEventsOrdered(FindAllFutureEventsOrderedArguments arguments) {
        String query = String.format("FROM %s e WHERE e.%s >= ? ORDER BY e.%s %s",
                PersistedClass.CalendarEvent.name(), CalendarEventField.date.name(), arguments.getSortField()
                        .name(), arguments.getSortDirection().getDatabaseCode());
        return (List<CalendarEvent>) this.getHibernateTemplate().find(query, arguments.getEarliestDate());
    }

    @Override
    public void update(CalendarEvent event) {
        this.getHibernateTemplate().update(event);
    }

    @Override
    public int save(CalendarEvent event) {
        return (Integer) this.getHibernateTemplate().save(event);
    }

    @Override
    public void delete(CalendarEvent event) {
        this.getHibernateTemplate().delete(event);
    }
}
