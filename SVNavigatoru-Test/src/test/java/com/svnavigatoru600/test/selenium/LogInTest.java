package com.svnavigatoru600.test.selenium;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.test.category.SeleniumTests;

/**
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Category(SeleniumTests.class)
public class LogInTest extends SeleniumTest {

	@Test
	public void testViewerLogIn() throws Exception {
		selenium.open("/prihlaseni/");

		final TestUser user = (TestUser) APPLICATION_CONTEXT.getBean("testUser");
		selenium.type("id=login", user.getUsername());
		selenium.type("id=password", user.getPassword());
		selenium.click("css=input[type='submit']");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
		selenium.click("link=Odhlásit se");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
	}
}
