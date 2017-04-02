package com.svnavigatoru600.repository.forum.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.forum.ForumThread Thread} class.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum ThreadField {

    id(ThreadColumn.id), name(ThreadColumn.name), creationTime(ThreadColumn.creation_time), authorUsername(
            ThreadColumn.author_username);

    /**
     * The name of a corresponding database column.
     */
    private final ThreadColumn column;

    private ThreadField(ThreadColumn column) {
        this.column = column;
    }

    public String getColumnName() {
        return this.column.name();
    }

    /**
     * Names of the columns of the database table which contains {@link com.svnavigatoru600.domain.forum.ForumThread
     * threads}.
     */
    private enum ThreadColumn {

        id, name, creation_time, author_username
    }
}
