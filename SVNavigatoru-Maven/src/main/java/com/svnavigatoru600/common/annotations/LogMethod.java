package com.svnavigatoru600.common.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Tomas Skalicky
 * @since 07.05.2017
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface LogMethod {

}
