/**
 *
 */
package com.svnavigatoru600.web;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.test.category.UnitTests;

/**
 * @author Tomas Skalicky
 * @date 01.04.2017
 */
@Category(UnitTests.class)
public class RequestAndResponseLoggingFilterTest {

    private static final RequestAndResponseLoggingFilter loggingFilter = new RequestAndResponseLoggingFilter();

    @Test
    public void testPasswordHidingInPayload() {
        final String actualResult = loggingFilter
                .hideLogInPasswordInPayload("j_username=tomas&j_password=0000&foo=bar");

        Assertions.assertThat(actualResult)
                .isEqualTo("j_username=tomas&j_password=******************************&foo=bar");
    }
}
