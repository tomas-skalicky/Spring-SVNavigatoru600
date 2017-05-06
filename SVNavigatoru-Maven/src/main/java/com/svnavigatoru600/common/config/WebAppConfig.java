package com.svnavigatoru600.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Tomas Skalicky
 * @since 06.05.2017
 */
@Configuration
// @formatter:off
@ComponentScan(basePackageClasses = {
        com.svnavigatoru600.common.settings.PackageMarker.class,
        com.svnavigatoru600.domain.PackageMarker.class,
        com.svnavigatoru600.repository.PackageMarker.class,
        com.svnavigatoru600.service.PackageMarker.class,
        })
//@formatter:on
@ImportResource({ "classpath:spring/applicationContext-security.xml" })
@Import({ DataSourceConfig.class, CacheConfig.class })
public class WebAppConfig {

}
