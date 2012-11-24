package com.svnavigatoru600.selenium;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

/**
 * Contains convenient methods used in methods of {@link Assert}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class AssertUtils {

	/**
	 * Returns the report about the current message of the given {@link WebDriver browser driver}.
	 */
	public static String getActualUrlReport(WebDriver browserDriver) {
		return String.format("Actual location is '%s'", browserDriver.getCurrentUrl());
	}
}
