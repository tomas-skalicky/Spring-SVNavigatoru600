package com.svnavigatoru600.service.users;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.test.category.UnitTests;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(UnitTests.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest(Localization.class)
@PowerMockIgnore(value = "javax.management.*")
@ContextConfiguration(locations = { "classpath:spring/applicationContext-business.xml" })
public class AuthorityServiceTest {

    private TestContextManager testContextManager;

    @Before
    public void setUpContext() throws Exception {
        // This is where the magic happens. We actually do "by hand" what the spring runner would do for us.
        this.testContextManager = new TestContextManager(this.getClass());
        this.testContextManager.prepareTestInstance(this);
    }

    @Inject
    private AuthorityService authorityService;

    @Before
    @CacheEvict("localizedRoleTitles")
    public void evictCache() {
    }

    @Test
    public void testGetLocalizedRoleTitlesCaching() throws Exception {
        PowerMockito.mockStatic(Localization.class);

        for (AuthorityType type : AuthorityType.values()) {
            when(Localization.findLocaleMessage(any(MessageSource.class), any(HttpServletRequest.class),
                    eq(type.getTitleLocalizationCode()))).thenReturn(type.getTitleLocalizationCode());
        }

        // Calls the tested method the first time.
        Map<Long, String> localizedRoleTitles = this.authorityService.getLocalizedRoleTitles(null, null);
        assertEquals(AuthorityType.ROLE_MEMBER_OF_BOARD.getTitleLocalizationCode(),
                localizedRoleTitles.get(AuthorityType.ROLE_MEMBER_OF_BOARD.getOrdinal()));

        // Calls the same method the second time.
        Map<Long, String> cachedLocalizedRoleTitles = this.authorityService.getLocalizedRoleTitles(null, null);

        // Checks the cache instance.
        assertEquals(localizedRoleTitles, cachedLocalizedRoleTitles);
    }
}
