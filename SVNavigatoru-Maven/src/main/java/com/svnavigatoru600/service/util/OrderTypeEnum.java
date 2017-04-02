package com.svnavigatoru600.service.util;

/**
 * Enumeration of common orders.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum OrderTypeEnum {

    // @formatter:off
    //               databaseCode
    ASCENDING       ("ASC"),
    DESCENDING      ("DESC"),
    WITHOUT_ORDER   (""),
    ;
    // @formatter:on

    private final String databaseCode;

    private OrderTypeEnum(final String databaseCode) {
        this.databaseCode = databaseCode;
    }

    /**
     * Gets a corresponding SQL snippet which has the same meaning as this {@link OrderTypeEnum}.
     */
    public String getDatabaseCode() {
        return databaseCode;
    }

}
