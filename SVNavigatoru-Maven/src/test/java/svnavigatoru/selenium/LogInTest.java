package svnavigatoru.selenium;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import svnavigatoru.test.category.SeleniumTests;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a> &lt;skalicky.tomas@gmail.com&gt;
 * @see EmailTest
 */
@Category(SeleniumTests.class)
public class LogInTest extends AbstractTestCase {

	@Test
	public void testSimpleLogInLogOut() throws Exception {
		selenium.open("/SVNavigatoru/prihlaseni/;jsessionid=AB05305CE81BA007FCB9C28288DE2BD9");
		selenium.type("id=login", "skalicky.tomas@gmail.com");
		selenium.type("id=password", "t");
		selenium.click("css=input[type='submit']");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
		selenium.click("link=Odhlásit se");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
	}
}
