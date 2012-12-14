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
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractSeleniumTest {

    /**
     * Context which provide us with the access to Beans of servers, test user and so on.
     */
    protected static final ApplicationContext APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(
            SeleniumAppConfig.class);

    /**
     * Time in seconds how long in total the browser may wait at most. If the given expectations are fulfilled
     * in between, a method ends up with a success; otherwise with a failure.
     */
    protected static final long DEFAULT_TIMEOUT_IN_SECONDS = 10;
    /**
     * Time in milliseconds how long the browser waits till it tests the given expectations again.
     */
    protected static final long DEFAULT_SLEEP_BETWEEN_POLLS_IN_MILLISECONDS = WebDriverWait.FIVE_HUNDRED_MILLIS
            .in(TimeUnit.MILLISECONDS);

    // private static final String PROXY = "emea-webproxy.gfk.com:3128";
    /**
     * The simulation of a web browser.
     */
    private static WebDriver browserDriver = null;

    /**
     * Before all tests, initialises the web browser. Afterwards, the browser is prepared to serve for tests.
     * 
     * @throws Exception
     *             If anything goes wrong.
     */
    @BeforeClass
    public static void openBrowser() throws Exception {
        final Server seleniumServer = (Server) APPLICATION_CONTEXT.getBean("seleniumServer");
        // Proxy proxy = new Proxy();
        // proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);
        // The proxy credentials have to be introduced if you want to log in.
        // So, it is not running full-automatic.

        final DesiredCapabilities desiredCapabilities = new DesiredCapabilities(DesiredCapabilities.firefox());
        // desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);

        browserDriver = (new Augmenter()).augment(new RemoteWebDriver(new URL(seleniumServer.getUrl()),
                desiredCapabilities));
    }

    /**
     * Before each test, loads the default page of the application.
     */
    @Before
    public void setUp() {
        final Server deployServer = (Server) APPLICATION_CONTEXT.getBean("deployServer");
        browserDriver.get(deployServer.getUrl());
    }

    /**
     * After all tests, releases the web browser.
     */
    @AfterClass
    public static void closeBrowser() {
        browserDriver.quit();
    }

    /**
     * Returns the browser drive. When it is used the first time, the current page is the homepage.
     * 
     * @return The web browser
     */
    protected WebDriver getBrowserDriver() {
        return browserDriver;
    }

    /**
     * The further processing of the caller test waits till either a timeout expires or the URL of the loaded
     * page matches the given regular expression.
     * 
     * @param browserDriver
     *            The web browser where the test is running
     * @param urlRegExp
     *            The regular expression which is being tested
     * @return <code>true</code> if the URL of the loaded page matches the given regular expression; otherwise
     *         <code>false</code>.
     */
    protected boolean waitForPageUrl(final WebDriver browserDriver, final String urlRegExp) {
        return (new WebDriverWait(browserDriver, DEFAULT_TIMEOUT_IN_SECONDS,
                DEFAULT_SLEEP_BETWEEN_POLLS_IN_MILLISECONDS)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                return driver.getCurrentUrl().matches(urlRegExp);
            }
        });
    }
}
