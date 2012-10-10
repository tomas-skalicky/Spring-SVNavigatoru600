package com.gfk.ait.viewer.selenium;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.gfk.ait.viewer.test.category.SeleniumTests;

/**
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 * @version $Id: $
 */
@Category(SeleniumTests.class)
public class LogInTest extends SeleniumTest {

    private static final String APP_URL = "/ait-viewer/";

    @Test
    public void testViewerLogIn() throws Exception {
        this.selenium.open(APP_URL);

        final TestUser user = (TestUser) APPLICATION_CONTEXT.getBean("testUser");
        this.selenium.type("xpath=//input[@name='username']", user.getUsername());
        this.selenium.type("xpath=//input[@type='password']", user.getPassword());
        this.selenium.click("xpath=//input[@class='loginsubmit']");
        this.selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);

        String expectedLocationRegexp = String.format(".*%sload\\.do.*", APP_URL);
        String actualLocation = this.selenium.getLocation();
        Assert.assertTrue(String.format("Actual location is '%s'", actualLocation), actualLocation.matches(expectedLocationRegexp));
    }
}
