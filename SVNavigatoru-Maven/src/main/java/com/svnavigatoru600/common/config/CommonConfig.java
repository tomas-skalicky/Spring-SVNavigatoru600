package com.svnavigatoru600.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import com.svnavigatoru600.common.constants.CommonConstants;

/**
 * @author Tomas Skalicky
 * @since 07.05.2017
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = CommonConstants.USE_CGLIB_PROXY)
@Import(CacheConfig.class)
//@formatter:off
@ComponentScan(basePackageClasses = {
     com.svnavigatoru600.common.aspects.PackageMarker.class,
     com.svnavigatoru600.common.settings.PackageMarker.class,
     })
//@formatter:on
public class CommonConfig {

}
