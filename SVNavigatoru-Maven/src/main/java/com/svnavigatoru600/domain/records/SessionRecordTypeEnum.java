package com.svnavigatoru600.domain.records;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.service.util.Localization;

/**
 * All types of {@link SessionRecord SessionRecords} in the application.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum SessionRecordTypeEnum {

    // @formatter:off
    SESSION_RECORD_OF_SV    ("session-records.sv"),
    SESSION_RECORD_OF_BOARD ("session-records.board"),
    ;
    // @formatter:on

    private final String localizationCode;

    private SessionRecordTypeEnum(final String localizationCode) {
        this.localizationCode = localizationCode;
    }

    /**
     * This method is based on the method <code>valueOf(String)</code>. The difference is that the first parameter is
     * not the name of the type, but the result of localization of its <code>localizationCode</code>.
     */
    public static SessionRecordTypeEnum valueOfAccordingLocalization(final String localizedType,
            final MessageSource messageSource, final HttpServletRequest request) {
        if (localizedType == null) {
            // Throws the NullPointerException.
            SessionRecordTypeEnum.valueOf(localizedType);
        }

        for (final SessionRecordTypeEnum type : SessionRecordTypeEnum.values()) {
            final String localized = Localization.findLocaleMessage(messageSource, request, type.localizationCode);
            if (localized.equals(localizedType)) {
                return type;
            }
        }
        // Throws the IllegalArgumentException.
        SessionRecordTypeEnum.valueOf(localizedType);
        return null;
    }

    public String getLocalizationCode() {
        return localizationCode;
    }

}
