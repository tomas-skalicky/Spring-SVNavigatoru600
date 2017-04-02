package com.svnavigatoru600.repository.forum.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.forum.ForumContribution Contribution} class.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum ContributionFieldEnum {

    // @formatter:off
    ID              ("id"),
    THREAD_ID       ("thread_id", "thread.id"),
    TEXT            ("text"),
    CREATION_TIME   ("creation_time"),
    LAST_SAVE_TIME  ("last_save_time"),
    AUTHOR_USERNAME ("author_username"),
    ;
    // @formatter:on

    /**
     * The name of a corresponding database column.
     */
    private final String columnName;
    /**
     * The chain of fields. Used when the {@link ContributionFieldEnum} is not located directly in the
     * {@link com.svnavigatoru600.domain.forum.ForumContribution Contribution} class.
     */
    private final String fieldChain;

    private ContributionFieldEnum(final String columnName) {
        this(columnName, null);
    }

    private ContributionFieldEnum(final String columnName, final String fieldChain) {
        this.columnName = columnName;
        this.fieldChain = fieldChain;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getFieldChain() {
        return fieldChain;
    }

}
