package com.svnavigatoru600.domain.eventcalendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.service.util.Localization;

/**
 * All allowed priorities which {@link CalendarEvent}s can have.
 * 
 * @author Tomas Skalicky
 */
public enum PriorityType {

    HIGH("event-calendar.priorities.high", "green"), NORMAL("event-calendar.priorities.normal", "blue"), LOW(
            "event-calendar.priorities.low", "gray");

    private String localizationCode;
    private String cssClass;

    public String getLocalizationCode() {
        return this.localizationCode;
    }

    public String getCssClass() {
        return this.cssClass;
    }

    private PriorityType(String localizationCode, String cssClass) {
        this.localizationCode = localizationCode;
        this.cssClass = cssClass;
    }

    /**
     * This method is based on the method <code>valueOf(String)</code>. The difference is that the first
     * parameter is not the name of the type, but the result of localization of its
     * <code>localizationCode</code>.
     */
    public static PriorityType valueOfAccordingLocalization(String localizedType,
            MessageSource messageSource, HttpServletRequest request) {
        if (localizedType == null) {
            // Throws the NullPointerException.
            PriorityType.valueOf(localizedType);
        }

        for (PriorityType type : PriorityType.values()) {
            String localized = Localization.findLocaleMessage(messageSource, request,
                    type.getLocalizationCode());
            if (localized.equals(localizedType)) {
                return type;
            }
        }
        // Throws the IllegalArgumentException.
        PriorityType.valueOf(localizedType);
        return null;
    }
}
