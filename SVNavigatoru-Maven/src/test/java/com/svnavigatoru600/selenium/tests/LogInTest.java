package com.svnavigatoru600.selenium.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.svnavigatoru600.selenium.AssertUtils;
import com.svnavigatoru600.selenium.SeleniumTest;
import com.svnavigatoru600.selenium.TestUser;
import com.svnavigatoru600.test.category.SeleniumTests;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 * @see EmailTest
 */
@Category(SeleniumTests.class)
public class LogInTest extends SeleniumTest {

	@Test
	public void testLogInLogOut() throws Exception {
		final WebDriver browserDriver = this.getBrowserDriver();

		final TestUser user = (TestUser) APPLICATION_CONTEXT.getBean("testUser");
		this.logIn(user.getUsername(), user.getPassword());
		Assert.assertTrue(AssertUtils.getActualUrlReport(browserDriver),
				this.waitForPageUrl(browserDriver, ".*/uzivatelsky-ucet/"));

		browserDriver.findElement(By.linkText("Odhlásit se")).click();
		Assert.assertTrue(AssertUtils.getActualUrlReport(browserDriver),
				this.waitForPageUrl(browserDriver, ".*/prihlaseni/"));
	}
}
