package com.svnavigatoru600.test.selenium;

import java.net.BindException;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.server.SeleniumServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.thoughtworks.selenium.SeleneseTestCase;

/**
 * Ancestor of all Selenium tests.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
abstract class SeleniumTest extends SeleneseTestCase {

	protected static final ApplicationContext APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(
			SeleniumAppConfig.class);

	// -------------------------------------------------------------------
	// Firefox: it does not work with "-P Selenium" option
	// private static final String BROWSER =
	// "*custom c:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe";
	// -------------------------------------------------------------------
	private static final String BROWSER = "*googlechrome";
	protected static final String MAX_WAIT_TIME_IN_MS = "30000";
	private SeleniumServer seleniumServer;

	@Before
	public void setUp() throws Exception {
		// -------------------------------------------------------------------
		// For running Firefox, start the Selenium server in the command line:
		//
		// java -jar selenium-server.jar -firefoxProfileTemplate
		// "c:\Users\tom\AppData\Roaming\Mozilla\Firefox\Profiles\k2256gca.Selenium"
		//
		// However, it has NOT been WORKING properly till now.
		// -------------------------------------------------------------------
		try {
			this.seleniumServer = new SeleniumServer();
			this.seleniumServer.start();
		} catch (BindException ex) {
			// Selenium is already running.
			this.seleniumServer = null;
		}

		// Firefox profile has to be created
		// (see
		// http://girliemangalo.wordpress.com/2009/02/05/creating-firefox-profile-for-your-selenium-rc-tests/)
		final DeployServer server = (DeployServer) APPLICATION_CONTEXT.getBean("deployServer");
		super.setUp(server.getUrl(), BROWSER);
	}

	@After
	public void tearDown() throws Exception {
		this.selenium.stop();
		if (this.seleniumServer != null) {
			this.seleniumServer.stop();
		}

		super.tearDown();
	}
}
