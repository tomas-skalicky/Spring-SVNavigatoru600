package com.svnavigatoru600.common.config;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.svnavigatoru600.test.category.IntegrationTests;

/**
 * @author Tomas Skalicky
 * @since 07.05.2017
 */
@Category(IntegrationTests.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class AppConfigTest {

    @Test
    public void testContextStart() {
    }

}
