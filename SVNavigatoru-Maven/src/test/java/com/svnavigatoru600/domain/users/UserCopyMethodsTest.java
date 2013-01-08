package com.svnavigatoru600.domain.users;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

/**
 * Tests only copy*** methods of the {@link User} class.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
public class UserCopyMethodsTest {

    private static final String DEFAULT_USERNAME = "username 1";
    private static final String DEFAULT_PASSWORD = "password 1";
    private static final boolean DEFAULT_ENABLED = true;
    private static final String DEFAULT_FIRST_NAME = "first name 1";
    private static final String DEFAULT_LAST_NAME = "last name 1";
    private static final String DEFAULT_EMAIL = "email 1";
    private static final String DEFAULT_PHONE = "phone 1";
    private static final boolean DEFAULT_IS_TEST_USER = false;
    private static final boolean DEFAULT_SUBSCRIBED_TO_NEWS = true;
    private static final boolean DEFAULT_SUBSCRIBED_TO_EVENTS = true;
    private static final boolean DEFAULT_SUBSCRIBED_TO_FORUM = true;
    private static final boolean DEFAULT_SUBSCRIBED_TO_OTHER_DOCUMENTS = false;
    private static final boolean DEFAULT_SUBSCRIBED_TO_OTHER_SECTIONS = false;
    private static final Set<GrantedAuthority> DEFAULT_AUTHORITIES = new HashSet<GrantedAuthority>();

    private static final String NEW_USERNAME = "username 2";
    private static final String NEW_PASSWORD = "password 2";
    private static final boolean NEW_ENABLED = false;
    private static final String NEW_FIRST_NAME = "first name 2";
    private static final String NEW_LAST_NAME = "last name 2";
    private static final String NEW_EMAIL = "email 2";
    private static final String NEW_PHONE = "phone 2";
    private static final boolean NEW_IS_TEST_USER = true;
    private static final boolean NEW_SUBSCRIBED_TO_NEWS = false;
    private static final boolean NEW_SUBSCRIBED_TO_EVENTS = false;
    private static final boolean NEW_SUBSCRIBED_TO_FORUM = false;
    private static final boolean NEW_SUBSCRIBED_TO_OTHER_DOCUMENTS = true;
    private static final boolean NEW_SUBSCRIBED_TO_OTHER_SECTIONS = true;
    private static final Set<GrantedAuthority> NEW_AUTHORITIES = null;

    private User destinationUser = null;
    private User sourceUser = null;

    @Before
    public void setUpDefaultUser() throws Exception {
        this.destinationUser = new User(DEFAULT_USERNAME, DEFAULT_PASSWORD, DEFAULT_ENABLED,
                DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, DEFAULT_EMAIL, DEFAULT_PHONE, DEFAULT_IS_TEST_USER,
                DEFAULT_AUTHORITIES);
        this.destinationUser.setSubscribedToNews(DEFAULT_SUBSCRIBED_TO_NEWS);
        this.destinationUser.setSubscribedToEvents(DEFAULT_SUBSCRIBED_TO_EVENTS);
        this.destinationUser.setSubscribedToForum(DEFAULT_SUBSCRIBED_TO_FORUM);
        this.destinationUser.setSubscribedToOtherDocuments(DEFAULT_SUBSCRIBED_TO_OTHER_DOCUMENTS);
        this.destinationUser.setSubscribedToOtherSections(DEFAULT_SUBSCRIBED_TO_OTHER_SECTIONS);
    }

    @Before
    public void setUpNewUser() throws Exception {
        this.sourceUser = new User(NEW_USERNAME, NEW_PASSWORD, NEW_ENABLED, NEW_FIRST_NAME, NEW_LAST_NAME,
                NEW_EMAIL, NEW_PHONE, NEW_IS_TEST_USER, NEW_AUTHORITIES);
        this.sourceUser.setSubscribedToNews(NEW_SUBSCRIBED_TO_NEWS);
        this.sourceUser.setSubscribedToEvents(NEW_SUBSCRIBED_TO_EVENTS);
        this.sourceUser.setSubscribedToForum(NEW_SUBSCRIBED_TO_FORUM);
        this.sourceUser.setSubscribedToOtherDocuments(NEW_SUBSCRIBED_TO_OTHER_DOCUMENTS);
        this.sourceUser.setSubscribedToOtherSections(NEW_SUBSCRIBED_TO_OTHER_SECTIONS);
    }

    @Test
    public void testCopyEmailPhoneSubscriptions() {
        this.destinationUser.copyEmailPhoneSubscriptions(this.sourceUser);

        Assert.assertEquals(DEFAULT_USERNAME, this.destinationUser.getUsername());
        Assert.assertEquals(DEFAULT_PASSWORD, this.destinationUser.getPassword());
        Assert.assertEquals(DEFAULT_ENABLED, this.destinationUser.isEnabled());
        Assert.assertEquals(DEFAULT_FIRST_NAME, this.destinationUser.getFirstName());
        Assert.assertEquals(DEFAULT_LAST_NAME, this.destinationUser.getLastName());
        Assert.assertEquals(NEW_EMAIL, this.destinationUser.getEmail());
        Assert.assertEquals(NEW_PHONE, this.destinationUser.getPhone());
        Assert.assertEquals(DEFAULT_IS_TEST_USER, this.destinationUser.isTestUser());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_NEWS, this.destinationUser.isSubscribedToNews());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_EVENTS, this.destinationUser.isSubscribedToEvents());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_FORUM, this.destinationUser.isSubscribedToForum());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_OTHER_DOCUMENTS,
                this.destinationUser.isSubscribedToOtherDocuments());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_OTHER_SECTIONS,
                this.destinationUser.isSubscribedToOtherSections());
        Assert.assertEquals(DEFAULT_AUTHORITIES, this.destinationUser.getAuthorities());
    }

    @Test
    public void testCopySubscriptions() {
        this.destinationUser.copySubscriptions(this.sourceUser);

        Assert.assertEquals(DEFAULT_USERNAME, this.destinationUser.getUsername());
        Assert.assertEquals(DEFAULT_PASSWORD, this.destinationUser.getPassword());
        Assert.assertEquals(DEFAULT_ENABLED, this.destinationUser.isEnabled());
        Assert.assertEquals(DEFAULT_FIRST_NAME, this.destinationUser.getFirstName());
        Assert.assertEquals(DEFAULT_LAST_NAME, this.destinationUser.getLastName());
        Assert.assertEquals(DEFAULT_EMAIL, this.destinationUser.getEmail());
        Assert.assertEquals(DEFAULT_PHONE, this.destinationUser.getPhone());
        Assert.assertEquals(DEFAULT_IS_TEST_USER, this.destinationUser.isTestUser());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_NEWS, this.destinationUser.isSubscribedToNews());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_EVENTS, this.destinationUser.isSubscribedToEvents());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_FORUM, this.destinationUser.isSubscribedToForum());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_OTHER_DOCUMENTS,
                this.destinationUser.isSubscribedToOtherDocuments());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_OTHER_SECTIONS,
                this.destinationUser.isSubscribedToOtherSections());
        Assert.assertEquals(DEFAULT_AUTHORITIES, this.destinationUser.getAuthorities());
    }
}
