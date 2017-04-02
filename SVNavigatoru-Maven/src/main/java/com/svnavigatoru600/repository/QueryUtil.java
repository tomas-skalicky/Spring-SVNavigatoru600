package com.svnavigatoru600.repository;

/**
 * @author Tomas Skalicky
 * @since 02.04.2017
 */
public final class QueryUtil {

    private static final String SELECT = "SELECT * FROM %s x WHERE x.%s = :%s";
    private static final String DELETE = "DELETE FROM %s WHERE %s = :%s";

    private QueryUtil() {
    }

    public static String selectQuery(final String tableName, final String columnName, final String placeholder) {
        return String.format(SELECT, tableName, columnName, placeholder);
    }

    public static String deleteQuery(final String tableName, final String columnName, final String placeholder) {
        return String.format(DELETE, tableName, columnName, placeholder);
    }

}
