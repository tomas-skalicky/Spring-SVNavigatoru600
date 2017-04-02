package com.svnavigatoru600.repository.forum.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.forum.ForumThread Thread} class.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum ThreadFieldEnum {

    // @formatter:off
    ID              ("id"),
    NAME            ("name"),
    CREATION_TIME   ("creation_time"),
    AUTHOR_USERNAME ("author_username"),
    ;
    // @formatter:on

    /**
     * The name of a corresponding database column.
     */
    private final String columnName;

    private ThreadFieldEnum(final String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

}
