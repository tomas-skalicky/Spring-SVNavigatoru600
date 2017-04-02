package com.svnavigatoru600.availability.selenium;

import org.openqa.selenium.WebDriver;

/**
 * Contains convenient methods used in methods of {@link org.junit.Assert Assert}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class AssertUtils {

    private AssertUtils() {
    }

    /**
     * Returns the report about the current message of the given {@link WebDriver browser driver}.
     * 
     * @param browserDriver
     *            The browser
     * @return The report about the current URL that the browser is looking at.
     */
    public static String getActualUrlReport(final WebDriver browserDriver) {
        return String.format("Actual location is '%s'", browserDriver.getCurrentUrl());
    }
}
