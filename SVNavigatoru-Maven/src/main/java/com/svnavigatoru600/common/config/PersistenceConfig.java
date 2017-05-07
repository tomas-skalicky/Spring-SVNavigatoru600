package com.svnavigatoru600.common.config;

import java.beans.PropertyVetoException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.svnavigatoru600.common.constants.CommonConstants;
import com.svnavigatoru600.common.settings.JdbcSettings;

/**
 * @author Tomas Skalicky
 * @since 06.05.2017
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = CommonConstants.USE_CGLIB_PROXY)
@ComponentScan(basePackageClasses = com.svnavigatoru600.repository.PackageMarker.class)
@Import(CommonConfig.class)
public class PersistenceConfig {

    @Inject
    private JdbcSettings jdbcSettings;

    @Bean
    DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * See documentation of c3p0 on http://www.mchange.com/projects/c3p0/#basic_pool_configuration
     */
    @Bean
    DataSource dataSource() {
        final ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(jdbcSettings.getDriverClassName());
        } catch (final PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        dataSource.setJdbcUrl(jdbcSettings.getUrl());
        dataSource.setUser(jdbcSettings.getUsername());
        dataSource.setPassword(jdbcSettings.getPassword());

        // Number of connections a pool will try to acquire upon startup. Should be between
        // minPoolSize and maxPoolSize.
        dataSource.setInitialPoolSize(3);

        // Minimum number of connections a pool will maintain at any given time.
        dataSource.setMinPoolSize(3);

        // Maximum number of connections a pool will maintain at any given time.
        dataSource.setMaxPoolSize(10);

        // Determines how many connections at a time c3p0 will try to acquire when the
        // pool is exhausted, i.e. all create connections are engaged and the current
        // number of connections is less than maxPoolSize.
        dataSource.setAcquireIncrement(3);

        // Seconds a connection can remain pooled but unused before being discarded.
        // Zero means idle connections never expire.
        dataSource.setMaxIdleTime(0);

        // If this is a number greater than 0, c3p0 will test all idle,
        // pooled but unchecked-out connections, every this number of seconds.
        dataSource.setIdleConnectionTestPeriod(3000);

        // If true, an operation will be performed asynchronously at every connection checkin
        // to verify that the connection is valid. Use in combination with idleConnectionTestPeriod
        // for quite reliable, always asynchronous connection testing.
        dataSource.setTestConnectionOnCheckin(true);

        // The size of c3p0's global PreparedStatement cache. If both maxStatements and
        // maxStatementsPerConnection are zero, statement caching will not be enabled.
        // If maxStatements is zero but maxStatementsPerConnection is a non-zero value,
        // statement caching will be enabled, but no global limit will be enforced,
        // only the per-connection maximum. maxStatements controls the total number of Statements cached,
        // for all Connections. If set, it should be a fairly large number,
        // as each pooled Connection requires its own, distinct flock of cached statements.
        // As a guide, consider how many distinct PreparedStatements are used frequently
        // in your application, and multiply that number by maxPoolSize to arrive
        // at an appropriate value. Though maxStatements is the JDBC standard parameter
        // for controlling statement caching, users may find c3p0's alternative
        // maxStatementsPerConnection more intuitive to use.
        dataSource.setMaxStatements(200);
        return dataSource;
    }

}
