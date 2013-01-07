package com.svnavigatoru600.domain.users;

/**
 * All possible types of notification settings in the application.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum NotificationType {

    IN_NEWS("notifications.in-news"), IN_EVENTS("notifications.in-calendar-events"), IN_FORUM(
            "notifications.in-forum"), IN_OTHER_DOCUMENTS("notifications.in-other-documents"), IN_OTHER_SECTIONS(
            "notifications.in-other-sections");

    private final String titleLocalizationCode;
    private final int ordinal;

    private NotificationType(String titleLocalizationCode) {
        this.titleLocalizationCode = titleLocalizationCode;
        this.ordinal = this.ordinal();
    }

    /**
     * Gets the localization code of the title of this {@link NotificationType}. Values which correspond to
     * this code are stored in <code>messages*.properties</code> files.
     */
    public String getTitleLocalizationCode() {
        return this.titleLocalizationCode;
    }

    /**
     * @return The same value as {@link #ordinal()}.
     */
    public long getOrdinal() {
        return this.ordinal;
    }
}
