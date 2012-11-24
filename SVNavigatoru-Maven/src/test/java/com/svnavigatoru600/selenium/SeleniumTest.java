package com.svnavigatoru600.selenium;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Ancestor of all Selenium tests of the Viewer project.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class SeleniumTest {

	protected static final ApplicationContext APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(
			SeleniumAppConfig.class);

	private static WebDriver browserDriver = null;

	@BeforeClass
	public static void openBrowser() throws Exception {
		final Server seleniumServer = (Server) APPLICATION_CONTEXT.getBean("seleniumServer");
		// browserDriver = (new Augmenter()).augment(new RemoteWebDriver(new URL(seleniumServer.getUrl()),
		// DesiredCapabilities.firefox()));
		browserDriver = new FirefoxDriver();
	}

	@Before
	public void setUp() {
		final Server deployServer = (Server) APPLICATION_CONTEXT.getBean("deployServer");
		browserDriver.get(deployServer.getUrl());
	}

	@AfterClass
	public static void closeBrowser() {
		browserDriver.quit();
	}

	/**
	 * Returns the browser drive. When it is used the first time, the current page is the homepage.
	 */
	protected WebDriver getBrowserDriver() {
		return browserDriver;
	}

	/**
	 * The further processing of the caller test waits till either a timeout expires or the URL of the loaded page
	 * matches the given regular expression.
	 * 
	 * @param driver
	 * @param urlRegExp
	 * @return <code>true</code> if the URL of the loaded page matches the given regular expression; otherwise
	 *         <code>false</code>.
	 */
	protected boolean waitForPageUrl(WebDriver browserDriver, final String urlRegExp) {
		return (new WebDriverWait(browserDriver, WebDriverWait.DEFAULT_SLEEP_TIMEOUT,
				WebDriverWait.FIVE_HUNDRED_MILLIS.in(TimeUnit.MILLISECONDS))).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return driver.getCurrentUrl().matches(urlRegExp);
			}
		});
	}

	/**
	 * <p>
	 * Signs in to the application with the given credentials. It does not check the "remember me" checkbox. It does not
	 * test, whether the login ended up with a success, or not.
	 * </p>
	 * <b>PRECONDITION:</b> The current location is the login page of the application.
	 * 
	 * @param login
	 * @param password
	 */
	protected void logIn(final String login, final String password) {
		browserDriver.findElement(By.id("login")).sendKeys(login);
		browserDriver.findElement(By.id("password")).sendKeys(password);
		browserDriver.findElement(By.cssSelector("input[type='submit']")).click();
	}
}
