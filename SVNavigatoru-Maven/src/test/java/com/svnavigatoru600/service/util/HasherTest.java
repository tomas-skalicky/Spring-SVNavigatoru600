package com.svnavigatoru600.service.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.test.category.UnitTests;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 * @see com.svnavigatoru600.availability.selenium.tests.EmailTest EmailTest
 */
@Category(UnitTests.class)
public final class HasherTest {

    /**
     * Tests whether the {@link Hash#doSha1Hashing(String) doSha1Hashing} utility function works properly.
     */
    @Test
    public void testDoSha1Hashing() {
        String stringToHash = "tomspassword";
        String expected = "e1d06df3f843c11e857fa844a5dd00f45808bd1b";
        Assert.assertEquals(expected, Hash.doSha1Hashing(stringToHash));
    }
}
