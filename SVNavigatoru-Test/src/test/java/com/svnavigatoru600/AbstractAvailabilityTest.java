package com.svnavigatoru600;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * Ancestor of all availability tests. In other words, this class is an ancestor of all Selenium and jsoup
 * tests.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractAvailabilityTest {

    /**
     * Context which provide us with the access to Beans of servers, test user and so on.
     */
    protected static final ApplicationContext APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(
            AvailabilityTestsAppConfig.class);

}
