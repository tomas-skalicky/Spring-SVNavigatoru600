package com.svnavigatoru600.domain.users;

/**
 * All allowed roles (authorities) in the application.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum AuthorityType {

    /**
     * Parent of all regular users.
     */
    ROLE_REGISTERED_USER("user-roles.registered-user"),

    /**
     * Inherits from ROLE_REGISTERED_USER.
     */
    ROLE_MEMBER_OF_SV("user-roles.member-of-sv"),

    /**
     * Inherits from ROLE_MEMBER_OF_SV.
     */
    ROLE_MEMBER_OF_BOARD("user-roles.member-of-board"),

    /**
     * Inherits from ROLE_REGISTERED_USER.
     */
    ROLE_USER_ADMINISTRATOR("user-roles.user-administrator");

    private final String titleLocalizationCode;

    private AuthorityType(String titleLocalizationCode) {
        this.titleLocalizationCode = titleLocalizationCode;
    }

    /**
     * Gets the localization code of the title of this {@link AuthorityType}. Values which correspond to this
     * code are stored in <code>messages*.properties</code> files.
     */
    public String getTitleLocalizationCode() {
        return this.titleLocalizationCode;
    }

    /**
     * This getter is necessary for Spring Expression Language (SpEL).
     * 
     * @return The same value as {@link #ordinal()}.
     */
    public long getOrdinal() {
        return this.ordinal();
    }
}
