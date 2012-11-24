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
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Category(SeleniumTests.class)
public class LogInTest extends SeleniumTest {

	@Test
	public void testLogInLogOut() throws Exception {
		final WebDriver browserDriver = this.getBrowserDriver();

		final TestUser user = (TestUser) APPLICATION_CONTEXT.getBean("testUser");
		browserDriver.findElement(By.id("login")).sendKeys(user.getUsername());
		browserDriver.findElement(By.id("password")).sendKeys(user.getPassword());

		browserDriver.findElement(By.cssSelector("input[type='submit']")).click();
		Assert.assertTrue(AssertUtils.getActualUrlReport(browserDriver),
				this.waitTillPageLoadAndTestIt(browserDriver, ".*/uzivatelsky-ucet/"));

		browserDriver.findElement(By.xpath("//a[@href='/j_spring_security_logout']")).click();
		Assert.assertTrue(AssertUtils.getActualUrlReport(browserDriver),
				this.waitTillPageLoadAndTestIt(browserDriver, ".*/prihlaseni/"));
	}
}
