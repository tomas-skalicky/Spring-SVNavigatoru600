package com.svnavigatoru600.repository.users.impl;

import com.svnavigatoru600.domain.users.NotificationTypeEnum;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.users.User User} class.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum UserFieldEnum {

    // @formatter:off
    //                               columnName
    USERNAME                        ("username"),
    PASSWORD                        ("password"),
    ENABLED                         ("enabled"),
    FIRST_NAME                      ("first_name"),
    LAST_NAME                       ("last_name"),
    EMAIL                           ("email"),
    PHONE                           ("phone"),
    IS_TEST_USER                    ("is_test_user"),
    AUTHORITIES                     (null),
    SUBSCRIBED_TO_NEWS              ("news_notifications"),
    SUBSCRIBED_TO_EVENTS            ("event_notifications"),
    SUBSCRIBED_TO_FORUM             ("forum_notifications"),
    SUBSCRIBED_TO_OTHER_DOCUMENTS   ("other_document_notifications"),
    SUBSCRIBED_TO_OTHER_SECTIONS    ("other_section_notifications"),
    SMTP_PORT                       ("smtp_port"),
    REDIRECT_EMAIL                  ("redirect_email");
    // @formatter:on

    /**
     * The name of a corresponding database column.
     */
    private final String columnName;

    private UserFieldEnum(final String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    /**
     * Gets the {@link UserFieldEnum} corresponding to the given {@link NotificationTypeEnum notificationType}.
     * <p>
     * Do NOT put these information to the {@link NotificationTypeEnum} enum though you could get rid of the switch. The
     * problem is that it would be a mixture of domain and repository tiers.
     */
    public static UserFieldEnum getSubscriptionField(final NotificationTypeEnum notificationType) {
        switch (notificationType) {
        case IN_NEWS:
            return SUBSCRIBED_TO_NEWS;
        case IN_EVENTS:
            return SUBSCRIBED_TO_EVENTS;
        case IN_FORUM:
            return SUBSCRIBED_TO_FORUM;
        case IN_OTHER_DOCUMENTS:
            return SUBSCRIBED_TO_OTHER_DOCUMENTS;
        case IN_OTHER_SECTIONS:
            return SUBSCRIBED_TO_OTHER_SECTIONS;
        default:
            throw new RuntimeException("Unsupported type of notification");
        }
    }
}
