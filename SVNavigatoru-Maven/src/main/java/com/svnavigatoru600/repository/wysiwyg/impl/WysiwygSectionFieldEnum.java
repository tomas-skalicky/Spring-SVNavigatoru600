package com.svnavigatoru600.repository.wysiwyg.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.WysiwygSection WysiwygSection} class.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum WysiwygSectionFieldEnum {

    // @formatter:off
    //               columnName
    NAME            ("name"),
    LAST_SAVE_TIME  ("last_save_time"),
    SOURCE_CODE     ("source_code"),
    ;
    // @formatter:on

    /**
     * The name of a corresponding database column.
     */
    private final String columnName;

    private WysiwygSectionFieldEnum(final String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

}
