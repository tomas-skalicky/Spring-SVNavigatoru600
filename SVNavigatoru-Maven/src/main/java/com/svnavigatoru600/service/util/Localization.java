package com.svnavigatoru600.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.support.RequestContext;

/**
 * Provides a set of static functions related to localizations of the text.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class Localization {

    private Localization() {
    }

    /**
     * Gets a localization of the message which has the given <code>messageCode</code>. The code requires NO
     * parameter.
     * <p>
     * The current locale settings is used.
     * 
     * @param messageCode
     *            Code of the message stored typically in messages*.properties.
     */
    public static String findLocaleMessage(MessageSource messageSource, HttpServletRequest request,
            String messageCode) {
        return Localization.findLocaleMessage(messageSource, request, messageCode, new Object[] {});
    }

    /**
     * Gets a localization of the message which has the given <code>messageCode</code>. The code requires ONE
     * parameter.
     * <p>
     * The current locale settings is used.
     * 
     * @param messageCode
     *            Code of the message stored typically in messages*.properties.
     * @param parameter
     *            Parameter of the code which substitutes the <code>{0}</code> placeholder in the
     *            localization.
     */
    public static String findLocaleMessage(MessageSource messageSource, HttpServletRequest request,
            String messageCode, Object parameter) {
        return Localization
                .findLocaleMessage(messageSource, request, messageCode, new Object[] { parameter });
    }

    /**
     * Gets a localization of the message which has the given <code>messageCode</code>. The code requires a
     * various number of parameters depending on the given code.
     * <p>
     * The current locale settings is used.
     * 
     * @param messageCode
     *            Code of the message stored typically in messages*.properties.
     * @param parameters
     *            Parameters of the code which substitute placeholders in the localization. The order of the
     *            parameters in the array is important. For instance: the 2nd parameter (with index 1)
     *            substitutes the <code>{1}</code> placeholder in the localization.
     */
    public static String findLocaleMessage(MessageSource messageSource, HttpServletRequest request,
            String messageCode, Object[] parameters) {
        return messageSource.getMessage(messageCode, parameters, Localization.getLocale(request));
    }

    /**
     * Takes {@link Locale} from the given <code>request</code>.
     */
    public static Locale getLocale(HttpServletRequest request) {
        RequestContext requestContext = new RequestContext(request);
        return requestContext.getLocale();
    }

    /**
     * Gets the bean of the current servlet associated with the given <code>request</code>.
     */
    public static MessageSource getMessageSource(HttpServletRequest request) {
        return (MessageSource) MyRequestContext.getBean("messageSource", request);
    }

    /**
     * Localizes the global errors of the {@link org.springframework.validation.BindingResult BindingResult}
     * object. The reason for usage of this method is an application of AJAX requests.
     */
    public static List<ObjectError> localizeGlobalErrors(List<ObjectError> errors,
            MessageSource messageSource, HttpServletRequest request) {
        List<ObjectError> localizedErrors = new ArrayList<ObjectError>();
        for (ObjectError error : errors) {
            String[] localizedCodes = Localization.localizeCodes(error.getCodes(), messageSource, request);
            localizedErrors.add(new ObjectError(error.getObjectName(), localizedCodes, error.getArguments(),
                    error.getDefaultMessage()));
        }
        return localizedErrors;
    }

    /**
     * Localizes the field errors of the {@link org.springframework.validation.BindingResult BindingResult}
     * object. The reason for usage of this method is an application of AJAX requests.
     */
    public static List<FieldError> localizeFieldErrors(List<FieldError> errors, MessageSource messageSource,
            HttpServletRequest request) {
        List<FieldError> localizedErrors = new ArrayList<FieldError>();
        for (FieldError error : errors) {
            String[] localizedCodes = Localization.localizeCodes(error.getCodes(), messageSource, request);
            localizedErrors.add(new FieldError(error.getObjectName(), error.getField(), error
                    .getRejectedValue(), error.isBindingFailure(), localizedCodes, error.getArguments(),
                    error.getDefaultMessage()));
        }
        return localizedErrors;
    }

    /**
     * Localizes the given message codes.
     */
    private static String[] localizeCodes(String[] codes, MessageSource messageSource,
            HttpServletRequest request) {
        String[] localizedCodes = new String[codes.length];
        for (int codeI = 0; codeI < codes.length; ++codeI) {
            try {
                localizedCodes[codeI] = Localization.findLocaleMessage(messageSource, request, codes[codeI]);
            } catch (NoSuchMessageException e) {
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
    public static String stripCzechDiacritics(String text) {
        return text.replace('á', 'a').replace('č', 'c').replace('ď', 'd').replace('é', 'e').replace('ě', 'e')
                .replace('í', 'i').replace('ň', 'n').replace('ó', 'o').replace('ř', 'r').replace('š', 's')
                .replace('ť', 't').replace('ú', 'u').replace('ů', 'u').replace('ý', 'y').replace('ž', 'z');
    }
}
