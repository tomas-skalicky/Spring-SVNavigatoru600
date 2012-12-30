package com.svnavigatoru600.repository.users.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.users.Authority Authority} class.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum AuthorityField {

    username(AuthorityColumn.username, "id.username"), authority(AuthorityColumn.authority, "id.authority");

    /**
     * The name of a corresponding database column.
     */
    private final AuthorityColumn column;
    /**
     * The chain of fields. Used when the {@link AuthorityField} is not located directly in the
     * {@link com.svnavigatoru600.domain.users.Authority Authority} class.
     */
    private final String fieldChain;

    private AuthorityField(AuthorityColumn column) {
        this(column, null);
    }

    private AuthorityField(AuthorityColumn column, String fieldChain) {
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
     * Names of the columns of the database table which contains
     * {@link com.svnavigatoru600.domain.users.Authority authorities}.
     */
    private enum AuthorityColumn {

        username, authority
    }
}
