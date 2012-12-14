package com.svnavigatoru600.service.util;

/**
 * Enumeration of common orders.
 * 
 * @author Tomas Skalicky
 */
public enum OrderType {

    ASCENDING, DESCENDING, WITHOUT_ORDER;

    public String getDatabaseCode() {
        switch (this) {
        case ASCENDING:
            return "ASC";
        case DESCENDING:
            return "DESC";
        case WITHOUT_ORDER:
            return "";
        default:
            throw new RuntimeException("Unsupported order.");
        }
    }
}
