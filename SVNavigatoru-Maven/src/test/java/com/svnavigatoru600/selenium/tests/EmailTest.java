package com.svnavigatoru600.selenium.tests;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.svnavigatoru600.selenium.AbstractSeleniumTest;
import com.svnavigatoru600.test.category.SeleniumTests;

/**
 * <code>Category</code> annotation is applicable both for classes and for methods. However,
 * <code>groups</code> of the <code>maven-surefire-plugin</code> plugin takes only those annotations into
 * account which are by <i>definitions of classes</i>, at least in the version 2.12.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 * @see http://maven.apache.org/plugins/maven-surefire-plugin/test-mojo.html#groups
 */
@Category(SeleniumTests.class)
public class EmailTest extends AbstractSeleniumTest {

    /**
     * The regular expression of URL of the login page.
     */
    private static final String LOGIN_PAGE_URL_REG_EXP = ".*/prihlaseni/";
    /**
     * The text of the link which logs out the user.
     */
    private static final String LOGOUT_LINK_TEXT = "Odhlásit se";
    /**
     * The regular expression of URL of the homepage.
     */
    private static final String HOMEPAGE_URL_REG_EXP = ".*/novinky/";
    /**
     * The text of the link to the user account page.
     */
    private static final String USER_ACCOUNT_LINK_TEXT = "Uživatelský účet";
    /**
     * The regular expression of URL of the user account page.
     */
    private static final String USER_ACCOUNT_PAGE_URL_REG_EXP = ".*/uzivatelsky-ucet/";
    /**
     * The regular expression of URL of the page which is shown when changes of user account have been
     * successfully saved.
     */
    private static final String USER_ACCOUNT_CHANGED_URL_REG_EXP = ".*/uzivatelsky-ucet/ulozeno/";
    /**
     * The CSS selector of the HTML element which submits changes of user account.
     */
    private static final String USER_ACCOUNT_CHANGES_SUBMIT_SELECTOR = "input[type='submit']";
    /**
     * The ID of the email element in the user account page.
     */
    private static final String EMAIL_ELEMENT_ID = "user.email";

    /**
     * @throws Exception
     *             If anything goes wrong.
     */
    @Test
    public void testEditEmail() throws Exception {
        final WebDriver browserDriver = this.getBrowserDriver();

        final String password = "password";
        final String originalEmail = "vaclavas@ramacz.aa";
        this.logIn(originalEmail, password);
        this.waitForPageUrl(browserDriver, HOMEPAGE_URL_REG_EXP);

        final String newEmail = "vaclavas@rama-cz.aa";
        this.changeEmail(newEmail, browserDriver);

        browserDriver.findElement(By.linkText(LOGOUT_LINK_TEXT)).click();
        this.waitForPageUrl(browserDriver, LOGIN_PAGE_URL_REG_EXP);

        this.logIn(newEmail, password);
        this.waitForPageUrl(browserDriver, HOMEPAGE_URL_REG_EXP);

        this.changeEmail(originalEmail, browserDriver);

        browserDriver.findElement(By.linkText(LOGOUT_LINK_TEXT)).click();
        this.waitForPageUrl(browserDriver, LOGIN_PAGE_URL_REG_EXP);
    }

    /**
     * Changes the email of the currently logged-in user to the given email.
     * 
     * @param newEmail
     *            The new email
     * @param browserDriver
     *            The web browser where the test is running
     */
    private void changeEmail(final String newEmail, final WebDriver browserDriver) {

        // Goes to the user account page.
        browserDriver.findElement(By.linkText(USER_ACCOUNT_LINK_TEXT)).click();
        this.waitForPageUrl(browserDriver, USER_ACCOUNT_PAGE_URL_REG_EXP);

        // Performs the change.
        final WebElement userEmailElement = browserDriver.findElement(By.id(EMAIL_ELEMENT_ID));
        userEmailElement.clear();
        userEmailElement.sendKeys(newEmail);

        // Confirms the change.
        browserDriver.findElement(By.cssSelector(USER_ACCOUNT_CHANGES_SUBMIT_SELECTOR)).click();
        this.waitForPageUrl(browserDriver, USER_ACCOUNT_CHANGED_URL_REG_EXP);
    }
}
