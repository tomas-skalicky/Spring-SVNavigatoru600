package svnavigatoru.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.support.RequestContext;

/**
 * Provides a set of static functions related to localizations of the text.
 * 
 * @author Tomas Skalicky
 */
public class Localization {

	/**
	 * Gets a message which corresponds to the given <code>code</code>. The current localization is to be used.
	 */
	public static String findLocaleMessage(MessageSource messageSource, HttpServletRequest request, String messageCode) {
		return Localization.findLocaleMessage(messageSource, request, messageCode, new Object[] {});
	}

	/**
	 * Takes {@link Locale} from the given <code>request</code>.
	 */
	public static Locale getLocale(HttpServletRequest request) {
		RequestContext requestContext = new RequestContext(request);
		return requestContext.getLocale();
	}

	/**
	 * Gets a message which corresponds to the given <code>code</code>. The current localization is to be used.
	 * 
	 * @param messageSource
	 * @param request
	 * @param messageCode
	 * @param parameters
	 *            Parameters in the localization of the desired message. References are done via "{&lt;number>}".
	 */
	public static String findLocaleMessage(MessageSource messageSource, HttpServletRequest request, String messageCode,
			Object[] parameters) {
		return messageSource.getMessage(messageCode, parameters, Localization.getLocale(request));
	}

	/**
	 * Gets the bean of the current servlet associated with the given <code>request</code>.
	 */
	public static MessageSource getMessageSource(HttpServletRequest request) {
		return (MessageSource) MyRequestContext.getBean("messageSource", request);
	}

	/**
	 * Localizes the global errors of the {@link BindingResult} object. The reason for usage of this method is an
	 * application of AJAX requests.
	 */
	public static List<ObjectError> localizeGlobalErrors(List<ObjectError> errors, MessageSource messageSource,
			HttpServletRequest request) {
		List<ObjectError> localizedErrors = new ArrayList<ObjectError>();
		for (ObjectError error : errors) {
			String[] localizedCodes = Localization.localizeCodes(error.getCodes(), messageSource, request);
			localizedErrors.add(new ObjectError(error.getObjectName(), localizedCodes, error.getArguments(), error
					.getDefaultMessage()));
		}
		return localizedErrors;
	}

	/**
	 * Localizes the field errors of the {@link BindingResult} object. The reason for usage of this method is an
	 * application of AJAX requests.
	 */
	public static List<FieldError> localizeFieldErrors(List<FieldError> errors, MessageSource messageSource,
			HttpServletRequest request) {
		List<FieldError> localizedErrors = new ArrayList<FieldError>();
		for (FieldError error : errors) {
			String[] localizedCodes = Localization.localizeCodes(error.getCodes(), messageSource, request);
			localizedErrors.add(new FieldError(error.getObjectName(), error.getField(), error.getRejectedValue(), error
					.isBindingFailure(), localizedCodes, error.getArguments(), error.getDefaultMessage()));
		}
		return localizedErrors;
	}

	/**
	 * Localizes the given message codes.
	 */
	private static String[] localizeCodes(String[] codes, MessageSource messageSource, HttpServletRequest request) {
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
}
