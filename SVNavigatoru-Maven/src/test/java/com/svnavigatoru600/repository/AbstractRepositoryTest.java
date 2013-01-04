package com.svnavigatoru600.repository;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.mchange.v2.c3p0.ComboPooledDataSource;
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
    protected static final ApplicationContext APPLICATION_CONTEXT = new FileSystemXmlApplicationContext(
            "classpath:applicationContext-business.xml");
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
        this.dataSource = APPLICATION_CONTEXT.getBean(DataSource.class);
        prepareDatabase(this.dataSource);
        this.sqlConnection = this.dataSource.getConnection();
        this.sqlConnection.setAutoCommit(DEFAULT_AUTO_COMMIT);
    }

    /**
     * NOTE: {@link After} method is guaranteed to run even if a {@link Before} or {@link org.junit.Test Test}
     * method throws an exception (see
     * http://stackoverflow.com/questions/9490569/does-teardown-get-called-if-test
     * -case-throws-exception-junit).
     */
    @After
    public void closeConnection() throws Exception {
        this.sqlConnection.close();
        emptyDatabase(this.dataSource);
    }

    private static void prepareDatabase(DataSource dataSource) throws Exception {
        synchronized (DataSource.class) {
            getDatabaseLoader(dataSource).loadDatabase(dataSource);
        }
    }

    private static void emptyDatabase(DataSource dataSource) throws Exception {
        synchronized (DataSource.class) {
            getDatabaseLoader(dataSource).emptyDatabase(dataSource);
        }
    }

    /**
     * Gets a {@link AbstractDatabaseLoader database loader} which is able to prepare a database for tests.
     */
    private static AbstractDatabaseLoader getDatabaseLoader(DataSource dataSource) {
        if (dataSource instanceof ComboPooledDataSource) {
            ComboPooledDataSource source = (ComboPooledDataSource) dataSource;
            String driverClass = source.getDriverClass();

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