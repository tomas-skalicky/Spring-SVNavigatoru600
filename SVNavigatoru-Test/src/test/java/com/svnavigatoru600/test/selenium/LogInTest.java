package com.svnavigatoru600.test.selenium;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.svnavigatoru600.test.category.SeleniumTests;

/**
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
@Category(SeleniumTests.class)
public class LogInTest extends SeleniumTest {

    @Test
    public void testLogInLogOut() throws Exception {
        final WebDriver browserDriver = this.getBrowserDriver();

        final TestUser user = (TestUser) APPLICATION_CONTEXT.getBean("testUser");
        browserDriver.findElement(By.id("login")).sendKeys(user.getUsername());
        browserDriver.findElement(By.id("password")).sendKeys(user.getPassword());

        browserDriver.findElement(By.cssSelector("input[type='submit']")).click();
        String expectedUrlRegexp = ".*/uzivatelsky-ucet/";
        String actualUrl = browserDriver.getCurrentUrl();
        Assert.assertTrue(String.format("Actual location is '%s'", actualUrl), actualUrl.matches(expectedUrlRegexp));

        browserDriver.findElement(By.xpath("//a[@href='/j_spring_security_logout']")).click();

        // Waits for the page to load, timeout after 10 seconds
        (new WebDriverWait(browserDriver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                String expectedUrlRegexp = ".*/prihlaseni/";
                return driver.getCurrentUrl().matches(expectedUrlRegexp);
            }
        });
    }
}
