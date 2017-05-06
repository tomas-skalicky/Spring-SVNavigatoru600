package com.svnavigatoru600.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;
import org.springframework.web.servlet.view.tiles2.TilesView;
import org.springframework.web.servlet.view.tiles2.TilesViewResolver;

import com.svnavigatoru600.common.constants.CommonConstants;

/**
 * First, we try to match the view name with names of definitions in the tiles-defs.xml file. If the name matches with
 * some definition, this definition is followed, i.e. a {@link TilesViewResolver} is used. Otherwise, an
 * {@link InternalResourceViewResolver} is used.
 *
 * @author Tomas Skalicky
 * @since 06.05.2017
 */
@Configuration
public class ViewResolverConfig {

    @Bean
    TilesViewResolver tilesViewResolver() {
        final TilesViewResolver viewResolver = new TilesViewResolver();
        viewResolver.setOrder(0);
        viewResolver.setViewClass(TilesView.class);
        return viewResolver;
    }

    /**
     * Bound to the {@link #tilesViewResolver() tilesViewResolver}.
     */
    @Bean
    TilesConfigurer tilesConfigurer() {
        final TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions("/WEB-INF/tiles-defs.xml");
        return tilesConfigurer;
    }

    @Bean
    InternalResourceViewResolver jstlViewResolver() {
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setOrder(1);
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(CommonConstants.JSP_FILE_EXTENSION_WITH_DOT);
        return viewResolver;
    }

}
