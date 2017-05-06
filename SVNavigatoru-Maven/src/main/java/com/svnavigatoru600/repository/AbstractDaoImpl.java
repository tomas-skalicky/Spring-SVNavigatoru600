/**
 *
 */
package com.svnavigatoru600.repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

/**
 * @author Tomas Skalicky
 * @since 06.05.2017
 */
public abstract class AbstractDaoImpl extends NamedParameterJdbcDaoSupport {

    @Inject
    private DataSource dataSource;

    @PostConstruct
    private void initDataSource() {
        setDataSource(dataSource);
    }

}
