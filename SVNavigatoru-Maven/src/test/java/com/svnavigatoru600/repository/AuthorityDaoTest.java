package com.svnavigatoru600.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityId;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.AuthorityDao;
import com.svnavigatoru600.test.category.PersistenceTests;

/**
 * Tests methods of the {@link AuthorityDao} interface.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public class AuthorityDaoTest extends AbstractRepositoryTest {

    /**
     * Default test author of threads and contributions.
     */
    private User defaultAuthor = null;

    @Before
    public void createDefaultAuthor() {
        this.defaultAuthor = TEST_UTILS.createDefaultTestUser();
    }

    @Test
    public void testSaveFindAllWithOneAuthority() throws Exception {
        AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // INSERT
        String username = this.defaultAuthor.getUsername();
        TEST_UTILS.createDefaultTestAuthority(username);

        // SELECT ALL
        List<Authority> authorities = authorityDao.findAll(username);
        int expectedFoundAuthorityCount = 1;
        Assert.assertEquals(expectedFoundAuthorityCount, authorities.size());
        AuthorityId authority = authorities.get(0).getId();
        Assert.assertEquals(username, authority.getUsername());
        Assert.assertEquals(RepositoryTestUtils.AUTHORITY_DEFAULT_TYPE.name(), authority.getAuthority());
    }

    @Test
    public void testSaveFindAllWithMoreAuthorities() throws Exception {
        AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // THREE INSERTS
        String firstUsername = this.defaultAuthor.getUsername();
        TEST_UTILS.createTestAuthority(firstUsername, AuthorityType.ROLE_REGISTERED_USER);
        String secondUsername = TEST_UTILS.createSecondDefaultTestUser().getUsername();
        TEST_UTILS.createTestAuthority(secondUsername, AuthorityType.ROLE_REGISTERED_USER);
        TEST_UTILS.createTestAuthority(firstUsername, AuthorityType.ROLE_MEMBER_OF_SV);

        // SELECT ALL
        List<Authority> authorities = authorityDao.findAll(firstUsername);
        int expectedFoundAuthorityCount = 2;
        Assert.assertEquals(expectedFoundAuthorityCount, authorities.size());
    }

    @Test
    public void testSaveFindAllWithSameAuthorities() throws Exception {
        // TWO INSERT
        String username = this.defaultAuthor.getUsername();
        TEST_UTILS.createDefaultTestAuthority(username);
        try {
            TEST_UTILS.createDefaultTestAuthority(username);
            Assert.fail("The identical authority exists already");
        } catch (DataAccessException ex) {
            // OK since duplications in authorities are not allowed.
            ;
        }
    }

    @Test
    public void testSaveMoreAtOneTime() throws Exception {
        AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // TWO INSERTS
        String firstUsername = this.defaultAuthor.getUsername();
        String secondUsername = TEST_UTILS.createSecondDefaultTestUser().getUsername();
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new Authority(firstUsername, AuthorityType.ROLE_REGISTERED_USER));
        authorities.add(new Authority(secondUsername, AuthorityType.ROLE_REGISTERED_USER));
        authorityDao.save(authorities);

        // SELECT ALL
        List<Authority> foundAuthorities = authorityDao.findAll(secondUsername);
        int expectedFoundAuthorityCount = 1;
        Assert.assertEquals(expectedFoundAuthorityCount, foundAuthorities.size());
    }

    @Test
    public void testDelete() throws Exception {
        AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // INSERT
        String username = this.defaultAuthor.getUsername();
        TEST_UTILS.createDefaultTestAuthority(username);

        // DELETE
        authorityDao.delete(username);

        // SELECT ALL
        List<Authority> foundAuthorities = authorityDao.findAll(username);
        int expectedFoundAuthorityCount = 0;
        Assert.assertEquals(expectedFoundAuthorityCount, foundAuthorities.size());
    }

    @Test
    public void testDeleteNoAuthority() throws Exception {
        AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // INSERT
        String username = this.defaultAuthor.getUsername();
        TEST_UTILS.createDefaultTestAuthority(username);
        String secondUsername = TEST_UTILS.createSecondDefaultTestUser().getUsername();

        // DELETE
        authorityDao.delete(secondUsername);

        // SELECT ALL
        List<Authority> foundAuthorities = authorityDao.findAll(username);
        int expectedFoundAuthorityCount = 1;
        Assert.assertEquals(expectedFoundAuthorityCount, foundAuthorities.size());
    }
}
