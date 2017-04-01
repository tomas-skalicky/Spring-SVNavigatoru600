/**
 *
 */
package com.svnavigatoru600.web;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.mock.web.MockHttpServletRequest;

import com.google.common.collect.ImmutableMap;
import com.svnavigatoru600.test.category.UnitTests;
import com.svnavigatoru600.web.RequestAndResponseLoggingFilter.RequestWrapper;

/**
 * @author Tomas Skalicky
 * @date 01.04.2017
 */
@Category(UnitTests.class)
public class RequestAndResponseLoggingFilterRequestWrapperTest {

    private static final RequestWrapper requestWrapper = new RequestAndResponseLoggingFilter().new RequestWrapper(
            new MockHttpServletRequest());

    @Test
    public void testRequestParameterRetrieval() {
        final Map<String, String[]> resultMap = requestWrapper.retrieveRequestParametersFromPayload(
                "news.title=title%3D%26%3Dtitle&news.text=%3Cp%3Etext%3D%26amp%3B%3Dtext%3C%2Fp%3E");

        // @formatter:off
        Assertions.assertThat(resultMap).containsAllEntriesOf(
                ImmutableMap.<String, String[]> builder()
                        .put("news.title", new String[] { "title=&=title" })
                        .put("news.text", new String[] { "<p>text=&amp;=text</p>" })
                        .build());
        // @formatter:on
    }
}
