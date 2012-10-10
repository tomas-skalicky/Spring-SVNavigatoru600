package com.svnavigatoru600.test.category.testsuites;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.svnavigatoru600.test.category.FastTests;

/**
 * Contains all tests which belong to the {@link FastTests} category.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 * @see <a
 *      href="http://stackoverflow.com/questions/6226026/how-to-run-all-junit-tests-in-a-category-suite-with-ant">How-to-run-all-JUnit-tests-in-a-category</a>
 */
@RunWith(Categories.class)
@IncludeCategory(FastTests.class)
@SuiteClasses(AllTests.class)
public final class FastTestSuite {
}
