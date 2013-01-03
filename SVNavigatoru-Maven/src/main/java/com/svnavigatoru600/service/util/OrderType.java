package com.svnavigatoru600.service.util;

/**
 * Enumeration of common orders.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum OrderType {

    ASCENDING("ASC"), DESCENDING("DESC"), WITHOUT_ORDER("");

    private final String databaseCode;

    private OrderType(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    /**
     * Gets a corresponding SQL snippet which has the same meaning as this {@link OrderType}.
     */
    public String getDatabaseCode() {
        return this.databaseCode;
    }
}
