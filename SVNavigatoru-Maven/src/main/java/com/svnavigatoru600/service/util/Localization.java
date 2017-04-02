package com.svnavigatoru600.service.util;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.support.RequestContext;

import com.google.common.collect.Lists;

/**
 * Provides a set of static functions related to localizations of the text.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class Localization {

    private Localization() {
    }

    /**
     * Gets a localization of the message which has the given <code>messageCode</code>. The code requires NO parameter.
     * <p>
     * The current locale settings is used.
     *
     * @param messageCode
     *            Code of the message stored typically in messages*.properties.
     */
    public static String findLocaleMessage(final MessageSource messageSource, final HttpServletRequest request,
            final String messageCode) {
        return Localization.findLocaleMessage(messageSource, request, messageCode, new Object[] {});
    }

    /**
     * Gets a localization of the message which has the given <code>messageCode</code>. The code requires ONE parameter.
     * <p>
     * The current locale settings is used.
     *
     * @param messageCode
     *            Code of the message stored typically in messages*.properties.
     * @param parameter
     *            Parameter of the code which substitutes the <code>{0}</code> placeholder in the localization.
     */
    public static String findLocaleMessage(final MessageSource messageSource, final HttpServletRequest request, final String messageCode,
            final Object parameter) {
        return Localization.findLocaleMessage(messageSource, request, messageCode, new Object[] { parameter });
    }

    /**
     * Gets a localization of the message which has the given <code>messageCode</code>. The code requires a various
     * number of parameters depending on the given code.
     * <p>
     * The current locale settings is used.
     *
     * @param messageCode
     *            Code of the message stored typically in messages*.properties.
     * @param parameters
     *            Parameters of the code which substitute placeholders in the localization. The order of the parameters
     *            in the array is important. For instance: the 2nd parameter (with index 1) substitutes the
     *            <code>{1}</code> placeholder in the localization.
     */
    public static String findLocaleMessage(final MessageSource messageSource, final HttpServletRequest request, final String messageCode,
            final Object[] parameters) {
        return messageSource.getMessage(messageCode, parameters, Localization.getLocale(request));
    }

    /**
     * Takes {@link Locale} from the given <code>request</code>.
     */
    public static Locale getLocale(final HttpServletRequest request) {
        final RequestContext requestContext = new RequestContext(request);
        return requestContext.getLocale();
    }

    /**
     * Gets the bean of the current servlet associated with the given <code>request</code>.
     */
    public static MessageSource getMessageSource(final HttpServletRequest request) {
        return (MessageSource) MyRequestContext.getBean("messageSource", request);
    }

    /**
     * Localizes the global errors of the {@link org.springframework.validation.BindingResult BindingResult} object. The
     * reason for usage of this method is an application of AJAX requests.
     */
    public static List<ObjectError> localizeGlobalErrors(final List<ObjectError> errors, final MessageSource messageSource,
            final HttpServletRequest request) {
        final List<ObjectError> localizedErrors = Lists.newArrayList();
        for (final ObjectError error : errors) {
            final String[] localizedCodes = Localization.localizeCodes(error.getCodes(), messageSource, request);
            localizedErrors.add(new ObjectError(error.getObjectName(), localizedCodes, error.getArguments(),
                    error.getDefaultMessage()));
        }
        return localizedErrors;
    }

    /**
     * Localizes the field errors of the {@link org.springframework.validation.BindingResult BindingResult} object. The
     * reason for usage of this method is an application of AJAX requests.
     */
    public static List<FieldError> localizeFieldErrors(final List<FieldError> errors, final MessageSource messageSource,
            final HttpServletRequest request) {
        final List<FieldError> localizedErrors = Lists.newArrayList();
        for (final FieldError error : errors) {
            final String[] localizedCodes = Localization.localizeCodes(error.getCodes(), messageSource, request);
            localizedErrors.add(new FieldError(error.getObjectName(), error.getField(), error.getRejectedValue(),
                    error.isBindingFailure(), localizedCodes, error.getArguments(), error.getDefaultMessage()));
        }
        return localizedErrors;
    }

    /**
     * Localizes the given message codes.
     */
    private static String[] localizeCodes(final String[] codes, final MessageSource messageSource, final HttpServletRequest request) {
        final String[] localizedCodes = new String[codes.length];
        for (int codeI = 0; codeI < codes.length; ++codeI) {
            try {
                localizedCodes[codeI] = Localization.findLocaleMessage(messageSource, request, codes[codeI]);
            } catch (final NoSuchMessageException e) {
                // We want to localize at least those message which can be.
                localizedCodes[codeI] = codes[codeI];
            }
        }
        return localizedCodes;
    }

    /**
     * Strips the Czech diacritics in the given <code>text</code>.
     * <p>
     * It replaces for instance 'č' by 'c'.
     *
     * @return Text without Czech diacritics
     */
    public static String stripCzechDiacritics(final String text) {
        return text.replace('á', 'a').replace('Á', 'A').replace('č', 'c').replace('Č', 'C').replace('ď', 'd')
                .replace('Ď', 'D').replace('é', 'e').replace('É', 'E').replace('ě', 'e').replace('Ě', 'E')
                .replace('í', 'i').replace('Í', 'I').replace('ň', 'n').replace('Ň', 'N').replace('ó', 'o')
                .replace('Ó', 'O').replace('ř', 'r').replace('Ř', 'R').replace('š', 's').replace('Š', 'S')
                .replace('ť', 't').replace('Ť', 'T').replace('ú', 'u').replace('Ú', 'U').replace('ů', 'u')
                .replace('Ů', 'U').replace('ý', 'y').replace('Ý', 'Y').replace('ž', 'z').replace('Ž', 'Z');
    }
}
