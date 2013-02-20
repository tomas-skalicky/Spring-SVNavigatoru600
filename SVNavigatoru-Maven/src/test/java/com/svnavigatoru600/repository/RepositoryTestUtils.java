package com.svnavigatoru600.repository;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.eventcalendar.PriorityType;
import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.domain.users.UserBuilder;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.repository.users.AuthorityDao;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.File;

/**
 * Contains convenient methods used in {@link AbstractRepositoryTest DAO tests}. The methods are basically two
 * types:<br />
 * those which return an implementation of DAO interface and<br />
 * those which return an instance of persisted class.
 * <p>
 * DO NOT CREATE any default instances or any other instances of persisted classes (e.g. Contribution,
 * CalendarEvent) here, in advance. The very first time the classes can be instantiated is in
 * {@link org.junit.Before Before} methods.<br />
 * The reason is a potential danger that a certain test would change an instance created here and other tests
 * in the same test class would use this changed object in a belief that the object has really the original
 * properties, but it has not.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class RepositoryTestUtils {

    /**
     * Default name of test event.
     */
    static final String EVENT_DEFAULT_NAME = "event name 1";
    /**
     * Default date of test event. It equals now.
     */
    static final Date EVENT_DEFAULT_DATE = new Date();
    /**
     * Default description of test event.
     */
    static final String EVENT_DEFAULT_DESCRIPTION = "event description 1";
    /**
     * Default priority of test event.
     */
    static final PriorityType EVENT_DEFAULT_PRIORITY = PriorityType.LOW;
    /**
     * Default title of test news.
     */
    static final String NEWS_DEFAULT_TITLE = "news title 1";
    /**
     * Default text of test news.
     */
    static final String NEWS_DEFAULT_TEXT = "news text 1";
    /**
     * Default name of test thread.
     */
    static final String THREAD_DEFAULT_NAME = "thread name 1";
    /**
     * Default contributions of test thread.
     */
    static final List<Contribution> THREAD_DEFAULT_CONTRIBUTIONS = new ArrayList<Contribution>();
    /**
     * Default text of test contribution.
     */
    static final String CONTRIBUTION_DEFAULT_TEXT = "contribution text 1";
    /**
     * Default username (=login) of test user.
     */
    static final String USER_DEFAULT_USERNAME = "username 1";
    /**
     * Default password of test user.
     */
    static final String USER_DEFAULT_PASSWORD = "password 1";
    /**
     * Default flag of test user whether he is active.
     */
    static final boolean USER_DEFAULT_ENABLED = true;
    /**
     * Default first name of test user.
     */
    static final String USER_DEFAULT_FIRST_NAME = "first name 1";
    /**
     * Default last name of test user.
     */
    static final String USER_DEFAULT_LAST_NAME = "last name 1";
    /**
     * Default email of test user.
     */
    static final String USER_DEFAULT_EMAIL = "email1@host.com";
    /**
     * Default phone of test user.
     */
    static final String USER_DEFAULT_PHONE = "phone 1";
    /**
     * Default flag of test user whether he is a test one.
     */
    static final boolean USER_DEFAULT_IS_TEST_USER = false;
    /**
     * Default authorities of test user.
     */
    static final Set<GrantedAuthority> USER_DEFAULT_AUTHORITIES = new HashSet<GrantedAuthority>();
    /**
     * Default username of the second test user.
     */
    static final String SECOND_USER_DEFAULT_USERNAME = "username 2";
    /**
     * Default email of the second test user.
     */
    static final String SECOND_USER_DEFAULT_EMAIL = "email2@host.com";
    /**
     * Default authorities of the second test user.
     */
    static final Set<GrantedAuthority> SECOND_USER_DEFAULT_AUTHORITIES = new HashSet<GrantedAuthority>();
    /**
     * Default type of user's test authority.
     */
    static final AuthorityType AUTHORITY_DEFAULT_TYPE = AuthorityType.ROLE_REGISTERED_USER;
    /**
     * Default type of user's second test authority. This type is different from
     * {@link #AUTHORITY_DEFAULT_TYPE}.
     */
    static final AuthorityType SECOND_AUTHORITY_DEFAULT_TYPE = AuthorityType.ROLE_MEMBER_OF_SV;
    /**
     * Default filename of test document record.
     */
    static final String DOCUMENT_RECORD_DEFAULT_FILE_NAME = "file name 1";
    /**
     * Default file of test document record.
     */
    static final Blob DOCUMENT_RECORD_DEFAULT_FILE = File.convertToBlobNoException(new byte[1]);
    /**
     * Filename of the edited test document record.
     */
    static final String EDITED_DOCUMENT_RECORD_FILE_NAME = "file name 2";
    /**
     * File of the edited test document record.
     */
    static final Blob EDITED_DOCUMENT_RECORD_FILE = File.convertToBlobNoException(new byte[2]);
    /**
     * Default type of test session record.
     */
    static final SessionRecordType SESSION_RECORD_DEFAULT_TYPE = SessionRecordType.SESSION_RECORD_OF_BOARD;
    /**
     * Default session date of test session record. It represents a day after 7 days from today.
     */
    static final Date SESSION_RECORD_DEFAULT_SESSION_DATE = DateUtils.getDay(7);
    /**
     * Default discussed topics of test session record.
     */
    static final String SESSION_RECORD_DEFAULT_DISCUSSED_TOPICS = "session record discussed topics 1";

    /**
     * Application context which contains necessary beans.
     */
    private final ApplicationContext applicationContext;

    RepositoryTestUtils(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Gets a {@link CalendarEventDao} from an application context.
     */
    CalendarEventDao getEventDao() {
        return this.applicationContext.getBean(CalendarEventDao.class);
    }

    /**
     * Creates and saves a default test event.
     * 
     * @return ID of the newly created event
     */
    int createDefaultTestEvent() {
        return this.createDefaultTestEvent(EVENT_DEFAULT_DATE);
    }

    /**
     * Creates and saves a default test event with the given date.
     * 
     * @return ID of the newly created event
     */
    int createDefaultTestEvent(Date date) {
        return this.createTestEvent(EVENT_DEFAULT_NAME, date, EVENT_DEFAULT_DESCRIPTION,
                EVENT_DEFAULT_PRIORITY);
    }

    /**
     * Creates and saves a test event.
     * 
     * @return ID of the newly created event
     */
    int createTestEvent(String name, Date date, String description, PriorityType priority) {
        CalendarEvent event = new CalendarEvent(name, date, description, priority);
        return this.getEventDao().save(event);
    }

    /**
     * Gets a {@link NewsDao} from an application context.
     */
    NewsDao getNewsDao() {
        return this.applicationContext.getBean(NewsDao.class);
    }

    /**
     * Creates and saves a default test news.
     * 
     * @return ID of the newly created news
     */
    int createDefaultTestNews() {
        return this.createTestNews(NEWS_DEFAULT_TITLE, NEWS_DEFAULT_TEXT);
    }

    /**
     * Creates and saves a test news.
     * 
     * @return ID of the newly created news
     */
    int createTestNews(String title, String text) {
        News news = new News(title, text);
        return this.getNewsDao().save(news);
    }

    /**
     * Gets a {@link WysiwygSectionDao} from an application context.
     */
    WysiwygSectionDao getSectionDao() {
        return this.applicationContext.getBean(WysiwygSectionDao.class);
    }

    /**
     * Gets a {@link ContributionDao} from an application context.
     */
    ContributionDao getContributionDao() {
        return this.applicationContext.getBean(ContributionDao.class);
    }

    /**
     * Creates and saves a test contribution.
     * 
     * @return ID of the newly created contribution
     */
    int createTestContribution(Thread thread, String text, User author) {
        Contribution contribution = new Contribution(thread, text, author);
        return this.getContributionDao().save(contribution);
    }

    /**
     * Gets a {@link ThreadDao} from an application context.
     */
    ThreadDao getThreadDao() {
        return this.applicationContext.getBean(ThreadDao.class);
    }

    /**
     * Creates and saves a test thread.
     * <p>
     * {@link Contribution#getThread() thread} of the given <code>contributions</code> need not be set. This
     * method does it itself.
     * 
     * @return ID of the newly created thread
     */
    int createTestThread(String name, User author, List<Contribution> contributions) {
        Thread thread = new Thread(name, author, contributions);
        for (Contribution contribution : contributions) {
            contribution.setThread(thread);
        }
        return this.getThreadDao().save(thread);
    }

    /**
     * Gets a {@link UserDao} from an application context.
     */
    UserDao getUserDao() {
        return this.applicationContext.getBean(UserDao.class);
    }

    /**
     * Creates {@link UserBuilder} thereof {@link User} is a default one.
     */
    public UserBuilder createDefaultUserBuilder() {
        return UserBuilder.anUser().withUsername(USER_DEFAULT_USERNAME).withPassword(USER_DEFAULT_PASSWORD)
                .enabled(USER_DEFAULT_ENABLED).withFirstName(USER_DEFAULT_FIRST_NAME)
                .withLastName(USER_DEFAULT_LAST_NAME).withEmail(USER_DEFAULT_EMAIL)
                .withPhone(USER_DEFAULT_PHONE).forTestPurposes(USER_DEFAULT_IS_TEST_USER)
                .withAuthorities(USER_DEFAULT_AUTHORITIES);
    }

    /**
     * Creates and saves the first default test user.
     * 
     * @return Newly created user
     */
    User createDefaultTestUser() {
        return this.saveTestUser(this.createDefaultUserBuilder());
    }

    /**
     * Creates and saves the second default test user.
     * 
     * @return Newly created user
     */
    User createSecondDefaultTestUser() {
        return this.saveTestUser(this.createDefaultUserBuilder().withUsername(SECOND_USER_DEFAULT_USERNAME)
                .withEmail(SECOND_USER_DEFAULT_EMAIL).withAuthorities(SECOND_USER_DEFAULT_AUTHORITIES));
    }

    /**
     * Creates and saves a default test user.
     * 
     * @return Newly created user
     */
    User saveTestUser(UserBuilder userBuilder) {
        User user = userBuilder.build();
        this.getUserDao().save(user);
        return this.getUserDao().findByUsername(user.getUsername());
    }

    /**
     * Gets a {@link AuthorityDao} from an application context.
     */
    AuthorityDao getAuthorityDao() {
        return this.applicationContext.getBean(AuthorityDao.class);
    }

    /**
     * Creates and saves a default test authority.
     */
    void createDefaultTestAuthority(String username) {
        this.createTestAuthority(username, AUTHORITY_DEFAULT_TYPE);
    }

    /**
     * Creates and saves the second default test authority.
     */
    void createSecondDefaultTestAuthority(String username) {
        this.createTestAuthority(username, SECOND_AUTHORITY_DEFAULT_TYPE);
    }

    /**
     * Creates and saves a test authority.
     */
    void createTestAuthority(String username, AuthorityType authorityType) {
        Authority authority = new Authority(username, authorityType);
        this.getAuthorityDao().save(authority);
    }

    /**
     * Extracts types of the given authorities.
     * 
     * @return Extracted types
     */
    Collection<String> extractAuthorityTypes(Collection<Authority> authorities) {
        return this.extractAuthorityTypes(authorities.toArray(new Authority[authorities.size()]));
    }

    /**
     * Extracts types of the given authorities.
     * 
     * @return Extracted types
     */
    Collection<String> extractAuthorityTypes(Authority[] authorities) {
        List<String> types = new ArrayList<String>(authorities.length);
        for (Authority authority : authorities) {
            types.add(authority.getId().getAuthority());
        }
        return types;
    }

    /**
     * Gets a {@link SessionRecordDao} from an application context.
     */
    SessionRecordDao getSessionRecordDao() {
        return this.applicationContext.getBean(SessionRecordDao.class);
    }

    /**
     * Creates and saves a default test session record. It is not associated with any file.
     * 
     * @return ID of the newly created record
     */
    int createDefaultTestSessionRecord() {
        return this.createDefaultTestSessionRecord(SESSION_RECORD_DEFAULT_SESSION_DATE);
    }

    /**
     * Creates and saves a default test session record. It is not associated with any file.
     * 
     * @return ID of the newly created record
     */
    int createDefaultTestSessionRecord(Date sessionDate) {
        return this.createDefaultTestSessionRecord(SESSION_RECORD_DEFAULT_TYPE, sessionDate);
    }

    /**
     * Creates and saves a default test session record. It is not associated with any file.
     * 
     * @return ID of the newly created record
     */
    int createDefaultTestSessionRecord(SessionRecordType type, Date sessionDate) {
        return this.createTestSessionRecord(DOCUMENT_RECORD_DEFAULT_FILE_NAME, DOCUMENT_RECORD_DEFAULT_FILE,
                type, sessionDate, SESSION_RECORD_DEFAULT_DISCUSSED_TOPICS);
    }

    /**
     * Creates and saves a test session record. It is not associated with any file.
     * 
     * @return ID of the newly created record
     */
    int createTestSessionRecord(String fileName, Blob file, SessionRecordType type, Date sessionDate,
            String discussedTopics) {
        SessionRecord record = new SessionRecord(fileName, file, type, sessionDate, discussedTopics);
        return this.getSessionRecordDao().save(record);
    }
}
