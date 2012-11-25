package com.svnavigatoru600.selenium.tests;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;

import com.svnavigatoru600.selenium.SeleniumTest;
import com.svnavigatoru600.test.category.SeleniumTests;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 * @see EmailTest
 */
@Category(SeleniumTests.class)
public class NewsTest extends SeleniumTest {

	private final String XPATH_NEWS_SECTION = "//*[@id='nav']/li[1]/a[1]";

	private final String XPATH_FIRST_NEWS_TITLE = "//*[@id='newsList']/*[@class='post'][1]//h3";
	private final String XPATH_FIRST_NEWS_TEXT = "//*[@id='newsList']/*[@class='post'][1]/*[@class='post-content clearfix'][1]/p[1]";

	private final String XPATH_NEW_NEWS_LINK = "//*[@id='newNewsLink']/a[1]";
	private final String NEW_NEWS_TITLE_ID = "news.title";
	private final String NEW_NEWS_TEXT_ID = "tinymce";
	private final String NEW_NEWS_SUBMIT_ID = "newsForm.submitButton";

	private final String XPATH_FIRST_NEWS_EDIT = "//*[@id='newsList']/*[@class='post'][1]//*[@class='controls'][1]/a[1]";
	private final String EDIT_NEWS_TITLE_ID = "news.title";
	private final String EDIT_NEWS_TEXT_ID = NEW_NEWS_TEXT_ID;
	private final String EDIT_NEWS_SUBMIT_ID = NEW_NEWS_SUBMIT_ID;
	private final String SUCCESS_EDIT_MESSAGE_ID = "successEditMessage";

	private final String XPATH_FIRST_NEWS_DELETE = "//*[@id='newsList']/*[@class='post'][1]//*[@class='controls'][1]/a[2]";

	@Test
	public void testAddEditDelete() throws Exception {
		this.logIn();
		this.createNewNews();
		this.editNewNews();
		this.deleteNewNews();
		this.logOut();
	}

	private void logIn() {
		final WebDriver browserDriver = this.getBrowserDriver();

		browserDriver.findElement(By.id("login")).sendKeys("skalicky.tomas@gmail.com");
		browserDriver.findElement(By.id("password")).sendKeys("t");
		browserDriver.findElement(By.id("rememberMe")).click();
		browserDriver.findElement(By.cssSelector("input[type='submit']")).click();
		this.waitForPageUrl(browserDriver, ".*/novinky/");
	}

	private void createNewNews() {
		final WebDriver browserDriver = this.getBrowserDriver();

		// CLUE: Use a Firefox extension called "XPath Checker" to find out a xpath of a certain element.
		browserDriver.findElement(By.xpath(XPATH_NEW_NEWS_LINK)).click();
		Assert.isTrue((new WebDriverWait(browserDriver, DEFAULT_TIMEOUT_IN_SECONDS,
				DEFAULT_SLEEP_BETWEEN_POLLS_IN_MILLISECONDS)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return "Přidat novinku".equals(NewsTest.this.getAttributeValue(NEW_NEWS_SUBMIT_ID));
			}
		}));

		final String newTitle = "New News";
		browserDriver.findElement(By.id(NEW_NEWS_TITLE_ID)).sendKeys(newTitle);

		final String newText = "New Text";
		WebElement tinymceIframe = browserDriver.findElement(By.id("news.text_ifr"));
		browserDriver.switchTo().frame(tinymceIframe);
		WebElement newsTextBox = browserDriver.findElement(By.id(NEW_NEWS_TEXT_ID));
		newsTextBox.sendKeys(newText);
		browserDriver.switchTo().window(browserDriver.getWindowHandle());

		browserDriver.findElement(By.cssSelector("input[type='submit']")).click();
		Assert.isTrue((new WebDriverWait(browserDriver, DEFAULT_TIMEOUT_IN_SECONDS,
				DEFAULT_SLEEP_BETWEEN_POLLS_IN_MILLISECONDS)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return newTitle.equals(NewsTest.this.getElementText(XPATH_FIRST_NEWS_TITLE))
						&& newText.equals(NewsTest.this.getElementText(XPATH_FIRST_NEWS_TEXT));
			}
		}));
	}

	private void editNewNews() {
		final WebDriver browserDriver = this.getBrowserDriver();

		browserDriver.findElement(By.xpath(XPATH_FIRST_NEWS_EDIT)).click();
		Assert.isTrue((new WebDriverWait(browserDriver, DEFAULT_TIMEOUT_IN_SECONDS,
				DEFAULT_SLEEP_BETWEEN_POLLS_IN_MILLISECONDS)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return "Uložit změny".equals(NewsTest.this.getAttributeValue(EDIT_NEWS_SUBMIT_ID));
			}
		}));

		final String newTitle = "Edited News";
		browserDriver.findElement(By.id(EDIT_NEWS_TITLE_ID)).sendKeys(newTitle);

		final String newTextWithFormating = "<p>Edited Text</p>";
		//browserDriver.findElement(By.xpath(XPATH_EDIT_NEWS_TEXT)).sendKeys(newTextWithFormating);

		browserDriver.findElement(By.cssSelector("input[type='submit']")).click();
		Assert.isTrue((new WebDriverWait(browserDriver, DEFAULT_TIMEOUT_IN_SECONDS,
				DEFAULT_SLEEP_BETWEEN_POLLS_IN_MILLISECONDS)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return driver.findElement(By.id(SUCCESS_EDIT_MESSAGE_ID)).isDisplayed();
			}
		}));

		browserDriver.findElement(By.xpath(XPATH_NEWS_SECTION)).click();
		this.waitForPageUrl(browserDriver, ".*/novinky/");
	}

	private void deleteNewNews() {
		final WebDriver browserDriver = this.getBrowserDriver();

		browserDriver.findElement(By.xpath(XPATH_FIRST_NEWS_DELETE)).click();
		// browserDriver.waitForCondition("selenium.isConfirmationPresent()", MAX_WAIT_TIME_IN_MS);
		// final String confirmation = browserDriver.getConfirmation();
		// assertTrue("The actual value is: " + confirmation,
		// confirmation.matches("^Opravdu chcete smazat novinku `Edited News`\\?$"));
	}

	private void logOut() {
		final WebDriver browserDriver = this.getBrowserDriver();

		browserDriver.findElement(By.linkText("Odhlásit se")).click();
		this.waitForPageUrl(browserDriver, ".*/prihlaseni/");
	}
}
