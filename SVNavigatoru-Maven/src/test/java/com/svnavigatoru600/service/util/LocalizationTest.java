package com.svnavigatoru600.service.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.test.category.UnitTests;

@Category(UnitTests.class)
public final class LocalizationTest {

    @Test
    public void testStripCzechDiacriticsOnWholeAlphabet() {
        String input = "a, á, b, c, č, d, ď, e, é, ě, f, g, h, ch, i, í, j, k, l, m, n, ň, o, ó, p, q, r, ř, s, š, t, ť, u, ú, ů, v, w, x, y, ý, z, ž";
        String expected = "a, a, b, c, c, d, d, e, e, e, f, g, h, ch, i, i, j, k, l, m, n, n, o, o, p, q, r, r, s, s, t, t, u, u, u, v, w, x, y, y, z, z";

        Assert.assertEquals(expected, Localization.stripCzechDiacritics(input));
    }

    /**
     * Expected result in this test is same as the input since non-standard ASCII characters (except czech
     * ones) are not translated.
     */
    @Test
    public void testStripCzechDiacriticsOnOtherNonStandardCharacters() {
        String input = "¾, ¿, À";

        Assert.assertEquals(input, Localization.stripCzechDiacritics(input));
    }
}
