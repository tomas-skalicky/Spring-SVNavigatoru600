package com.svnavigatoru600.domain.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

/**
 * Tests the rest of methods of the {@link User} class which are not tested in the {@link UserCopyMethodsTest}
 * class.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class UserTest extends AbstractUserTest {

    @After
    public void emptyAuthorities() {
        this.getDefaultUser().getAuthorities().clear();
    }

    @Test
    public void testAddAuthority() {
        User user = this.getDefaultUser();

        // Adds
        AuthorityType newAuthority = AuthorityType.ROLE_MEMBER_OF_BOARD;
        user.addAuthority(newAuthority);

        // Checks
        this.checkAuthorities(user, newAuthority);
    }

    @Test
    public void testAddAuthorityMoreTimes() {
        User user = this.getDefaultUser();

        // Adds more times.
        AuthorityType newAuthority = AuthorityType.ROLE_MEMBER_OF_BOARD;
        user.addAuthority(newAuthority);
        user.addAuthority(newAuthority);
        // OK since the remoteAuthority method should not throw an exception.

        // Checks
        this.checkAuthorities(user, newAuthority);
    }

    @Test
    public void testRemoveAuthority() {
        User user = this.getDefaultUser();

        // Adds two authorities.
        user.addAuthority(AuthorityType.ROLE_MEMBER_OF_BOARD);
        user.addAuthority(AuthorityType.ROLE_USER_ADMINISTRATOR);

        // Checks
        this.checkAuthorities(user,
                Arrays.asList(AuthorityType.ROLE_MEMBER_OF_BOARD, AuthorityType.ROLE_USER_ADMINISTRATOR));

        // Removes
        user.removeAuthority(AuthorityType.ROLE_MEMBER_OF_BOARD);

        // Checks
        this.checkAuthorities(user, AuthorityType.ROLE_USER_ADMINISTRATOR);
    }

    @Test
    public void testRemoveAuthorityMoreTimes() {
        User user = this.getDefaultUser();

        // Adds
        AuthorityType newAuthority = AuthorityType.ROLE_MEMBER_OF_BOARD;
        user.addAuthority(newAuthority);

        // Removes more times.
        user.removeAuthority(newAuthority);
        user.removeAuthority(newAuthority);
        // OK since the remoteAuthority method should not throw an exception.

        // Checks
        this.checkAuthorities(user, new ArrayList<AuthorityType>());
    }

    /**
     * Checks that the given {@link User user} has just the given <code>ownedAuthority</code> and no other.
     */
    private void checkAuthorities(User user, AuthorityType ownedAuthority) {
        this.checkAuthorities(user, Arrays.asList(ownedAuthority));
    }

    /**
     * Checks that the given {@link User user} has exactly the given <code>ownedAuthorities</code>, i.e. he
     * has all of them and no other.
     */
    private void checkAuthorities(User user, List<AuthorityType> ownedAuthorities) {
        for (AuthorityType authority : AuthorityType.values()) {
            if (ownedAuthorities.contains(authority)) {
                Assert.assertTrue(user.hasAuthority(authority));
            } else {
                Assert.assertFalse(user.hasAuthority(authority));
            }
        }
    }
}
