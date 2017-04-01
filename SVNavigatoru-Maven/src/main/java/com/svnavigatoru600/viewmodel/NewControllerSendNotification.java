package com.svnavigatoru600.viewmodel;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.service.util.Localization;

/**
 * {@link SendNotification} view model which contains standard settings for controllers which handle <em>new</em>
 * operations.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class NewControllerSendNotification extends SendNotification {

    private static final boolean DEFAULT_CHECKBOX_STATUS = true;
    private static final String CHECKBOX_TITLE_LOCALIZATION_CODE = "notifications.send-users-notification-of-news";
    private static String checkboxTitle = null;

    public NewControllerSendNotification(HttpServletRequest request, MessageSource messageSource) {
        super(DEFAULT_CHECKBOX_STATUS, getCheckboxTitle(request, messageSource));
    }

    /**
     * Gets the localized title of the send-notification checkbox.
     */
    public static String getCheckboxTitle(HttpServletRequest request, MessageSource messageSource) {
        if (checkboxTitle == null) {
            checkboxTitle = Localization.findLocaleMessage(messageSource, request, CHECKBOX_TITLE_LOCALIZATION_CODE);
        }
        return checkboxTitle;
    }
}
