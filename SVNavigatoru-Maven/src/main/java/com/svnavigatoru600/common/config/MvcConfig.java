package com.svnavigatoru600.common.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.common.collect.ImmutableMap;
import com.svnavigatoru600.common.constants.CommonConstants;

/**
 * @author Tomas Skalicky
 * @since 06.05.2017
 */
@Configuration
@EnableWebMvc
//@formatter:off
@ComponentScan(basePackageClasses = {
        com.svnavigatoru600.viewmodel.PackageMarker.class,
        com.svnavigatoru600.web.PackageMarker.class,
        })
//@formatter:on
@Import({ LocalizationConfig.class, ViewResolverConfig.class })
public class MvcConfig extends WebMvcConfigurerAdapter {

    /**
     * Resources (URIs) matching the mapped path patterns (keys) are not processed by the dispatcher servler, but are
     * considered as static.
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        // @formatter:off
        final Map<String, String> resourceMapping = ImmutableMap.<String, String> builder()
                .put("/arclite/**", "/arclite/")
                .put("/favicon.ico", "/favicon.ico")
                .put("/img/**", "/img/")
                .put("/lib/**", "/lib/")
                .put("/tigra_calendar/**", "/tigra_calendar/")
                .put("/tinymce/**", "/tinymce/")
                .put("/uploaded-files/**", "/uploaded-files/")
                .build();
        // @formatter:on

        resourceMapping.entrySet()
                .forEach(e -> registry.addResourceHandler(e.getKey()).addResourceLocations(e.getValue()));
    }

    @Bean
    CommonsMultipartResolver multipartResolver() {
        final CommonsMultipartResolver resolver = new CommonsMultipartResolver();

        // The maximal size of a file which can be uploaded (in bytes).
        resolver.setMaxUploadSize(CommonConstants.MAX_UPLOAD_SIZE);
        return resolver;
    }

}
