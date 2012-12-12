package com.svnavigatoru600.selenium.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.svnavigatoru600.selenium.AssertUtils;
import com.svnavigatoru600.selenium.AbstractSeleniumTest;
import com.svnavigatoru600.selenium.TestUser;
import com.svnavigatoru600.test.category.SeleniumTests;

/**
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Category(SeleniumTests.class)
public class LogInTest extends AbstractSeleniumTest {

    /**
     * @throws Exception
     *             If something bad happens.
     */
    @Test
    public final void testLogInLogOut() throws Exception {
        final WebDriver browserDriver = this.getBrowserDriver();

        final TestUser user = (TestUser) APPLICATION_CONTEXT.getBean("testUser");
        this.logIn(user.getUsername(), user.getPassword());
        Assert.assertTrue(AssertUtils.getActualUrlReport(browserDriver),
                this.waitForPageUrl(browserDriver, ".*/uzivatelsky-ucet/"));

        browserDriver.findElement(By.xpath("//a[@href='/j_spring_security_logout']")).click();
        Assert.assertTrue(AssertUtils.getActualUrlReport(browserDriver),
                this.waitForPageUrl(browserDriver, ".*/prihlaseni/"));
    }
}
