package com.svnavigatoru600.domain.users;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.springframework.security.core.GrantedAuthority;

/**
 * The parent of all classes which contain {@link org.junit.Test tests} of the {@link User} class and its
 * methods.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractUserTest {

    static final String DEFAULT_USERNAME = "username 1";
    static final String DEFAULT_PASSWORD = "password 1";
    static final boolean DEFAULT_ENABLED = true;
    static final String DEFAULT_FIRST_NAME = "first name 1";
    static final String DEFAULT_LAST_NAME = "last name 1";
    static final String DEFAULT_EMAIL = "email 1";
    static final String DEFAULT_PHONE = "phone 1";
    static final boolean DEFAULT_IS_TEST_USER = false;
    static final boolean DEFAULT_SUBSCRIBED_TO_NEWS = true;
    static final boolean DEFAULT_SUBSCRIBED_TO_EVENTS = true;
    static final boolean DEFAULT_SUBSCRIBED_TO_FORUM = true;
    static final boolean DEFAULT_SUBSCRIBED_TO_OTHER_DOCUMENTS = false;
    static final boolean DEFAULT_SUBSCRIBED_TO_OTHER_SECTIONS = false;
    static final Set<GrantedAuthority> DEFAULT_AUTHORITIES = new HashSet<GrantedAuthority>();

    private User defaultUser = null;

    @Before
    public void setUpDefaultUser() throws Exception {
        this.defaultUser = new User(DEFAULT_USERNAME, DEFAULT_PASSWORD, DEFAULT_ENABLED,
                DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, DEFAULT_EMAIL, DEFAULT_PHONE, DEFAULT_IS_TEST_USER,
                DEFAULT_AUTHORITIES);
        this.defaultUser.setSubscribedToNews(DEFAULT_SUBSCRIBED_TO_NEWS);
        this.defaultUser.setSubscribedToEvents(DEFAULT_SUBSCRIBED_TO_EVENTS);
        this.defaultUser.setSubscribedToForum(DEFAULT_SUBSCRIBED_TO_FORUM);
        this.defaultUser.setSubscribedToOtherDocuments(DEFAULT_SUBSCRIBED_TO_OTHER_DOCUMENTS);
        this.defaultUser.setSubscribedToOtherSections(DEFAULT_SUBSCRIBED_TO_OTHER_SECTIONS);
    }

    /**
     * Trivial getter
     */
    protected User getDefaultUser() {
        return this.defaultUser;
    }
}
