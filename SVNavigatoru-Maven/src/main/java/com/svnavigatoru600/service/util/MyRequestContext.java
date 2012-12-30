package com.svnavigatoru600.service.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Provides a set of static functions related to the context of the HTTP request.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class MyRequestContext {

    private MyRequestContext() {
    }

    /**
     * Gets the bean with the given <code>beanName</code> accessible through the given <code>request</code>.
     */
    public static Object getBean(String beanName, HttpServletRequest request) {
        ApplicationContext appContext = RequestContextUtils.getWebApplicationContext(request);
        return appContext.getBean(beanName);
    }
}
