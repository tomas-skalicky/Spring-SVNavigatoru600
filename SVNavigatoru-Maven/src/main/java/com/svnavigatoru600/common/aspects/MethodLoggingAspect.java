package com.svnavigatoru600.common.aspects;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.svnavigatoru600.common.annotations.LogMethod;

/**
 * @author Tomas Skalicky
 * @since 07.05.2017
 */
@Aspect
// NOTE: Spring aspects must be beans. Otherwise, they would not be used.
@Component
public class MethodLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut(value = "execution(@com.svnavigatoru600.common.annotations.LogMethod * *(..)) && @annotation(logMethodAnnotation))", argNames = "logMethodAnnotation")
    private void annotatedMethod(final LogMethod logMethodAnnotation) {
    }

    @Around(value = "annotatedMethod(logMethodAnnotation)", argNames = "logMethodAnnotation")
    public Object logMethod(final ProceedingJoinPoint joinPoint, final LogMethod logMethodAnnotation) throws Throwable {

        logger.info("Before {} with args: {}", joinPoint.toString(), Arrays.toString(joinPoint.getArgs()));

        final Object result = joinPoint.proceed();

        logger.info("After {}(..), return value: {}", joinPoint.toString(), result);

        return result;
    }

}
