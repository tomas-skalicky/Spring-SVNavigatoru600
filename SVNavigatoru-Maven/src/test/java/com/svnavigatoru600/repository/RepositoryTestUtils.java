package com.svnavigatoru600.repository;

import java.util.ArrayList;
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
import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.repository.users.UserDao;

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
    static final String USER_DEFAULT_EMAIL = "email 1";
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
    static final Set<GrantedAuthority> USER_DEFAULT_AUTHORITIES;

    static {
        USER_DEFAULT_AUTHORITIES = new HashSet<GrantedAuthority>();
        USER_DEFAULT_AUTHORITIES.add(new Authority(USER_DEFAULT_USERNAME, AuthorityType.ROLE_REGISTERED_USER
                .name()));
    }

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
     * Creates and saves a default test user.
     * 
     * @return ID of the newly created user
     */
    User createDefaultTestUser() {
        return this
                .createDefaultTestUser(USER_DEFAULT_USERNAME, USER_DEFAULT_EMAIL, USER_DEFAULT_AUTHORITIES);
    }

    /**
     * Creates and saves a default test user.
     * 
     * @return ID of the newly created user
     */
    User createDefaultTestUser(String username, String email, Set<GrantedAuthority> authorities) {
        this.createTestUser(username, USER_DEFAULT_PASSWORD, USER_DEFAULT_ENABLED, USER_DEFAULT_FIRST_NAME,
                USER_DEFAULT_LAST_NAME, email, USER_DEFAULT_PHONE, USER_DEFAULT_IS_TEST_USER, authorities);
        return this.getUserDao().findByUsername(username);
    }

    /**
     * Creates and saves a test user.
     * 
     * @return ID of the newly created user
     */
    void createTestUser(String username, String password, boolean enabled, String firstName, String lastName,
            String email, String phone, boolean isTestUser, Set<GrantedAuthority> authorities) {
        User user = new User(username, password, enabled, firstName, lastName, email, phone, isTestUser,
                authorities);
        this.getUserDao().save(user);
    }
}
