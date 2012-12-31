package com.svnavigatoru600.repository.util;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jpatterns.gof.FactoryMethodPattern;
import org.jpatterns.gof.FactoryMethodPattern.Creator;

/**
 * A parent of all classes which load a certain database from various sources, typically from SQL files.
 * "Load" means that they build up the whole schema and fill it up with a default data.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@FactoryMethodPattern(participants = { AbstractDatabaseLoader.class, HsqlDatabaseLoader.class })
@Creator
public abstract class AbstractDatabaseLoader {

    /**
     * Relative path to the test non-Java-source resources. It is relative to the root of the project.
     */
    protected static final String TEST_RESOURCES_RELATIVE_PATH = "src/test/resources";

    /**
     * Loads a database which is encapsulated in the given {@link DataSource}. Loads both DB schema and
     * default data.
     */
    public void loadDatabase(final DataSource dataSource) throws SQLException, IOException {
        final String createSchemaFile = this.getCreateSchemaFile(dataSource);
        SqlExecutor.executeSqlFile(dataSource, createSchemaFile);

        final String importDataFile = this.getImportDataFile(dataSource);
        SqlExecutor.executeSqlFile(dataSource, importDataFile);
    }

    /**
     * Returns a name of the SQL file which contains SQL commands for creating a database schema.
     */
    protected abstract String getCreateSchemaFile(DataSource dataSource);

    /**
     * Returns a name of the SQL file which contains SQL commands for importing a default data.
     */
    protected abstract String getImportDataFile(DataSource dataSource);
}
