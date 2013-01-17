package com.svnavigatoru600.test.jsoup;

import junit.framework.Assert;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.test.AbstractAvailabilityTest;
import com.svnavigatoru600.test.Server;
import com.svnavigatoru600.test.category.FastTests;

/**
 * Contains tests verifying that the web app is running and that the login page is reachable.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
public class JsoupAvailabilityTest extends AbstractAvailabilityTest {

    /**
     * Is to be set to <code>svnavigatoru600.com:80/</code>
     */
    private String rootUrl = null;
    /**
     * Is to be set to <code>svnavigatoru600.com:80/prihlaseni/</code>
     */
    private String loginPage = null;

    @Before
    public void setUpUrls() {
        this.rootUrl = ((Server) APPLICATION_CONTEXT.getBean("deployServer")).getUrl();
        this.loginPage = this.rootUrl + "prihlaseni/";
    }

    @Test
    @Category(FastTests.class)
    public void testLoginPage() throws Exception {

        Document loginPage = Jsoup.connect(this.loginPage).get();

        Assert.assertEquals("Přihlášení :: SV Navigátorů 600-3", loginPage.title());
        Assert.assertEquals("Uživatelské jméno / Email:", loginPage.select("label[for~=login]").get(0).text());
    }
}
