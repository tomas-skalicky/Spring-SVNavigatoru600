package com.svnavigatoru600.repository.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;

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
     * Executes the given SQL statement in the schema determined by the given {@link Connection}.
     */
    public static void executeSqlStatement(String sqlStatement, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sqlStatement);
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
        BufferedReader reader = null;

        try {
            connection = dataSource.getConnection();
            reader = new BufferedReader(new FileReader(fileName));

            StringBuilder sqlBuilder = new StringBuilder();
            boolean isInBlockComment = false;
            for (String nextLine = reader.readLine(); nextLine != null; nextLine = reader.readLine()) {
                // Ignores an inline comment which starts at the beginning of the current line.
                if (nextLine.indexOf(SQL_INLINE_COMMENT_BEGINNING) == 0) {
                    continue;
                }

                // Ignores a block comment which starts at the beginning of the current line.
                int blockCommentStart = nextLine.indexOf(SQL_BLOCK_COMMENT_BEGINNING);
                if (blockCommentStart == 0) {
                    isInBlockComment = true;
                }

                // Ignores a block comment which ends up at the end of the current line.
                int blockCommentEnd = nextLine.lastIndexOf(SQL_BLOCK_COMMENT_ENDING);
                int commentEndIndexIfExists = nextLine.length() - SQL_BLOCK_COMMENT_ENDING.length();
                boolean isLineEndBlockCommentEnd = (blockCommentEnd == commentEndIndexIfExists)
                        && (blockCommentEnd >= 0);
                if (isLineEndBlockCommentEnd) {
                    isInBlockComment = false;
                    continue;
                }

                // Ignores the current block comment.
                if (isInBlockComment) {
                    continue;
                }

                sqlBuilder.append(nextLine).append("\n");

                // Executes all complete statements.
                while (sqlBuilder.indexOf(SQL_COMMAND_ENDING) >= 0) {
                    String sql = sqlBuilder.toString();
                    int commandEndIndex = sql.indexOf(SQL_COMMAND_ENDING) + 1;
                    String sqlStatement = sql.substring(0, commandEndIndex);
                    executeSqlStatement(sqlStatement, connection);
                    sqlBuilder.delete(0, commandEndIndex);
                }
            }
        } finally {
            IOUtils.closeQuietly(reader);
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
