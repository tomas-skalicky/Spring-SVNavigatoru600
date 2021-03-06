package com.svnavigatoru600.availability.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.svnavigatoru600.web.url.CommonUrlParts;
import com.svnavigatoru600.web.url.LoginUrlParts;
import com.svnavigatoru600.web.url.news.NewsUrlParts;
import com.svnavigatoru600.web.url.users.UserAccountUrlParts;

/**
 * Appends to the {@link AbstractSeleniumTest} class useful constants and methods which are application-specific.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractTailoredSeleniumTest extends AbstractSeleniumTest {

    /**
     * The CSS selector of the HTML element which submits the current form.
     */
    protected static final String SUBMIT_SELECTOR = "input[type='submit']";

    /**
     * The ID of the tinymce iframe which is used for modifications of texts.
     */
    protected static final String TINYMCE_IFRAME_ID = "news.text_ifr";

    /**
     * The regular expression of URL of the login page.
     */
    protected static final String LOGIN_PAGE_URL_REG_EXP = ".*" + LoginUrlParts.BASE_URL;
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
    protected static final String HOMEPAGE_URL_REG_EXP = ".*" + NewsUrlParts.BASE_URL;

    /**
     * The text of the link to the user account page.
     */
    protected static final String USER_ACCOUNT_LINK_TEXT = "Uživatelský účet";
    /**
     * The regular expression of URL of the user account page.
     */
    protected static final String USER_ACCOUNT_PAGE_URL_REG_EXP = ".*" + UserAccountUrlParts.BASE_URL;
    /**
     * The regular expression of URL of the page which is shown when changes of user account have been successfully
     * saved.
     */
    protected static final String USER_ACCOUNT_CHANGED_URL_REG_EXP = USER_ACCOUNT_PAGE_URL_REG_EXP
            + CommonUrlParts.SAVED_EXTENSION;
    /**
     * The ID of the email element in the user account page.
     */
    protected static final String EMAIL_ELEMENT_ID = "user.email";

    /**
     * Signs in to the application with the given credentials. It does not check the "remember me" checkbox. It does not
     * test, whether the login ended up with a success, or not.
     * <p>
     * <b>PRECONDITION:</b> The current location is the login page of the application.
     * 
     * @param login
     *            The username which is going to be used for sign-in
     * @param password
     *            The password which is going to be used for sign-in
     */
    protected void logIn(String login, String password) {
        WebDriver browserDriver = getBrowserDriver();
        browserDriver.findElement(By.id(LOGIN_ELEMENT_ID)).sendKeys(login);
        browserDriver.findElement(By.id(PASSWORD_ELEMENT_ID)).sendKeys(password);
        browserDriver.findElement(By.cssSelector(SUBMIT_SELECTOR)).click();
    }
}
