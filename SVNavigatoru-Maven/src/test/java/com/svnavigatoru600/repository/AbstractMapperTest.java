package com.svnavigatoru600.repository;

import javax.sql.DataSource;

import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.svnavigatoru600.repository.util.AbstractDatabaseLoader;
import com.svnavigatoru600.repository.util.HsqlDatabaseLoader;

/**
 * The parent of all MyBatis Mapper tests.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractMapperTest {

    /**
     * Application context which contains necessary beans.
     */
    protected static final ApplicationContext APPLICATION_CONTEXT = new FileSystemXmlApplicationContext(
            "classpath:applicationContext-business.xml");

    @Before
    public void setUpDatabase() throws Exception {
        prepareDatabase();
    }

    private static void prepareDatabase() throws Exception {
        synchronized (AbstractMapperTest.class) {
            DataSource dataSource = APPLICATION_CONTEXT.getBean(DataSource.class);
            getDatabaseLoader(dataSource).loadDatabase(dataSource);
        }
    }

    /**
     * Gets a {@link AbstractDatabaseLoader database loader} which is able to prepare a database for tests.
     */
    private static AbstractDatabaseLoader getDatabaseLoader(final DataSource dataSource) {
        if (dataSource instanceof ComboPooledDataSource) {
            final ComboPooledDataSource source = (ComboPooledDataSource) dataSource;
            final String driverClass = source.getDriverClass();

            if ("org.hsqldb.jdbcDriver".equals(driverClass)) {
                return new HsqlDatabaseLoader();
            } else {
                throw new IllegalArgumentException("Not supported driver class: " + driverClass);
            }
        } else {
            throw new IllegalArgumentException("Not supported implementation of javax.sql.DataSource: "
                    + dataSource.getClass());
        }
    }
}
