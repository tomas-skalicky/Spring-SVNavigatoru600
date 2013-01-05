package com.svnavigatoru600.repository.users.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.users.User User} class.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum UserField {

    username(UserColumn.username), password(UserColumn.password), enabled(UserColumn.enabled), firstName(
            UserColumn.first_name), lastName(UserColumn.last_name), email(UserColumn.email), phone(
            UserColumn.phone), isTestUser(UserColumn.is_test_user), authorities(null), subscribedToNews(
            UserColumn.news_notifications), subscribedToEvents(UserColumn.event_notifications), subscribedToForum(
            UserColumn.forum_notifications), subscribedToOtherDocuments(
            UserColumn.other_document_notifications), subscribedToOtherSections(
            UserColumn.other_section_notifications);

    /**
     * The name of a corresponding database column.
     */
    private final UserColumn column;

    private UserField(UserColumn column) {
        this.column = column;
    }

    public String getColumnName() {
        return this.column.name();
    }

    /**
     * Names of the columns of the database table which contains {@link com.svnavigatoru600.domain.users.User
     * users}.
     */
    private enum UserColumn {

        username, password, enabled, first_name, last_name, email, phone, is_test_user, news_notifications, event_notifications, forum_notifications, other_document_notifications, other_section_notifications
    }
}
