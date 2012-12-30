package com.svnavigatoru600.repository.impl;

/**
 * Names of classes which are persisted in a repository.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum PersistedClass {

    News(DatabaseTable.news), WysiwygSection(DatabaseTable.wysiwyg_sections), Thread(DatabaseTable.threads), Contribution(
            DatabaseTable.contributions);

    /**
     * The name of a corresponding database table.
     */
    private final DatabaseTable table;

    private PersistedClass(DatabaseTable table) {
        this.table = table;
    }

    public String getTableName() {
        return this.table.name();
    }

    /**
     * Names of the database tables which contain instances of {@link PersistedClass persisted classes}.
     */
    private enum DatabaseTable {

        news, wysiwyg_sections, threads, contributions
    }
}
