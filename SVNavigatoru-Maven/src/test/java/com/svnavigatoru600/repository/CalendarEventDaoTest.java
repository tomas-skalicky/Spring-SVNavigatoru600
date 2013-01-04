package com.svnavigatoru600.repository;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.dao.EmptyResultDataAccessException;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.eventcalendar.PriorityType;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.OrderType;
import com.svnavigatoru600.test.category.PersistenceTests;

/**
 * Tests methods of the {@link CalendarEventDao} interface.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public class CalendarEventDaoTest extends AbstractRepositoryTest {

    /**
     * Name of the edited test event.
     */
    private static final String EDITED_EVENT_NAME = "name 2";
    /**
     * Date of the edited test event. It equals now.
     */
    private static final Date EDITED_EVENT_DATE = new Date();
    /**
     * Description of the edited test event.
     */
    private static final String EDITED_EVENT_DESCRIPTION = "description 2";
    /**
     * Priority of the edited test event.
     */
    private static final PriorityType EDITED_EVENT_PRIORITY = PriorityType.HIGH;

    @Test
    public void testCreateRetrieve() throws Exception {
        CalendarEventDao eventDao = TEST_UTILS.getEventDao();

        // INSERT
        int eventId = TEST_UTILS.createDefaultTestEvent();

        // SELECT ONE
        CalendarEvent event = eventDao.findById(eventId);
        Assert.assertTrue(event.getId() >= 1);
        Assert.assertEquals(eventId, event.getId());
        Assert.assertEquals(RepositoryTestUtils.EVENT_DEFAULT_NAME, event.getName());
        Assert.assertEquals(RepositoryTestUtils.EVENT_DEFAULT_DATE, event.getDate());
        Assert.assertEquals(RepositoryTestUtils.EVENT_DEFAULT_DESCRIPTION, event.getDescription());
        Assert.assertEquals(RepositoryTestUtils.EVENT_DEFAULT_PRIORITY.name(), event.getPriority());
    }

    @Test
    public void testUpdate() throws Exception {
        CalendarEventDao eventDao = TEST_UTILS.getEventDao();

        // INSERT & SELECT ONE
        int eventId = TEST_UTILS.createDefaultTestEvent();
        CalendarEvent event = eventDao.findById(eventId);

        // UPDATE
        event.setName(EDITED_EVENT_NAME);
        event.setDate(EDITED_EVENT_DATE);
        event.setDescription(EDITED_EVENT_DESCRIPTION);
        event.setPriority(EDITED_EVENT_PRIORITY);
        eventDao.update(event);

        // SELECT ONE
        event = eventDao.findById(event.getId());
        Assert.assertTrue(event.getId() >= 1);
        Assert.assertEquals(eventId, event.getId());
        Assert.assertEquals(EDITED_EVENT_NAME, event.getName());
        Assert.assertEquals(EDITED_EVENT_DATE, event.getDate());
        Assert.assertEquals(EDITED_EVENT_DESCRIPTION, event.getDescription());
        Assert.assertEquals(EDITED_EVENT_PRIORITY.name(), event.getPriority());
    }

    @Test
    public void testDelete() throws Exception {
        CalendarEventDao eventDao = TEST_UTILS.getEventDao();

        // INSERT & SELECT ONE
        int eventId = TEST_UTILS.createDefaultTestEvent();
        CalendarEvent event = eventDao.findById(eventId);

        // DELETE
        eventDao.delete(event);

        // SELECT ONE
        try {
            event = eventDao.findById(event.getId());
            Assert.fail("The event has been found");
        } catch (EmptyResultDataAccessException ex) {
            // OK since the event cannot have been found.
            ;
        }
    }

    @Test
    public void testFindAllOrdered() throws Exception {
        CalendarEventDao eventDao = TEST_UTILS.getEventDao();
        Date now = new Date();

        // THREE INSERTS
        int firstEventId = TEST_UTILS.createDefaultTestEvent(DateUtils.getDay(now, 2));
        int secondEventId = TEST_UTILS.createDefaultTestEvent(DateUtils.getDay(now, 1));
        @SuppressWarnings("unused")
        int thirdNewsId = TEST_UTILS.createDefaultTestEvent(DateUtils.getDay(now, -1));

        // SELECT ALL
        List<CalendarEvent> foundEvents = eventDao.findAllFutureEventsOrdered(now, OrderType.ASCENDING);
        int expectedFoundEventCount = 2;
        Assert.assertEquals(expectedFoundEventCount, foundEvents.size());
        Assert.assertEquals(secondEventId, foundEvents.get(0).getId());
        Assert.assertEquals(firstEventId, foundEvents.get(1).getId());
    }
}
