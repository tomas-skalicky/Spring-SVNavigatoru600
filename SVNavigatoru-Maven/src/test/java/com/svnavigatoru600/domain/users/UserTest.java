package com.svnavigatoru600.domain.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.test.category.UnitTests;

/**
 * Tests the rest of methods of the {@link User} class which are not tested in the {@link UserCopyMethodsTest} class.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(UnitTests.class)
public final class UserTest extends AbstractUserTest {

    @After
    public void emptyAuthorities() {
        getDefaultUser().getAuthorities().clear();
    }

    @Test
    public void testAddAuthority() {
        final User user = getDefaultUser();

        // Adds
        final AuthorityTypeEnum newAuthority = AuthorityTypeEnum.ROLE_MEMBER_OF_BOARD;
        user.addAuthority(newAuthority);

        // Checks
        this.checkAuthorities(user, newAuthority);
    }

    @Test
    public void testAddAuthorityMoreTimes() {
        final User user = getDefaultUser();

        // Adds more times.
        final AuthorityTypeEnum newAuthority = AuthorityTypeEnum.ROLE_MEMBER_OF_BOARD;
        user.addAuthority(newAuthority);
        user.addAuthority(newAuthority);
        // OK since the remoteAuthority method should not throw an exception.

        // Checks
        this.checkAuthorities(user, newAuthority);
    }

    @Test
    public void testRemoveAuthority() {
        final User user = getDefaultUser();

        // Adds two authorities.
        user.addAuthority(AuthorityTypeEnum.ROLE_MEMBER_OF_BOARD);
        user.addAuthority(AuthorityTypeEnum.ROLE_USER_ADMINISTRATOR);

        // Checks
        this.checkAuthorities(user,
                Arrays.asList(AuthorityTypeEnum.ROLE_MEMBER_OF_BOARD, AuthorityTypeEnum.ROLE_USER_ADMINISTRATOR));

        // Removes
        user.removeAuthority(AuthorityTypeEnum.ROLE_MEMBER_OF_BOARD);

        // Checks
        this.checkAuthorities(user, AuthorityTypeEnum.ROLE_USER_ADMINISTRATOR);
    }

    @Test
    public void testRemoveAuthorityMoreTimes() {
        final User user = getDefaultUser();

        // Adds
        final AuthorityTypeEnum newAuthority = AuthorityTypeEnum.ROLE_MEMBER_OF_BOARD;
        user.addAuthority(newAuthority);

        // Removes more times.
        user.removeAuthority(newAuthority);
        user.removeAuthority(newAuthority);
        // OK since the remoteAuthority method should not throw an exception.

        // Checks
        this.checkAuthorities(user, new ArrayList<AuthorityTypeEnum>());
    }

    /**
     * Checks that the given {@link User user} has just the given <code>ownedAuthority</code> and no other.
     */
    private void checkAuthorities(final User user, final AuthorityTypeEnum ownedAuthority) {
        this.checkAuthorities(user, Arrays.asList(ownedAuthority));
    }

    /**
     * Checks that the given {@link User user} has exactly the given <code>ownedAuthorities</code>, i.e. he has all of
     * them and no other.
     */
    private void checkAuthorities(final User user, final List<AuthorityTypeEnum> ownedAuthorities) {
        for (final AuthorityTypeEnum authority : AuthorityTypeEnum.values()) {
            if (ownedAuthorities.contains(authority)) {
                Assert.assertTrue(user.hasAuthority(authority));
            } else {
                Assert.assertFalse(user.hasAuthority(authority));
            }
        }
    }

    @Test
    public void testFilterWithNoEmailOut() {
        final User firstUser = new User();
        firstUser.setEmail(null);
        final User secondUser = new User();
        secondUser.setEmail("");
        final User thirdUser = new User();
        thirdUser.setEmail("     ");
        final User forthUser = new User();
        forthUser.setEmail("email");
        final User fifthUser = new User();
        fifthUser.setEmail("email@host.com");

        final List<User> inputUsers = Arrays.asList(firstUser, secondUser, thirdUser, forthUser, fifthUser);
        final List<User> expectedUsers = Arrays.asList(forthUser, fifthUser);

        final List<User> actualUsers = User.filterWithNoEmailOut(inputUsers);
        Assert.assertEquals(expectedUsers.size(), actualUsers.size());
        Assert.assertTrue(actualUsers.containsAll(expectedUsers));
    }
}
