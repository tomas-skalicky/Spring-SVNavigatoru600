/**
 *
 */
package com.svnavigatoru600.test.common.aspects;

import com.svnavigatoru600.common.annotations.LogMethod;

/**
 * @author Tomas Skalicky
 * @since 07.05.2017
 */
public class TestBeanForMethodLogginAspectTest {

    @LogMethod
    public long annotatedMethod(final String param1, final int param2) {
        return param2;
    }

    public long notAnnotatedMethod(final String param1, final int param2) {
        return param2;
    }

}
