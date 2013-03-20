package com.svnavigatoru600.availability.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.service.Configuration;
import com.svnavigatoru600.test.category.JsoupTests;

/**
 * Contains tests verifying that the web app is running and that the login page is reachable.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Category(JsoupTests.class)
public final class JsoupAvailabilityTest {

    /**
     * Is to be set to {@code www.svnavigatoru600.com}.
     */
    private static final String LOGIN_PAGE = Configuration.DEFAULT_PROTOCOL + Configuration.DOMAIN;

    @Test
    public void testLoginPage() throws Exception {

        Document loginPageHtml = Jsoup.connect(LOGIN_PAGE).get();

        Assert.assertEquals("Získat zapomenuté heslo",
                loginPageHtml.select("a[href~=/prihlaseni/zapomenute-heslo/]").get(0).text());
    }
}
