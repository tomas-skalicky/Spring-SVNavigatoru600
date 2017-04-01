package com.svnavigatoru600.repository.forum.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.forum.Contribution Contribution} class.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum ContributionField {

    id(ContributionColumn.id), threadId(ContributionColumn.thread_id, "thread.id"), text(
            ContributionColumn.text), creationTime(ContributionColumn.creation_time), lastSaveTime(
                    ContributionColumn.last_save_time), authorUsername(ContributionColumn.author_username);

    /**
     * The name of a corresponding database column.
     */
    private final ContributionColumn column;
    /**
     * The chain of fields. Used when the {@link ContributionField} is not located directly in the
     * {@link com.svnavigatoru600.domain.forum.Contribution Contribution} class.
     */
    private final String fieldChain;

    private ContributionField(ContributionColumn column) {
        this(column, null);
    }

    private ContributionField(ContributionColumn column, String fieldChain) {
        this.column = column;
        this.fieldChain = fieldChain;
    }

    public String getColumnName() {
        return this.column.name();
    }

    public String getFieldChain() {
        return this.fieldChain;
    }

    /**
     * Names of the columns of the database table which contains {@link com.svnavigatoru600.domain.forum.Contribution
     * contributions}.
     */
    private enum ContributionColumn {

        id, thread_id, text, creation_time, last_save_time, author_username
    }
}
