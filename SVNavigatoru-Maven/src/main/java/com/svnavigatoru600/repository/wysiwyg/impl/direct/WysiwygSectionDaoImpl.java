package com.svnavigatoru600.repository.wysiwyg.impl.direct;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionNameEnum;
import com.svnavigatoru600.repository.QueryUtil;
import com.svnavigatoru600.repository.WysiwygSectionDao;
import com.svnavigatoru600.repository.wysiwyg.impl.WysiwygSectionFieldEnum;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("wysiwygSectionDao")
@Transactional
public class WysiwygSectionDaoImpl extends NamedParameterJdbcDaoSupport implements WysiwygSectionDao {

    /**
     * Database table which provides a persistence of {@link WysiwygSection WysiwygSections}.
     */
    private static final String TABLE_NAME = "wysiwyg_sections";

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public WysiwygSectionDaoImpl(final DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public WysiwygSection findByName(final WysiwygSectionNameEnum name) {
        final String nameColumn = WysiwygSectionFieldEnum.NAME.getColumnName();
        final String query = QueryUtil.selectQuery(WysiwygSectionDaoImpl.TABLE_NAME, nameColumn, nameColumn);

        final Map<String, String> args = Collections.singletonMap(nameColumn, name.name());

        return getNamedParameterJdbcTemplate().queryForObject(query, args, new WysiwygSectionRowMapper());
    }

    /**
     * Maps properties of the given {@link WysiwygSection} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(final WysiwygSection section) {
        final Map<String, Object> parameters = Maps.newHashMap();
        parameters.put(WysiwygSectionFieldEnum.NAME.getColumnName(), section.getName());
        parameters.put(WysiwygSectionFieldEnum.LAST_SAVE_TIME.getColumnName(), section.getLastSaveTime());
        parameters.put(WysiwygSectionFieldEnum.SOURCE_CODE.getColumnName(), section.getSourceCode());
        return parameters;
    }

    @Override
    public void update(final WysiwygSection section) {
        final Date now = new Date();
        section.setLastSaveTime(now);

        final String nameColumn = WysiwygSectionFieldEnum.NAME.getColumnName();
        final String lastSaveTimeColumn = WysiwygSectionFieldEnum.LAST_SAVE_TIME.getColumnName();
        final String sourceCodeColumn = WysiwygSectionFieldEnum.SOURCE_CODE.getColumnName();
        final String query = String.format("UPDATE %s SET %s = :%s, %s = :%s WHERE %s = :%s",
                WysiwygSectionDaoImpl.TABLE_NAME, lastSaveTimeColumn, lastSaveTimeColumn, sourceCodeColumn,
                sourceCodeColumn, nameColumn, nameColumn);

        getNamedParameterJdbcTemplate().update(query, getNamedParameters(section));
    }

}
