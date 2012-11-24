package com.svnavigatoru600.selenium.tests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.test.category.SeleniumTests;


/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 * @see EmailTest
 */
@Category(SeleniumTests.class)
public class NewsTest extends AbstractTestCase {

	private final String XPATH_NEWS_SECTION = "//*[@id='nav']/li[1]/a[1]";

	private final String XPATH_FIRST_NEWS_TITLE = "//*[@id='newsList']/*[@class='post'][1]//h3";
	private final String XPATH_FIRST_NEWS_TEXT = "//*[@id='newsList']/*[@class='post'][1]/*[@class='post-content clearfix'][1]/p[1]";

	private final String XPATH_NEW_NEWS_LINK = "//*[@id='newNewsLink']/a[1]";
	private final String XPATH_NEW_NEWS_TITLE = "//*[@id='news.title']";
	private final String XPATH_NEW_NEWS_TEXT = "//*[@id='tinymce']";
	private final String XPATH_NEW_NEWS_SUBMIT = "//*[@id='newsForm.submitButton']";

	private final String XPATH_FIRST_NEWS_EDIT = "//*[@id='newsList']/*[@class='post'][1]//*[@class='controls'][1]/a[1]";
	private final String XPATH_EDIT_NEWS_TITLE = "//*[@id='news.title']";
	private final String XPATH_EDIT_NEWS_TEXT = XPATH_NEW_NEWS_TEXT;
	private final String XPATH_EDIT_NEWS_SUBMIT = XPATH_NEW_NEWS_SUBMIT;
	private final String XPATH_SUCCESS_EDIT_MESSAGE = "//*[@id='successEditMessage']";

	private final String XPATH_FIRST_NEWS_DELETE = "//*[@id='newsList']/*[@class='post'][1]//*[@class='controls'][1]/a[2]";

	private String getElementInfo(String xpath, String info) {
		StringBuffer builder = new StringBuffer("selenium.get");
		builder.append(info);
		builder.append("(\"");
		builder.append(xpath);
		builder.append("\")");
		return builder.toString();
	}

	private String getText(String xpath) {
		return this.getElementInfo(xpath, "Text");
	}

	private String getValue(String xpath) {
		return this.getElementInfo(xpath, "Value");
	}

	@Test
	public void testAddEditDelete() throws Exception {
		this.logIn();
		this.createNewNews();
		this.editNewNews();
		this.deleteNewNews();
		this.logOut();
	}

	private void logIn() {
		selenium.open("/SVNavigatoru/prihlaseni/");
		selenium.type("id=login", "skalicky.tomas@gmail.com");
		selenium.type("id=password", "t");
		selenium.click("id=rememberMe");

		selenium.click("css=input[type='submit']");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
	}

	private void createNewNews() {
		// CLUE: Use a Firefox extension called "XPath Checker" to find out a xpath of a certain element.
		selenium.click("xpath=" + XPATH_NEW_NEWS_LINK);
		selenium.waitForCondition(this.getValue(XPATH_NEW_NEWS_SUBMIT) + " == 'Přidat novinku'", MAX_WAIT_TIME_IN_MS);

		final String newTitle = "New News";
		selenium.type("xpath=" + XPATH_NEW_NEWS_TITLE, newTitle);

		final String newText = "New Text";
		selenium.type("xpath=" + XPATH_NEW_NEWS_TEXT, "<p>" + newText + "</p>");

		selenium.click("css=input[type='submit']");
		String condition = this.getText(XPATH_FIRST_NEWS_TITLE) + " == '" + newTitle + "' && "
				+ this.getText(XPATH_FIRST_NEWS_TEXT) + " == '" + newText + "'";
		selenium.waitForCondition(condition, MAX_WAIT_TIME_IN_MS);
	}

	private void editNewNews() {
		selenium.click("xpath=" + XPATH_FIRST_NEWS_EDIT);
		selenium.waitForCondition(this.getValue(XPATH_EDIT_NEWS_SUBMIT) + " == 'Uložit změny'", MAX_WAIT_TIME_IN_MS);

		final String newTitle = "Edited News";
		selenium.type("xpath=" + XPATH_EDIT_NEWS_TITLE, newTitle);

		final String newTextWithFormating = "<p>Edited Text</p>";
		selenium.type("xpath=" + XPATH_EDIT_NEWS_TEXT, newTextWithFormating);

		selenium.click("css=input[type='submit']");
		String condition = "selenium.isVisible(\"xpath=" + XPATH_SUCCESS_EDIT_MESSAGE + "\")";
		selenium.waitForCondition(condition, MAX_WAIT_TIME_IN_MS);

		selenium.click("xpath=" + XPATH_NEWS_SECTION);
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
	}

	private void deleteNewNews() {
		selenium.click("xpath=" + XPATH_FIRST_NEWS_DELETE);
		selenium.waitForCondition("selenium.isConfirmationPresent()", MAX_WAIT_TIME_IN_MS);
		final String confirmation = selenium.getConfirmation();
		assertTrue("The actual value is: " + confirmation,
				confirmation.matches("^Opravdu chcete smazat novinku `Edited News`\\?$"));
	}

	private void logOut() {
		selenium.click("link=Odhlásit se");
		selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
	}
}
