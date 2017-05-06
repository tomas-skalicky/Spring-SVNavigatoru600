package com.svnavigatoru600.common.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.svnavigatoru600.common.constants.CommonConstants;
import com.svnavigatoru600.service.util.TimeUtils;

/**
 * @author Tomas Skalicky
 * @since 06.05.2017
 */
@Configuration
public class LocalizationConfig {

    @Bean
    MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setCacheSeconds(TimeUtils.HOUR_IN_SECONDS);
        messageSource.setDefaultEncoding(CommonConstants.DEFAULT_CHARSET_VALUE);
        messageSource.setBasename("classpath:locale/messages");
        return messageSource;
    }

    @Bean
    LocaleChangeInterceptor localeChangeInterceptor() {
        final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Bean
    LocaleResolver localeResolver() {
        final CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(Locale.forLanguageTag("cs-CZ"));
        return localeResolver;
    }

}
