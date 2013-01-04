package com.svnavigatoru600.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
 * DO NOT CREATE any default instances or any other of persisted classes (e.g. Contribution, CalendarEvent)
 * here, in advance. The first the classes can be instantiated is in {@link org.junit.Before Before} methods.
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
     * Default text of test contribution.
     */
    static final String CONTRIBUTION_DEFAULT_TEXT = "contribution text 1";
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
    int createDefaultTestEvent(CalendarEventDao eventDao) {
        return this.createDefaultTestEvent(EVENT_DEFAULT_DATE, eventDao);
    }

    /**
     * Creates and saves a default test event with the given date.
     * 
     * @return ID of the newly created event
     */
    int createDefaultTestEvent(Date date, CalendarEventDao eventDao) {
        return this.createTestEvent(EVENT_DEFAULT_NAME, date, EVENT_DEFAULT_DESCRIPTION,
                EVENT_DEFAULT_PRIORITY, eventDao);
    }

    /**
     * Creates and saves a test event.
     * 
     * @return ID of the newly created event
     */
    int createTestEvent(String name, Date date, String description, PriorityType priority,
            CalendarEventDao eventDao) {
        CalendarEvent event = new CalendarEvent();
        event.setName(name);
        event.setDate(date);
        event.setDescription(description);
        event.setPriority(priority);
        return eventDao.save(event);
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
    int createDefaultTestNews(NewsDao newsDao) {
        return this.createTestNews(NEWS_DEFAULT_TITLE, NEWS_DEFAULT_TEXT, newsDao);
    }

    /**
     * Creates and saves a test news.
     * 
     * @return ID of the newly created news
     */
    int createTestNews(String title, String text, NewsDao newsDao) {
        News news = new News();
        news.setTitle(title);
        news.setText(text);
        return newsDao.save(news);
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
    int createTestContribution(Thread thread, String text, User author, ContributionDao contributionDao) {
        Contribution contribution = new Contribution();
        contribution.setThread(thread);
        contribution.setText(text);
        contribution.setAuthor(author);
        return contributionDao.save(contribution);
    }

    /**
     * Gets a {@link ThreadDao} from an application context.
     */
    ThreadDao getThreadDao() {
        return this.applicationContext.getBean(ThreadDao.class);
    }

    /**
     * Creates and saves a test thread.
     * 
     * @return ID of the newly created thread
     */
    int createTestThread(String name, User author, ThreadDao threadDao) {
        Thread thread = new Thread();
        thread.setName(name);
        thread.setAuthor(author);
        thread.setContributions(new ArrayList<Contribution>());
        return threadDao.save(thread);
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
        UserDao userDao = this.getUserDao();
        String username = "username 1";
        String password = "password 1";
        boolean enabled = true;
        String firstName = "first name 1";
        String lastName = "last name 1";
        String email = "email 1";
        String phone = "phone 1";
        boolean isTestUser = false;
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new Authority(username, AuthorityType.ROLE_REGISTERED_USER.name()));
        this.createTestUser(username, password, enabled, firstName, lastName, email, phone, isTestUser,
                authorities, userDao);
        return userDao.findByUsername(username);
    }

    /**
     * Creates and saves a test user.
     * 
     * @return ID of the newly created user
     */
    void createTestUser(String username, String password, boolean enabled, String firstName, String lastName,
            String email, String phone, boolean isTestUser, Set<GrantedAuthority> authorities, UserDao userDao) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(enabled);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setTestUser(isTestUser);
        user.setAuthorities(authorities);
        userDao.save(user);
    }
}
