package com.svnavigatoru600.repository.wysiwyg.impl.direct;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionName;
import com.svnavigatoru600.repository.WysiwygSectionDao;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.wysiwyg.impl.WysiwygSectionField;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class WysiwygSectionDaoImpl extends NamedParameterJdbcDaoSupport implements WysiwygSectionDao {

    /**
     * Database table which provides a persistence of {@link WysiwygSection WysiwygSections}.
     */
    private static final String TABLE_NAME = PersistedClass.WysiwygSection.getTableName();

    @Override
    public WysiwygSection findByName(WysiwygSectionName name) {
        String nameColumn = WysiwygSectionField.name.getColumnName();
        String query = String.format("SELECT * FROM %s s WHERE s.%s = :%s", WysiwygSectionDaoImpl.TABLE_NAME,
                nameColumn, nameColumn);

        Map<String, String> args = Collections.singletonMap(nameColumn, name.name());

        return this.getNamedParameterJdbcTemplate()
                .queryForObject(query, args, new WysiwygSectionRowMapper());
    }

    /**
     * Maps properties of the given {@link WysiwygSection} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(WysiwygSection section) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(WysiwygSectionField.name.getColumnName(), section.getName());
        parameters.put(WysiwygSectionField.lastSaveTime.getColumnName(), section.getLastSaveTime());
        parameters.put(WysiwygSectionField.sourceCode.getColumnName(), section.getSourceCode());
        return parameters;
    }

    @Override
    public void update(WysiwygSection section) {
        Date now = new Date();
        section.setLastSaveTime(now);

        String nameColumn = WysiwygSectionField.name.getColumnName();
        String lastSaveTimeColumn = WysiwygSectionField.lastSaveTime.getColumnName();
        String sourceCodeColumn = WysiwygSectionField.sourceCode.getColumnName();
        String query = String.format("UPDATE %s SET %s = :%s, %s = :%s WHERE %s = :%s",
                WysiwygSectionDaoImpl.TABLE_NAME, lastSaveTimeColumn, lastSaveTimeColumn, sourceCodeColumn,
                sourceCodeColumn, nameColumn, nameColumn);

        this.getNamedParameterJdbcTemplate().update(query, this.getNamedParameters(section));
    }
}
