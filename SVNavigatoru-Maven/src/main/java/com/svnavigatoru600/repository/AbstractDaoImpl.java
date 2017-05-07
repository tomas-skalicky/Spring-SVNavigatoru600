/**
 *
 */
package com.svnavigatoru600.repository;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.svnavigatoru600.common.annotations.LogMethod;

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

    /**
     * @return the number of rows affected
     */
    @LogMethod
    protected int doUpdate(final String query, final Map<String, ?> namedParameters) {
        return getNamedParameterJdbcTemplate().update(query, namedParameters);
    }

}
