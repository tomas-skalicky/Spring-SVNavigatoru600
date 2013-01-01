package com.svnavigatoru600.repository.wysiwyg.impl.direct;

import java.util.Date;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionName;
import com.svnavigatoru600.repository.WysiwygSectionDao;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.wysiwyg.impl.WysiwygSectionField;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class WysiwygSectionDaoImpl extends SimpleJdbcDaoSupport implements WysiwygSectionDao {

    /**
     * Database table which provides a persistence of {@link WysiwygSection WysiwygSections}.
     */
    private static final String TABLE_NAME = PersistedClass.WysiwygSection.getTableName();

    @Override
    public WysiwygSection findByName(WysiwygSectionName name) {
        String query = String.format("SELECT * FROM %s s WHERE s.%s = ?", WysiwygSectionDaoImpl.TABLE_NAME,
                WysiwygSectionField.name.getColumnName());
        return this.getSimpleJdbcTemplate().queryForObject(query, new WysiwygSectionRowMapper(), name.name());
    }

    @Override
    public void update(WysiwygSection section) {
        Date now = new Date();
        section.setLastSaveTime(now);

        String query = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
                WysiwygSectionDaoImpl.TABLE_NAME, WysiwygSectionField.lastSaveTime.getColumnName(),
                WysiwygSectionField.sourceCode.getColumnName(), WysiwygSectionField.name.getColumnName());
        this.getSimpleJdbcTemplate().update(query, section.getLastSaveTime(), section.getSourceCode(),
                section.getName());
    }
}
