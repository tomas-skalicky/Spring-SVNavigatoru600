package com.svnavigatoru600.selenium.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.svnavigatoru600.selenium.SeleniumTest;
import com.svnavigatoru600.test.category.SeleniumTests;

/**
 * <code>Category</code> annotation is applicable both for classes and for methods. However, <code>groups</code> of the
 * <code>maven-surefire-plugin</code> plugin takes only those annotations into account which are by <i>definitions of
 * classes</i>, at least in the version 2.12.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 * @see http://maven.apache.org/plugins/maven-surefire-plugin/test-mojo.html#groups
 */
@Category(SeleniumTests.class)
public class EmailTest extends SeleniumTest {

	@Test
	public void testEditEmail() throws Exception {
		final WebDriver browserDriver = this.getBrowserDriver();

		// Logs in.
		browserDriver.findElement(By.id("login")).sendKeys("vaclavas@ramacz.aa");
		browserDriver.findElement(By.id("password")).sendKeys("password");
		browserDriver.findElement(By.cssSelector("input[type='submit']")).click();

        // Waits for the page to load. Timeout is WAIT_LIMIT seconds.
        (new WebDriverWait(browserDriver, WAIT_LIMIT)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                String expectedUrlRegexp = ".*/uzivatelsky-ucet/";
                return driver.getCurrentUrl().matches(expectedUrlRegexp);
            }
        });

		// Edits the email address of the logged-in user.
		browserDriver.findElement(By.link("Uživatelský účet")).click();
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
		// Adds a dash (i.e. "-") in the address.
		browserDriver.findElement(By.id("user.email")).sendKeys("vaclavas@rama-cz.aa");
		browserDriver.findElement(By.cssSelector("input[type='submit']")).click();
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);

		// Logs out.
		browserDriver.findElement(By.link("Odhlásit se")).click();;
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);

		// Logs in.
		browserDriver.findElement(By.id("login", "vaclavas@rama-cz.aa");
		browserDriver.findElement(By.id("password")).sendKeys("password");
		browserDriver.findElement(By.cssSelector("input[type='submit']")).click();
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);

		// Sets up the original email address.
		selenium.click("link=Uživatelský účet");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
		browserDriver.findElement(By.id("user.email")).sendKeys("vaclavas@ramacz.aa");
		browserDriver.findElement(By.cssSelector("input[type='submit']")).click();
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);

		// Logs out.
		browserDriver.findElement(By.link("Odhlásit se")).click();

        // Waits for the page to load. Timeout is WAIT_LIMIT seconds.
        (new WebDriverWait(browserDriver, WAIT_LIMIT)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                String expectedUrlRegexp = ".*/prihlaseni/";
                return driver.getCurrentUrl().matches(expectedUrlRegexp);
            }
        });
	}
}
