package com.svnavigatoru600.repository;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.eventcalendar.PriorityType;
import com.svnavigatoru600.test.category.PersistenceTests;

/**
 * Tests methods of the {@link CalendarEventDao} interface.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public class CalendarEventTest extends AbstractRepositoryTest {

    /**
     * Default name of test event.
     */
    private static final String EVENT_DEFAULT_NAME = "name 1";
    /**
     * Default date of test event. It equals now.
     */
    private static final Date EVENT_DEFAULT_DATE = new Date();
    /**
     * Default description of test event.
     */
    private static final String EVENT_DEFAULT_DESCRIPTION = "description 1";
    /**
     * Default priority of test event.
     */
    private static final PriorityType EVENT_DEFAULT_PRIORITY = PriorityType.LOW;

    @Test
    public void testCreateRetrieve() throws Exception {
        CalendarEventDao eventDao = APPLICATION_CONTEXT.getBean(CalendarEventDao.class);

        // INSERT
        int eventId = this.createDefaultTestEvent(eventDao);

        // SELECT ONE
        CalendarEvent event = eventDao.findById(eventId);
        Assert.assertNotNull(event);
        Assert.assertTrue(event.getId() >= 1);
        Assert.assertEquals(eventId, event.getId());
        Assert.assertEquals(EVENT_DEFAULT_NAME, event.getName());
        Assert.assertEquals(EVENT_DEFAULT_DATE, event.getDate());
        Assert.assertEquals(EVENT_DEFAULT_DESCRIPTION, event.getDescription());
        Assert.assertEquals(EVENT_DEFAULT_PRIORITY.name(), event.getPriority());
    }

    @Test
    public void testUpdate() throws Exception {
        CalendarEventDao eventDao = APPLICATION_CONTEXT.getBean(CalendarEventDao.class);

        // INSERT & SELECT ONE
        int eventId = this.createDefaultTestEvent(eventDao);
        CalendarEvent event = eventDao.findById(eventId);

        // UPDATE
        news.setTitle(EDITED_NEWS_TITLE);
        news.setText(EDITED_NEWS_TEXT);
        newsDao.update(news);

        // SELECT ONE
        news = newsDao.findById(news.getId());
        Assert.assertNotNull(news);
        Assert.assertTrue(news.getId() >= 1);
        Assert.assertEquals(newsId, news.getId());
        Assert.assertEquals(EDITED_NEWS_TITLE, news.getTitle());
        Assert.assertEquals(EDITED_NEWS_TEXT, news.getText());
        Assert.assertTrue(news.getLastSaveTime().after(news.getCreationTime()));
    }

    /**
     * Creates and saves a default test event.
     * 
     * @return ID of the newly created event
     */
    private int createDefaultTestEvent(CalendarEventDao eventDao) {
        return this.createTestEvent(EVENT_DEFAULT_NAME, EVENT_DEFAULT_DATE, EVENT_DEFAULT_DESCRIPTION,
                EVENT_DEFAULT_PRIORITY, eventDao);
    }

    /**
     * Creates and saves a test event.
     * 
     * @return ID of the newly created news
     */
    private int createTestEvent(String name, Date date, String description, PriorityType priority,
            CalendarEventDao eventDao) {
        CalendarEvent event = new CalendarEvent();
        event.setName(name);
        event.setDate(date);
        event.setDescription(description);
        event.setPriority(priority);

        return eventDao.save(event);
    }
}
