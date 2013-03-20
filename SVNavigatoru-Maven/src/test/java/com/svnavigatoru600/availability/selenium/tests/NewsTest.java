package com.svnavigatoru600.availability.selenium.tests;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;

import com.svnavigatoru600.availability.selenium.AbstractTailoredSeleniumTest;
import com.svnavigatoru600.test.category.SeleniumTests;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 * @see EmailTest
 */
@Category(SeleniumTests.class)
// There are bugs.
public final class NewsTest extends AbstractTailoredSeleniumTest {

    private static final String XPATH_NEWS_SECTION = "//*[@id='nav']/li[1]/a[1]";

    private static final String XPATH_FIRST_NEWS_TITLE = "//*[@id='newsList']/*[@class='post'][1]//h3";
    private static final String XPATH_FIRST_NEWS_TEXT = "//*[@id='newsList']/*[@class='post'][1]/*[@class='post-content clearfix'][1]/p[1]";

    private static final String XPATH_NEW_NEWS_LINK = "//*[@id='newNewsLink']/a[1]";
    private static final String NEW_NEWS_TITLE_ID = "news.title";
    private static final String NEW_NEWS_TEXT_ID = "tinymce";
    private static final String NEW_NEWS_SUBMIT_ID = "newsForm.submitButton";

    private static final String XPATH_FIRST_NEWS_EDIT = "//*[@id='newsList']/*[@class='post'][1]//*[@class='controls'][1]/a[1]";
    private static final String EDIT_NEWS_TITLE_ID = "news.title";
    private static final String EDIT_NEWS_TEXT_ID = NEW_NEWS_TEXT_ID;
    private static final String EDIT_NEWS_SUBMIT_ID = NEW_NEWS_SUBMIT_ID;
    private static final String SUCCESS_EDIT_MESSAGE_ID = "successEditMessage";

    private static final String XPATH_FIRST_NEWS_DELETE = "//*[@id='newsList']/*[@class='post'][1]//*[@class='controls'][1]/a[2]";

    /**
     * @throws Exception
     *             If anything goes wrong.
     */
    @Ignore
    @Test
    public void testAddEditDelete() throws Exception {
        this.logIn();
        createNewNews();
        editNewNews();
        deleteNewNews();
        logOut();
    }

    /**
     * Signs in the application. It checks the remember-me checkbox.
     */
    private void logIn() {
        WebDriver browserDriver = getBrowserDriver();

        browserDriver.findElement(By.id(LOGIN_ELEMENT_ID)).sendKeys("skalicky.tomas@gmail.com");
        browserDriver.findElement(By.id(PASSWORD_ELEMENT_ID)).sendKeys("t");
        browserDriver.findElement(By.id("rememberMe")).click();
        browserDriver.findElement(By.cssSelector(SUBMIT_SELECTOR)).click();
        waitForPageUrl(browserDriver, HOMEPAGE_URL_REG_EXP);
    }

    /**
     * Creates an entirely new news.
     */
    private void createNewNews() {
        WebDriver browserDriver = getBrowserDriver();

        // HINT: Use a Firefox extension called "XPath Checker" to find out a xpath of a certain element.
        browserDriver.findElement(By.xpath(XPATH_NEW_NEWS_LINK)).click();
        Assert.isTrue((new WebDriverWait(browserDriver, DEFAULT_TIMEOUT_IN_SECONDS,
                DEFAULT_SLEEP_BETWEEN_POLLS_IN_MILLISECONDS)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return "Přidat novinku".equals(NewsTest.this.getAttributeValue(NEW_NEWS_SUBMIT_ID));
            }
        }));

        final String newTitle = "New News";
        browserDriver.findElement(By.id(NEW_NEWS_TITLE_ID)).sendKeys(newTitle);

        final String newText = "New Text";
        WebElement tinymceIframe = browserDriver.findElement(By.id(TINYMCE_IFRAME_ID));
        browserDriver.switchTo().frame(tinymceIframe);
        WebElement newsTextBox = browserDriver.findElement(By.id(NEW_NEWS_TEXT_ID));
        newsTextBox.sendKeys("<p>" + newTitle + "</p>");
        browserDriver.switchTo().defaultContent();

        browserDriver.findElement(By.cssSelector(SUBMIT_SELECTOR)).click();
        Assert.isTrue((new WebDriverWait(browserDriver, DEFAULT_TIMEOUT_IN_SECONDS,
                DEFAULT_SLEEP_BETWEEN_POLLS_IN_MILLISECONDS)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return newTitle.equals(NewsTest.this.getElementText(XPATH_FIRST_NEWS_TITLE))
                        && newText.equals(NewsTest.this.getElementText(XPATH_FIRST_NEWS_TEXT));
            }
        }));
    }

    /**
     * Makes a slight modification of the last inserted news record, i.e. the news which has been created by
     * the {@link #createNewNews() createNewNews} method.
     */
    private void editNewNews() {
        WebDriver browserDriver = getBrowserDriver();

        browserDriver.findElement(By.xpath(XPATH_FIRST_NEWS_EDIT)).click();
        Assert.isTrue((new WebDriverWait(browserDriver, DEFAULT_TIMEOUT_IN_SECONDS,
                DEFAULT_SLEEP_BETWEEN_POLLS_IN_MILLISECONDS)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return "Uložit změny".equals(NewsTest.this.getAttributeValue(EDIT_NEWS_SUBMIT_ID));
            }
        }));

        String newTitle = "Edited News";
        browserDriver.findElement(By.id(EDIT_NEWS_TITLE_ID)).sendKeys(newTitle);

        String newTextWithFormating = "<p>Edited Text</p>";
        WebElement tinymceIframe = browserDriver.findElement(By.id(TINYMCE_IFRAME_ID));
        browserDriver.switchTo().frame(tinymceIframe);
        WebElement newsTextBox = browserDriver.findElement(By.id(EDIT_NEWS_TEXT_ID));
        newsTextBox.sendKeys(newTextWithFormating);
        browserDriver.switchTo().defaultContent();

        browserDriver.findElement(By.cssSelector(SUBMIT_SELECTOR)).click();
        Assert.isTrue((new WebDriverWait(browserDriver, DEFAULT_TIMEOUT_IN_SECONDS,
                DEFAULT_SLEEP_BETWEEN_POLLS_IN_MILLISECONDS)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.findElement(By.id(SUCCESS_EDIT_MESSAGE_ID)).isDisplayed();
            }
        }));

        browserDriver.findElement(By.xpath(XPATH_NEWS_SECTION)).click();
        waitForPageUrl(browserDriver, HOMEPAGE_URL_REG_EXP);
    }

    /**
     * Deletes the last inserted news record, i.e. the news which has been created by the
     * {@link #createNewNews() createNewNews} method.
     */
    private void deleteNewNews() {
        WebDriver browserDriver = getBrowserDriver();

        browserDriver.findElement(By.xpath(XPATH_FIRST_NEWS_DELETE)).click();
        // browserDriver.waitForCondition("selenium.isConfirmationPresent()", MAX_WAIT_TIME_IN_MS);
        // String confirmation = browserDriver.getConfirmation();
        // assertTrue("The actual value is: " + confirmation,
        // confirmation.matches("^Opravdu chcete smazat novinku `Edited News`\\?$"));
    }

    /**
     * Logs out from the application.
     */
    private void logOut() {
        WebDriver browserDriver = getBrowserDriver();

        browserDriver.findElement(By.linkText(LOGOUT_LINK_TEXT)).click();
        waitForPageUrl(browserDriver, LOGIN_PAGE_URL_REG_EXP);
    }
}
