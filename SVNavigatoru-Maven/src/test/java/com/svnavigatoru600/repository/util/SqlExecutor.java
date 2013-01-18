package com.svnavigatoru600.repository.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.jdbc.ScriptRunner;

/**
 * Contains convenient functions for a work with SQL.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class SqlExecutor {

    /**
     * The {@link String} which determines that the text behind (on that line) should not be considered as an
     * executable SQL code, but as an inline comment.
     */
    static final String SQL_INLINE_COMMENT_BEGINNING = "--";
    /**
     * The {@link String} which determines that the text behind should not be considered as an executable SQL
     * code, but as a block comment. The comment ends up with the {@link #SQL_BLOCK_COMMENT_ENDING} sign.
     */
    static final String SQL_BLOCK_COMMENT_BEGINNING = "/*";
    /**
     * The end of the block SQL comment.
     */
    static final String SQL_BLOCK_COMMENT_ENDING = "*/";
    /**
     * The character that SQL statements end up with.
     */
    static final String SQL_COMMAND_ENDING = ";";

    private SqlExecutor() {
    }

    /**
     * Executes all code written in the given SQL file.
     * 
     * @param fileName
     *            The name of the SQL file.
     */
    public static void executeSqlFile(DataSource dataSource, String fileName) throws SQLException,
            IOException {
        Connection connection = null;
        BufferedReader fileReader = null;
        try {
            connection = dataSource.getConnection();
            ScriptRunner scriptRunner = new ScriptRunner(connection);

            fileReader = new BufferedReader(new FileReader(fileName));

            scriptRunner.runScript(fileReader);
        } finally {
            IOUtils.closeQuietly(fileReader);
            closeQuietly(connection);
        }
    }

    /**
     * Closes the given {@link Connection} in such a way that no exception will be propagated.
     */
    public static void closeQuietly(Connection connection) {
        if (connection == null) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException ex) {
            // "Quietly" means that no exception will be propagated to the caller.
            ex.printStackTrace();
        }
    }
}
