package com.svnavigatoru600.selenium;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Ancestor of all Selenium tests of the Viewer project.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
public abstract class SeleniumTest {

	protected static final ApplicationContext APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(
			SeleniumAppConfig.class);

	protected static final long DEFAULT_TIMEOUT_IN_SECONDS = WebDriverWait.DEFAULT_SLEEP_TIMEOUT;
	protected static final long DEFAULT_SLEEP_BETWEEN_POLLS_IN_MILLISECONDS = WebDriverWait.FIVE_HUNDRED_MILLIS
			.in(TimeUnit.MILLISECONDS);

	// private static final String PROXY = "emea-webproxy.gfk.com:3128";
	private static WebDriver browserDriver = null;

	@BeforeClass
	public static void openBrowser() throws Exception {
		final Server seleniumServer = (Server) APPLICATION_CONTEXT.getBean("seleniumServer");
		// Proxy proxy = new Proxy();
		// proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);
		// The proxy credentials have to be introduced if you want to log in. So, it is not running full-automatic.

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities(DesiredCapabilities.firefox());
		// desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);

		browserDriver = (new Augmenter()).augment(new RemoteWebDriver(new URL(seleniumServer.getUrl()),
				desiredCapabilities));
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
		return (new WebDriverWait(browserDriver, DEFAULT_TIMEOUT_IN_SECONDS,
				DEFAULT_SLEEP_BETWEEN_POLLS_IN_MILLISECONDS)).until(new ExpectedCondition<Boolean>() {
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
