package com.svnavigatoru600.service.util;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.test.category.FastTests;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(FastTests.class)
public class UrlTest {

    @Test
    public void testConvertImageRelativeUrlsToAbsolute() {
        String firstRelativeUrl = "/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-undecided.gif";
        String secondRelativeUrl = "/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-smile.gif";

        final String textPatter = "A src=\"%s\" B src=%s\" C src=\"%s\" D ";
        String originalText = String.format(textPatter, firstRelativeUrl, secondRelativeUrl,
                secondRelativeUrl);
        String webPageDomain = "www.domain123.cz";

        String expectedText = String.format(textPatter, webPageDomain + firstRelativeUrl, secondRelativeUrl,
                webPageDomain + secondRelativeUrl);

        String actualText = Url.convertImageRelativeUrlsToAbsolute(originalText, webPageDomain);
        Assert.assertEquals(expectedText, actualText);
    }
}
