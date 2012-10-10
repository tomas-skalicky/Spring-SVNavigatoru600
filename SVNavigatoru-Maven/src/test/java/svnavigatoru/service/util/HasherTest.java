package svnavigatoru.service.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import svnavigatoru.selenium.EmailTest;
import svnavigatoru.test.category.FastTests;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a> &lt;skalicky.tomas@gmail.com&gt;@see EmailTest
 * @see EmailTest
 */
@Category(FastTests.class)
public class HasherTest {

	@Test
	public void testDoSha1Hashing() {
		String stringToHash = "tomspassword";
		String expected = "e1d06df3f843c11e857fa844a5dd00f45808bd1b";
		assertEquals(expected, Hash.doSha1Hashing(stringToHash));
	}
}
