package com.svnavigatoru600.repository.wysiwyg.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.WysiwygSection WysiwygSection} class.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum WysiwygSectionField {

    name(NewsColumn.name), lastSaveTime(NewsColumn.last_save_time), sourceCode(NewsColumn.source_code);

    /**
     * The name of a corresponding database column.
     */
    private final NewsColumn column;

    private WysiwygSectionField(NewsColumn column) {
        this.column = column;
    }

    public String getColumnName() {
        return this.column.name();
    }

    /**
     * Names of the columns of the database table which contains {@link com.svnavigatoru600.domain.WysiwygSection
     * WYSIWYG sections}.
     */
    private enum NewsColumn {

        name, last_save_time, source_code
    }
}
