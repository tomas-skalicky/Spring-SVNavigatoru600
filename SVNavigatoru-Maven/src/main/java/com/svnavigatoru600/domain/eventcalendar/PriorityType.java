package com.svnavigatoru600.domain.eventcalendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.service.util.Localization;

/**
 * All allowed priorities which {@link CalendarEvent events} can have.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum PriorityType {

    // @formatter:off
    //       localizationCode                    cssClass
    HIGH    ("event-calendar.priorities.high",   "green"),
    NORMAL  ("event-calendar.priorities.normal", "blue"),
    LOW     ("event-calendar.priorities.low",    "gray"),
    ;
    // @formatter:on

    private final String localizationCode;
    private final String cssClass;

    private PriorityType(final String localizationCode, final String cssClass) {
        this.localizationCode = localizationCode;
        this.cssClass = cssClass;
    }

    /**
     * This method is based on the method <code>valueOf(String)</code>. The difference is that the first parameter is
     * not the name of the type, but the result of localization of its <code>localizationCode</code>.
     */
    public static PriorityType valueOfAccordingLocalization(final String localizedType, final MessageSource messageSource,
            final HttpServletRequest request) {
        if (localizedType == null) {
            // Throws the NullPointerException.
            PriorityType.valueOf(localizedType);
        }

        for (final PriorityType type : PriorityType.values()) {
            final String localized = Localization.findLocaleMessage(messageSource, request, type.localizationCode);
            if (localized.equals(localizedType)) {
                return type;
            }
        }
        // Throws the IllegalArgumentException.
        PriorityType.valueOf(localizedType);
        return null;
    }

    public String getLocalizationCode() {
        return localizationCode;
    }

    public String getCssClass() {
        return cssClass;
    }

}
