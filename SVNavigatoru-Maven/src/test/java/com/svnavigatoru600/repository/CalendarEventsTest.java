package com.svnavigatoru600.repository;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.test.category.PersistenceTests;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public class CalendarEventsTest extends AbstractMapperTest {

    /**
     * Tests methods of the {@link CalendarEventDao} interface.
     */
    @Test
    public void testWholeNewsDaoInterface() throws Exception {
        CalendarEventDao eventDao = APPLICATION_CONTEXT.getBean(CalendarEventDao.class);

    }
}
