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
    public void loadDatabase(DataSource dataSource) throws SQLException, IOException {
        SqlExecutor.executeSqlFile(dataSource, this.getCreateSchemaFile(dataSource));
        SqlExecutor.executeSqlFile(dataSource, this.getImportDataFile(dataSource));
    }

    /**
     * Loads a database which is encapsulated in the given {@link DataSource}. Loads both DB schema and
     * default data.
     */
    public void emptyDatabase(DataSource dataSource) throws SQLException, IOException {
        SqlExecutor.executeSqlFile(dataSource, this.getDropSchemaFile(dataSource));
    }

    /**
     * @return Name of the SQL file containing statements for creating a database schema.
     */
    protected abstract String getCreateSchemaFile(DataSource dataSource);

    /**
     * @return Name of the SQL file containing statements for importing default data.
     */
    protected abstract String getImportDataFile(DataSource dataSource);

    /**
     * @return Name of the SQL file containing statements for dropping a database schema.
     */
    protected abstract String getDropSchemaFile(DataSource dataSource);
}
