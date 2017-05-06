package com.svnavigatoru600.repository;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.svnavigatoru600.common.config.WebAppConfig;
import com.svnavigatoru600.repository.util.AbstractDatabaseLoader;
import com.svnavigatoru600.repository.util.HsqlDatabaseLoader;

/**
 * The parent of all tests of DAO interfaces (MyBatis Mapper respectively).
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractRepositoryTest {

    /**
     * Application context which contains necessary beans.
     */
    protected static final ApplicationContext APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(
            WebAppConfig.class);
    /**
     * Utility functions for retrieving DAO objects or creation default representants of persisted classes.
     */
    protected static final RepositoryTestUtils TEST_UTILS = new RepositoryTestUtils(APPLICATION_CONTEXT);
    /**
     * Data source initialised in an {@link Before} method.
     */
    private DataSource dataSource = null;
    /**
     * Default setting of connection's auto commit mode.
     */
    protected static final boolean DEFAULT_AUTO_COMMIT = true;
    /**
     * Connection to a database which is opened at the very beginning of each test and closed at its very end.
     */
    private Connection sqlConnection = null;

    @Before
    public void initializeConnection() throws Exception {
        dataSource = APPLICATION_CONTEXT.getBean(DataSource.class);
        prepareDatabase(dataSource);
        sqlConnection = dataSource.getConnection();
        sqlConnection.setAutoCommit(DEFAULT_AUTO_COMMIT);
    }

    /**
     * NOTE: All {@link After} methods are guaranteed to run even if {@link Before} or {@link org.junit.Test Test}
     * method throws an exception (see http://stackoverflow.com/questions/9490569/does-teardown-get-called-if-test
     * -case-throws-exception-junit).
     */
    @After
    public void closeConnection() throws Exception {
        sqlConnection.close();
        emptyDatabase(dataSource);
    }

    private static void prepareDatabase(final DataSource dataSource) throws Exception {
        synchronized (DataSource.class) {
            getDatabaseLoader(dataSource).loadDatabase(dataSource);
        }
    }

    private static void emptyDatabase(final DataSource dataSource) throws Exception {
        synchronized (DataSource.class) {
            getDatabaseLoader(dataSource).emptyDatabase(dataSource);
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
            throw new IllegalArgumentException(
                    "Not supported implementation of javax.sql.DataSource: " + dataSource.getClass());
        }
    }
}
