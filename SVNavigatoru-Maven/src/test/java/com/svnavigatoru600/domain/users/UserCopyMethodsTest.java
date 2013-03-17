package com.svnavigatoru600.domain.users;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.test.category.FastTests;

/**
 * Tests only copy*** methods of the {@link User} class.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Category(FastTests.class)
public class UserCopyMethodsTest extends AbstractUserTest {

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

    private User sourceUser = null;

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
        User destinationUser = this.getDefaultUser();
        destinationUser.copyEmailPhoneSubscriptions(this.sourceUser);

        Assert.assertEquals(DEFAULT_USERNAME, destinationUser.getUsername());
        Assert.assertEquals(DEFAULT_PASSWORD, destinationUser.getPassword());
        Assert.assertEquals(DEFAULT_ENABLED, destinationUser.isEnabled());
        Assert.assertEquals(DEFAULT_FIRST_NAME, destinationUser.getFirstName());
        Assert.assertEquals(DEFAULT_LAST_NAME, destinationUser.getLastName());
        Assert.assertEquals(NEW_EMAIL, destinationUser.getEmail());
        Assert.assertEquals(NEW_PHONE, destinationUser.getPhone());
        Assert.assertEquals(DEFAULT_IS_TEST_USER, destinationUser.isTestUser());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_NEWS, destinationUser.isSubscribedToNews());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_EVENTS, destinationUser.isSubscribedToEvents());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_FORUM, destinationUser.isSubscribedToForum());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_OTHER_DOCUMENTS, destinationUser.isSubscribedToOtherDocuments());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_OTHER_SECTIONS, destinationUser.isSubscribedToOtherSections());
        Assert.assertEquals(DEFAULT_AUTHORITIES, destinationUser.getAuthorities());
    }

    @Test
    public void testCopySubscriptions() {
        User destinationUser = this.getDefaultUser();
        destinationUser.copySubscriptions(this.sourceUser);

        Assert.assertEquals(DEFAULT_USERNAME, destinationUser.getUsername());
        Assert.assertEquals(DEFAULT_PASSWORD, destinationUser.getPassword());
        Assert.assertEquals(DEFAULT_ENABLED, destinationUser.isEnabled());
        Assert.assertEquals(DEFAULT_FIRST_NAME, destinationUser.getFirstName());
        Assert.assertEquals(DEFAULT_LAST_NAME, destinationUser.getLastName());
        Assert.assertEquals(DEFAULT_EMAIL, destinationUser.getEmail());
        Assert.assertEquals(DEFAULT_PHONE, destinationUser.getPhone());
        Assert.assertEquals(DEFAULT_IS_TEST_USER, destinationUser.isTestUser());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_NEWS, destinationUser.isSubscribedToNews());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_EVENTS, destinationUser.isSubscribedToEvents());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_FORUM, destinationUser.isSubscribedToForum());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_OTHER_DOCUMENTS, destinationUser.isSubscribedToOtherDocuments());
        Assert.assertEquals(NEW_SUBSCRIBED_TO_OTHER_SECTIONS, destinationUser.isSubscribedToOtherSections());
        Assert.assertEquals(DEFAULT_AUTHORITIES, destinationUser.getAuthorities());
    }
}
