package com.svnavigatoru600.domain.records;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.service.util.Localization;

/**
 * All types of {@link SessionRecord}s in the application.
 * 
 * @author Tomas Skalicky
 */
public enum SessionRecordType {

    SESSION_RECORD_OF_SV("session-records.sv"), SESSION_RECORD_OF_BOARD("session-records.board");

    private String localizationCode;

    public String getLocalizationCode() {
        return this.localizationCode;
    }

    private SessionRecordType(String localizationCode) {
        this.localizationCode = localizationCode;
    }

    /**
     * This method is based on the method <code>valueOf(String)</code>. The difference is that the first
     * parameter is not the name of the type, but the result of localization of its
     * <code>localizationCode</code>.
     */
    public static SessionRecordType valueOfAccordingLocalization(String localizedType,
            MessageSource messageSource, HttpServletRequest request) {
        if (localizedType == null) {
            // Throws the NullPointerException.
            SessionRecordType.valueOf(localizedType);
        }

        for (SessionRecordType type : SessionRecordType.values()) {
            String localized = Localization.findLocaleMessage(messageSource, request,
                    type.getLocalizationCode());
            if (localized.equals(localizedType)) {
                return type;
            }
        }
        // Throws the IllegalArgumentException.
        SessionRecordType.valueOf(localizedType);
        return null;
    }
}
