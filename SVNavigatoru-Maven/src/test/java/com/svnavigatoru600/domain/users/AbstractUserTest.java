package com.svnavigatoru600.domain.users;

import java.util.Set;

import org.assertj.core.util.Sets;
import org.junit.Before;
import org.springframework.security.core.GrantedAuthority;

/**
 * The parent of all classes which contain {@link org.junit.Test tests} of the {@link User} class and its methods.
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
    static final Set<GrantedAuthority> DEFAULT_AUTHORITIES = Sets.newHashSet();
    static final int DEFAULT_SMTP_PORT = 2525;

    private User defaultUser = null;

    @Before
    public void setUpDefaultUser() throws Exception {
        defaultUser = new User(DEFAULT_USERNAME, DEFAULT_PASSWORD, DEFAULT_ENABLED, DEFAULT_FIRST_NAME,
                DEFAULT_LAST_NAME, DEFAULT_EMAIL, DEFAULT_PHONE, DEFAULT_IS_TEST_USER, DEFAULT_AUTHORITIES);
        defaultUser.setSubscribedToNews(DEFAULT_SUBSCRIBED_TO_NEWS);
        defaultUser.setSubscribedToEvents(DEFAULT_SUBSCRIBED_TO_EVENTS);
        defaultUser.setSubscribedToForum(DEFAULT_SUBSCRIBED_TO_FORUM);
        defaultUser.setSubscribedToOtherDocuments(DEFAULT_SUBSCRIBED_TO_OTHER_DOCUMENTS);
        defaultUser.setSubscribedToOtherSections(DEFAULT_SUBSCRIBED_TO_OTHER_SECTIONS);
        defaultUser.setSmtpPort(DEFAULT_SMTP_PORT);
    }

    /**
     * Trivial getter
     */
    protected User getDefaultUser() {
        return defaultUser;
    }
}
