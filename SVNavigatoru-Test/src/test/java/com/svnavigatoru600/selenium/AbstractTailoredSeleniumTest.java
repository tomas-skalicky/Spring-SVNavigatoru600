package com.svnavigatoru600.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Appends to the {@link AbstractSeleniumTest} class useful constants and methods which are
 * application-specific.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractTailoredSeleniumTest extends AbstractSeleniumTest {

    /**
     * The CSS selector of the HTML element which submits the current form.
     */
    protected static final String SUBMIT_SELECTOR = "input[type='submit']";

    /**
     * The regular expression of URL of the login page.
     */
    protected static final String LOGIN_PAGE_URL_REG_EXP = ".*/prihlaseni/";
    /**
     * The ID of the login/username element in the login page.
     */
    protected static final String LOGIN_ELEMENT_ID = "login";
    /**
     * The ID of the password element in the login page.
     */
    protected static final String PASSWORD_ELEMENT_ID = "password";
    /**
     * The text of the link which logs out the user.
     */
    protected static final String LOGOUT_LINK_TEXT = "Odhlásit se";

    /**
     * The regular expression of URL of the homepage.
     */
    protected static final String HOMEPAGE_URL_REG_EXP = ".*/novinky/";

    /**
     * The regular expression of URL of the user account page.
     */
    protected static final String USER_ACCOUNT_PAGE_URL_REG_EXP = ".*/uzivatelsky-ucet/";

    /**
     * <p>
     * Signs in to the application with the given credentials. It does not check the "remember me" checkbox.
     * It does not test, whether the login ended up with a success, or not.
     * </p>
     * <b>PRECONDITION:</b> The current location is the login page of the application.
     * 
     * @param login
     *            The username which is going to be used for sign-in
     * @param password
     *            The password which is going to be used for sign-in
     */
    protected void logIn(final String login, final String password) {
        final WebDriver browserDriver = this.getBrowserDriver();
        browserDriver.findElement(By.id(LOGIN_ELEMENT_ID)).sendKeys(login);
        browserDriver.findElement(By.id(PASSWORD_ELEMENT_ID)).sendKeys(password);
        browserDriver.findElement(By.cssSelector(SUBMIT_SELECTOR)).click();
    }
}
