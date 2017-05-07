/**
 *
 */
package com.svnavigatoru600.common.aspects;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.svnavigatoru600.test.category.IntegrationTests;
import com.svnavigatoru600.test.common.aspects.TestBeanForMethodLogginAspectTest;
import com.svnavigatoru600.test.common.aspects.TestConfigForMethodLoggingAspectTest;

/**
 * @author Tomas Skalicky
 * @since 07.05.2017
 */
@Category(IntegrationTests.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfigForMethodLoggingAspectTest.class)
public class MethodLoggingAspectTest {

    /**
     * NOTE 1: We need to add an appender to the MethodLoggingAspect logger, not to any other logger.
     * <p>
     * NOTE 2: Since we need to add a new appender, we cannot work with slf4j, but directly with log4j.
     */
    private final Logger aspectLogger = Logger.getLogger(MethodLoggingAspect.class);

    @Inject
    private TestBeanForMethodLogginAspectTest testBean;

    private StringListAppender newAppender;

    @Before
    public void addNewAppender() {
        newAppender = new StringListAppender();
        aspectLogger.addAppender(newAppender);
    }

    @After
    public void removeNewAppender() {
        aspectLogger.removeAppender(newAppender);
    }

    @Test
    public void testAnnotatedMethod() {
        testBean.annotatedMethod("Ahoj", 1122);

        //@formatter:off
        assertThat(newAppender.getMessages())
                .hasSize(2)
                .contains("Before execution(long com.svnavigatoru600.test.common.aspects.TestBeanForMethodLogginAspectTest.annotatedMethod(String,int)) with args: [Ahoj, 1122]",
                          "After execution(long com.svnavigatoru600.test.common.aspects.TestBeanForMethodLogginAspectTest.annotatedMethod(String,int))(..), return value: 1122");
        //@formatter:on
    }

    @Test
    public void testNotAnnotatedMethod() {
        testBean.notAnnotatedMethod("Ahoj", 1122);

        assertThat(newAppender.getMessages()).isEmpty();
    }

}
