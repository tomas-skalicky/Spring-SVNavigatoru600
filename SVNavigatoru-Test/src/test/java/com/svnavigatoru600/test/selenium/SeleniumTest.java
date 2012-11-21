package com.svnavigatoru600.test.selenium;

import java.net.URL;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Ancestor of all Selenium tests of the Viewer project.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
public abstract class SeleniumTest {

    protected static final ApplicationContext APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(SeleniumAppConfig.class);

    private static final String PROXY = "emea-webproxy.gfk.com:3128";
    private static WebDriver browserDriver = null;

    @BeforeClass
    public static void openBrowser() throws Exception {
        final Server seleniumServer = (Server) APPLICATION_CONTEXT.getBean("seleniumServer");
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);
        // The proxy credentials have to be introduced if you want to log in. So, it is not running full-automatic.

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities(DesiredCapabilities.firefox());
        desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);

        browserDriver = (new Augmenter()).augment(new RemoteWebDriver(new URL(seleniumServer.getUrl()), desiredCapabilities));
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

    protected WebDriver getBrowserDriver() {
        return browserDriver;
    }
}
