package com.svnavigatoru600.repository.news.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.News News} class.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum NewsField {

    id(NewsColumn.id), title(NewsColumn.title), text(NewsColumn.text), creationTime(
            NewsColumn.creation_time), lastSaveTime(NewsColumn.last_save_time);

    /**
     * The name of a corresponding database column.
     */
    private final NewsColumn column;

    private NewsField(NewsColumn column) {
        this.column = column;
    }

    public String getColumnName() {
        return this.column.name();
    }

    /**
     * Names of the columns of the database table which contains {@link com.svnavigatoru600.domain.News news}.
     */
    private enum NewsColumn {

        id, title, text, creation_time, last_save_time
    }
}
