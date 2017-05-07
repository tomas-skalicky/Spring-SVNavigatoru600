package com.svnavigatoru600.test.common.aspects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.svnavigatoru600.common.config.CommonConfig;

/**
 * @author Tomas Skalicky
 * @since 06.05.2017
 */
@Configuration
@Import(CommonConfig.class)
public class TestConfigForMethodLoggingAspectTest {

    @Bean
    TestBeanForMethodLogginAspectTest testBean() {
        return new TestBeanForMethodLogginAspectTest();
    }

}
