package com.svnavigatoru600.repository.users.impl;

/**
 * Names of the fields of the {@link com.svnavigatoru600.domain.users.Authority Authority} class.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum AuthorityFieldEnum {

    // @formatter:off
    //           columnName     fieldChain
    USERNAME    ("username",    "id.username"),
    AUTHORITY   ("authority",   "id.authority"),
    ;
    // @formatter:on

    /**
     * The name of a corresponding database column.
     */
    private final String columnName;
    /**
     * The chain of fields. Used when the {@link AuthorityFieldEnum} is not located directly in the
     * {@link com.svnavigatoru600.domain.users.Authority Authority} class.
     */
    private final String fieldChain;

    private AuthorityFieldEnum(final String columnName, final String fieldChain) {
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
