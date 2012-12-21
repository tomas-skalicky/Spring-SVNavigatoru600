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

    @Override
    protected String getCreateSchemaFile(DataSource dataSource) {
        return TEST_RESOURCES_RELATIVE_PATH + "/db/hsqldb/create_schema.sql";
    }

    @Override
    protected String getImportDataFile(DataSource dataSource) {
        return TEST_RESOURCES_RELATIVE_PATH + "/db/hsqldb/load_test_data.sql";
    }
}
