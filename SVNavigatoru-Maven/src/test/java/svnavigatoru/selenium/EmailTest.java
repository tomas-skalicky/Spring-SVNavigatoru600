package svnavigatoru.selenium;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import svnavigatoru.test.category.SeleniumTests;

/**
 * <code>Category</code> annotation is applicable both for classes and for methods. However, <code>groups</code> of the
 * <code>maven-surefire-plugin</code> plugin takes only those annotations into account which are by <i>definitions of
 * classes</i>, at least in the version 2.12.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a> &lt;skalicky.tomas@gmail.com&gt;
 * @see http://maven.apache.org/plugins/maven-surefire-plugin/test-mojo.html#groups
 */
@Category(SeleniumTests.class)
public class EmailTest extends AbstractTestCase {

	@Test
	public void testEditEmail() throws Exception {

		// Logs in.
		selenium.open("/SVNavigatoru/prihlaseni/");
		selenium.type("id=login", "vaclavas@ramacz.aa");
		selenium.type("id=password", "password");
		selenium.click("css=input[type='submit']");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);

		// Edits the email address of the logged-in user.
		selenium.click("link=Uživatelský účet");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
		// Adds a dash (i.e. "-") in the address.
		selenium.type("id=user.email", "vaclavas@rama-cz.aa");
		selenium.click("css=input[type='submit']");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);

		// Logs out.
		selenium.click("link=Odhlásit se");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);

		// Logs in.
		selenium.type("id=login", "vaclavas@rama-cz.aa");
		selenium.type("id=password", "password");
		selenium.click("css=input[type='submit']");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);

		// Sets up the original email address.
		selenium.click("link=Uživatelský účet");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
		selenium.type("id=user.email", "vaclavas@ramacz.aa");
		selenium.click("css=input[type='submit']");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);

		// Logs out.
		selenium.click("link=Odhlásit se");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
	}
}
