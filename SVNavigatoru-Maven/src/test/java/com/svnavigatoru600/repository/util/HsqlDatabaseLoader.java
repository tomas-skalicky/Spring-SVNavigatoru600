package com.svnavigatoru600.repository.util;

import javax.sql.DataSource;

import org.jpatterns.gof.FactoryMethodPattern;
import org.jpatterns.gof.FactoryMethodPattern.ConcreteCreator;

/**
 * Loads a HSQL database.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@FactoryMethodPattern(participants = { AbstractDatabaseLoader.class, HsqlDatabaseLoader.class })
@ConcreteCreator
public class HsqlDatabaseLoader extends AbstractDatabaseLoader {

    /**
     * Relative path of the folder containing all HSQLDB scripts for test purposes.
     */
    private static final String HSQLDB_SCRIPTS_HOME = TEST_RESOURCES_RELATIVE_PATH + "/db/hsqldb";

    @Override
    protected String getCreateSchemaFile(DataSource dataSource) {
        return HSQLDB_SCRIPTS_HOME + "/create_schema.sql";
    }

    @Override
    protected String getImportDataFile(DataSource dataSource) {
        return HSQLDB_SCRIPTS_HOME + "/load_test_data.sql";
    }

    @Override
    protected String getDropSchemaFile(DataSource dataSource) {
        return HSQLDB_SCRIPTS_HOME + "/drop_schema.sql";
    }
}
