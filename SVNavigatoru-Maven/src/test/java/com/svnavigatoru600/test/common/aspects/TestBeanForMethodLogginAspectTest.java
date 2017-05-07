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
    public long logEverything(final String param1, final int param2) {
        return param2;
    }

    @LogMethod(logReturnValue = false)
    public long doNotLogReturnValue(final String param1, final int param2) {
        return param2;
    }

    public long notAnnotatedMethod(final String param1, final int param2) {
        return param2;
    }

}
