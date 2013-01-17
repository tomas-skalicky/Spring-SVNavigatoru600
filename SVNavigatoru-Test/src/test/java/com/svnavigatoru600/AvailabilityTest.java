package com.svnavigatoru600;

import junit.framework.Assert;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 * Tests verifying that the web app is running and the login page is reachable.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
public class AvailabilityTest {

    private static final String DOMAIN_NAME = "http://www.svnavigatoru600.com";
    private static final String LOGIN_PAGE = AvailabilityTest.DOMAIN_NAME + "/prihlaseni/";

    @Test
    public void testLoginPage() throws Exception {

        Document loginPage = Jsoup.connect(LOGIN_PAGE).get();

        Assert.assertEquals("Přihlášení :: SV Navigátorů 600-3", loginPage.title());
        Assert.assertEquals("Uživatelské jméno / Email:", loginPage.select("label[for~=login]").get(0).text());
    }
}
