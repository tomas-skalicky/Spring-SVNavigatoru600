package com.svnavigatoru600.selenium;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
	 * Returns the browser drive which has already loaded the default page.
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
	protected boolean waitTillPageLoadAndTestIt(WebDriver browserDriver, final String urlRegExp) {
		return (new WebDriverWait(browserDriver, WebDriverWait.DEFAULT_SLEEP_TIMEOUT))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver driver) {
						return driver.getCurrentUrl().matches(urlRegExp);
					}
				});
	}
}
