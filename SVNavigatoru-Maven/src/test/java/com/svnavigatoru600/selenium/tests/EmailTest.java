package com.svnavigatoru600.selenium.tests;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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

		this.logIn("vaclavas@ramacz.aa", "password");
		this.waitForPageUrl(browserDriver, ".*/uzivatelsky-ucet/");

		// Edits the email address of the logged-in user.
		browserDriver.findElement(By.linkText("Uživatelský účet")).click();
		this.waitForPageUrl(browserDriver, ".*/uzivatelsky-ucet/");
		// Adds a dash (i.e. "-") into the email.
		browserDriver.findElement(By.id("user.email")).sendKeys("vaclavas@rama-cz.aa");
		browserDriver.findElement(By.cssSelector("input[type='submit']")).click();
		this.waitForPageUrl(browserDriver, ".*/uzivatelsky-ucet/");

		browserDriver.findElement(By.linkText("Odhlásit se")).click();
		this.waitForPageUrl(browserDriver, ".*/prihlaseni/");

		this.logIn("vaclavas@rama-cz.aa", "password");
		this.waitForPageUrl(browserDriver, ".*/uzivatelsky-ucet/");

		// Sets up the original email address.
		browserDriver.findElement(By.linkText("Uživatelský účet")).click();
		this.waitForPageUrl(browserDriver, ".*/uzivatelsky-ucet/");
		// Removes a dash from the email.
		browserDriver.findElement(By.id("user.email")).sendKeys("vaclavas@ramacz.aa");
		browserDriver.findElement(By.cssSelector("input[type='submit']")).click();
		this.waitForPageUrl(browserDriver, ".*/uzivatelsky-ucet/");

		browserDriver.findElement(By.linkText("Odhlásit se")).click();
		this.waitForPageUrl(browserDriver, ".*/prihlaseni/");
	}
}
