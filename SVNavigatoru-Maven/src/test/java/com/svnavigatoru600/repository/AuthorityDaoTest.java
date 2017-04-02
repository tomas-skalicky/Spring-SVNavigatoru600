package com.svnavigatoru600.repository;

import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.GrantedAuthority;

import com.google.common.collect.Lists;
import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityId;
import com.svnavigatoru600.domain.users.AuthorityTypeEnum;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.AuthorityDao;
import com.svnavigatoru600.test.category.PersistenceTests;

/**
 * Tests methods of the {@link AuthorityDao} interface.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public final class AuthorityDaoTest extends AbstractRepositoryTest {

    /**
     * Default test author of threads and contributions.
     */
    private User defaultAuthor = null;

    @Before
    public void createDefaultAuthor() {
        defaultAuthor = TEST_UTILS.createDefaultTestUser();
    }

    @Test
    public void testSaveFindAllWithOneAuthority() throws Exception {
        final AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // INSERT
        final String username = defaultAuthor.getUsername();
        TEST_UTILS.createDefaultTestAuthority(username);

        // SELECT ALL
        final List<Authority> authorities = authorityDao.findByUsername(username);
        final int expectedFoundAuthorityCount = 1;
        Assert.assertEquals(expectedFoundAuthorityCount, authorities.size());
        final AuthorityId authority = authorities.get(0).getId();
        Assert.assertEquals(username, authority.getUsername());
        Assert.assertEquals(RepositoryTestUtils.AUTHORITY_DEFAULT_TYPE.name(), authority.getAuthority());
    }

    @Test
    public void testSaveFindAllWithMoreAuthorities() throws Exception {
        final AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // THREE INSERTS
        final String firstUsername = defaultAuthor.getUsername();
        TEST_UTILS.createTestAuthority(firstUsername, AuthorityTypeEnum.ROLE_REGISTERED_USER);
        final String secondUsername = TEST_UTILS.createSecondDefaultTestUser().getUsername();
        TEST_UTILS.createTestAuthority(secondUsername, AuthorityTypeEnum.ROLE_REGISTERED_USER);
        TEST_UTILS.createTestAuthority(firstUsername, AuthorityTypeEnum.ROLE_MEMBER_OF_SV);

        // SELECT ALL
        final List<Authority> authorities = authorityDao.findByUsername(firstUsername);
        final int expectedFoundAuthorityCount = 2;
        Assert.assertEquals(expectedFoundAuthorityCount, authorities.size());
    }

    @Test(expected = DuplicateKeyException.class)
    public void testSaveFindAllWithSameAuthorities() throws Exception {
        // TWO INSERT
        final String username = defaultAuthor.getUsername();
        TEST_UTILS.createDefaultTestAuthority(username);
        // Throws an exception since duplications in authorities are not allowed.
        TEST_UTILS.createDefaultTestAuthority(username);
    }

    @Test
    public void testSaveMoreAtOneTime() throws Exception {
        final AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // TWO INSERTS
        final String firstUsername = defaultAuthor.getUsername();
        final String secondUsername = TEST_UTILS.createSecondDefaultTestUser().getUsername();
        final List<GrantedAuthority> authorities = Lists.newArrayList();
        authorities.add(new Authority(firstUsername, AuthorityTypeEnum.ROLE_REGISTERED_USER));
        authorities.add(new Authority(secondUsername, AuthorityTypeEnum.ROLE_REGISTERED_USER));
        authorityDao.save(authorities);

        // SELECT ALL
        final List<Authority> foundAuthorities = authorityDao.findByUsername(secondUsername);
        final int expectedFoundAuthorityCount = 1;
        Assert.assertEquals(expectedFoundAuthorityCount, foundAuthorities.size());
    }

    @Test
    public void testDelete() throws Exception {
        final AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // INSERT
        final String username = defaultAuthor.getUsername();
        TEST_UTILS.createDefaultTestAuthority(username);

        // DELETE
        authorityDao.delete(username);

        // SELECT ALL
        final List<Authority> foundAuthorities = authorityDao.findByUsername(username);
        final int expectedFoundAuthorityCount = 0;
        Assert.assertEquals(expectedFoundAuthorityCount, foundAuthorities.size());
    }

    @Test
    public void testDeleteNoAuthority() throws Exception {
        final AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // INSERT
        final String username = defaultAuthor.getUsername();
        TEST_UTILS.createDefaultTestAuthority(username);
        final String secondUsername = TEST_UTILS.createSecondDefaultTestUser().getUsername();

        // DELETE
        authorityDao.delete(secondUsername);

        // SELECT ALL
        final List<Authority> foundAuthorities = authorityDao.findByUsername(username);
        final int expectedFoundAuthorityCount = 1;
        Assert.assertEquals(expectedFoundAuthorityCount, foundAuthorities.size());
    }

    @Test
    public void testDeleteSingleAuthority() {
        final AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // TWO INSERTS
        final String username = defaultAuthor.getUsername();
        TEST_UTILS.createTestAuthority(username, RepositoryTestUtils.AUTHORITY_DEFAULT_TYPE);
        TEST_UTILS.createTestAuthority(username, RepositoryTestUtils.SECOND_AUTHORITY_DEFAULT_TYPE);

        // DELETE
        authorityDao.delete(username, RepositoryTestUtils.AUTHORITY_DEFAULT_TYPE);

        // SELECT ALL
        final List<Authority> foundAuthorities = authorityDao.findByUsername(username);
        final int expectedFoundAuthorityCount = 1;
        Assert.assertEquals(expectedFoundAuthorityCount, foundAuthorities.size());
        final Collection<String> foundAuthorityTypes = TEST_UTILS.extractAuthorityTypes(foundAuthorities);
        Assert.assertTrue(foundAuthorityTypes.contains(RepositoryTestUtils.SECOND_AUTHORITY_DEFAULT_TYPE.name()));
    }

    @Test
    public void testDeleteNotExistingAuthority() {
        final AuthorityDao authorityDao = TEST_UTILS.getAuthorityDao();

        // INSERT & DELETE
        final String username = defaultAuthor.getUsername();
        final AuthorityTypeEnum type = RepositoryTestUtils.AUTHORITY_DEFAULT_TYPE;
        TEST_UTILS.createTestAuthority(username, type);
        authorityDao.delete(username, type);

        // DELETE
        authorityDao.delete(username, type);
        // OK since no exception is thrown when the authority had not existed before.
    }
}
