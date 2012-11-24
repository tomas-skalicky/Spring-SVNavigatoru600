package com.svnavigatoru600.service.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import svnavigatoru.service.util.Hash;

import com.svnavigatoru600.selenium.EmailTest;
import com.svnavigatoru600.test.category.FastTests;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
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
