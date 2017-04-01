package com.svnavigatoru600.repository;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.dao.EmptyResultDataAccessException;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.OrderType;
import com.svnavigatoru600.test.category.PersistenceTests;

/**
 * Tests methods of the {@link SessionRecordDao} interface.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public final class SessionRecordDaoTest extends AbstractRepositoryTest {

    /**
     * Type of the edited test session record.
     */
    static final SessionRecordType EDITED_SESSION_RECORD_TYPE = SessionRecordType.SESSION_RECORD_OF_SV;
    /**
     * Session date of the edited test session record. It represents a day after 20 days from today.
     */
    static final Date EDITED_SESSION_RECORD_SESSION_DATE = DateUtils.getDay(20);
    /**
     * Discussed topics of the edited test session record.
     */
    static final String EDITED_SESSION_RECORD_DISCUSSED_TOPICS = "session record discussed topics 2";

    @Test
    public void testSaveFindByIdWithFile() throws Exception {
        SessionRecordDao recordDao = TEST_UTILS.getSessionRecordDao();

        // INSERT
        int recordId = TEST_UTILS.createDefaultTestSessionRecord();

        // SELECT ONE
        SessionRecord record = recordDao.findById(recordId);
        doAssertAfterFindById(record, recordId);
        Assert.assertEquals(RepositoryTestUtils.DOCUMENT_RECORD_DEFAULT_FILE.length(), record.getFile().length());
    }

    @Test
    public void testSaveFindByIdWithoutFile() throws Exception {
        SessionRecordDao recordDao = TEST_UTILS.getSessionRecordDao();

        // INSERT
        int recordId = TEST_UTILS.createDefaultTestSessionRecord();

        // SELECT ONE
        SessionRecord record = recordDao.findById(recordId, false);
        doAssertAfterFindById(record, recordId);
        Assert.assertNull(record.getFile());
    }

    private void doAssertAfterFindById(SessionRecord foundRecord, int expectedRecordId) {
        Assert.assertTrue(foundRecord.getId() >= 1);
        Assert.assertEquals(expectedRecordId, foundRecord.getId());
        Assert.assertEquals(RepositoryTestUtils.DOCUMENT_RECORD_DEFAULT_FILE_NAME, foundRecord.getFileName());
        Assert.assertEquals(RepositoryTestUtils.SESSION_RECORD_DEFAULT_TYPE, foundRecord.getTypedType());
        Assert.assertEquals(RepositoryTestUtils.SESSION_RECORD_DEFAULT_SESSION_DATE, foundRecord.getSessionDate());
        Assert.assertEquals(RepositoryTestUtils.SESSION_RECORD_DEFAULT_DISCUSSED_TOPICS,
                foundRecord.getDiscussedTopics());
    }

    @Test
    public void testFindAllOrdered() throws Exception {
        SessionRecordDao recordDao = TEST_UTILS.getSessionRecordDao();

        // TWO INSERTS
        int firstRecordId = TEST_UTILS.createDefaultTestSessionRecord(DateUtils.getYesterday());
        int secondRecordId = TEST_UTILS.createDefaultTestSessionRecord(DateUtils.getTomorrow());

        // SELECT ALL
        List<SessionRecord> foundRecords = recordDao.findAllOrdered(OrderType.DESCENDING);
        int expectedFoundRecordCount = 2;
        Assert.assertEquals(expectedFoundRecordCount, foundRecords.size());
        Assert.assertEquals(secondRecordId, foundRecords.get(0).getId());
        Assert.assertEquals(firstRecordId, foundRecords.get(1).getId());
        Assert.assertNull(foundRecords.get(1).getFile());
    }

    @Test
    public void testFindAllOrderedWithType() throws Exception {
        SessionRecordDao recordDao = TEST_UTILS.getSessionRecordDao();

        // THREE INSERTS
        SessionRecordType type = SessionRecordType.SESSION_RECORD_OF_BOARD;
        int firstRecordId = TEST_UTILS.createDefaultTestSessionRecord(type, DateUtils.getYesterday());
        @SuppressWarnings("unused")
        int secondRecordId = TEST_UTILS.createDefaultTestSessionRecord(SessionRecordType.SESSION_RECORD_OF_SV,
                DateUtils.getToday());
        int thirdRecordId = TEST_UTILS.createDefaultTestSessionRecord(type, DateUtils.getTomorrow());

        // SELECT ALL
        List<SessionRecord> foundRecords = recordDao.findAllOrdered(type, OrderType.ASCENDING);
        int expectedFoundRecordCount = 2;
        Assert.assertEquals(expectedFoundRecordCount, foundRecords.size());
        Assert.assertEquals(firstRecordId, foundRecords.get(0).getId());
        Assert.assertEquals(thirdRecordId, foundRecords.get(1).getId());
        Assert.assertNull(foundRecords.get(1).getFile());
    }

    @Test
    public void testUpdate() throws Exception {
        SessionRecordDao recordDao = TEST_UTILS.getSessionRecordDao();

        // INSERT & SELECT ONE
        int recordId = TEST_UTILS.createDefaultTestSessionRecord();
        SessionRecord record = recordDao.findById(recordId);

        // UPDATE
        record.setFileName(RepositoryTestUtils.EDITED_DOCUMENT_RECORD_FILE_NAME);
        record.setFile(RepositoryTestUtils.EDITED_DOCUMENT_RECORD_FILE);
        record.setType(EDITED_SESSION_RECORD_TYPE);
        record.setSessionDate(EDITED_SESSION_RECORD_SESSION_DATE);
        record.setDiscussedTopics(EDITED_SESSION_RECORD_DISCUSSED_TOPICS);
        recordDao.update(record);

        // SELECT ONE
        record = recordDao.findById(record.getId());
        Assert.assertEquals(recordId, record.getId());
        Assert.assertEquals(RepositoryTestUtils.EDITED_DOCUMENT_RECORD_FILE_NAME, record.getFileName());
        Assert.assertEquals(RepositoryTestUtils.EDITED_DOCUMENT_RECORD_FILE.length(), record.getFile().length());
        Assert.assertFalse(RepositoryTestUtils.DOCUMENT_RECORD_DEFAULT_FILE.length() == record.getFile().length());
        Assert.assertEquals(EDITED_SESSION_RECORD_TYPE, record.getTypedType());
        Assert.assertEquals(EDITED_SESSION_RECORD_SESSION_DATE, record.getSessionDate());
        Assert.assertEquals(EDITED_SESSION_RECORD_DISCUSSED_TOPICS, record.getDiscussedTopics());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testDelete() throws Exception {
        SessionRecordDao recordDao = TEST_UTILS.getSessionRecordDao();

        // INSERT & SELECT ONE
        int recordId = TEST_UTILS.createDefaultTestSessionRecord();
        SessionRecord record = recordDao.findById(recordId);

        // DELETE
        recordDao.delete(record);

        // SELECT ONE
        // Throws an exception since the record cannot be found.
        recordDao.findById(record.getId());
    }
}
