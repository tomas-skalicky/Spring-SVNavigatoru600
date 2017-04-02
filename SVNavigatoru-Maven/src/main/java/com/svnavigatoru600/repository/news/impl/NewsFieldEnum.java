package com.svnavigatoru600.repository.news.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.News News} class.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum NewsFieldEnum {

    // @formatter:off
    //               columnName
    ID              ("id"),
    TITLE           ("title"),
    TEXT            ("text"),
    CREATION_TIME   ("creation_time"),
    LAST_SAVE_TIME  ("last_save_time"),
    ;
    // @formatter:on

    /**
     * The name of a corresponding database column.
     */
    private final String columnName;

    private NewsFieldEnum(final String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

}
