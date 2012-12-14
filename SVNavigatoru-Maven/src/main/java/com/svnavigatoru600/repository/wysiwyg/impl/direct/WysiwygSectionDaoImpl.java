package com.svnavigatoru600.repository.wysiwyg.impl.direct;

import java.util.Date;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionName;
import com.svnavigatoru600.repository.WysiwygSectionDao;

public class WysiwygSectionDaoImpl extends SimpleJdbcDaoSupport implements WysiwygSectionDao {

    private static final String TABLE_NAME = "wysiwyg_sections";

    @Override
    public WysiwygSection findByName(WysiwygSectionName name) {
        String query = String.format("SELECT * FROM %s s WHERE s.%s = ?", WysiwygSectionDaoImpl.TABLE_NAME,
                WysiwygSectionRowMapper.getColumn("name"));
        return this.getSimpleJdbcTemplate().queryForObject(query, new WysiwygSectionRowMapper(), name.name());
    }

    @Override
    public void update(WysiwygSection section) {
        Date now = new Date();
        section.setLastSaveTime(now);

        String query = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
                WysiwygSectionDaoImpl.TABLE_NAME, WysiwygSectionRowMapper.getColumn("lastSaveTime"),
                WysiwygSectionRowMapper.getColumn("sourceCode"), WysiwygSectionRowMapper.getColumn("name"));
        this.getSimpleJdbcTemplate().update(query, section.getLastSaveTime(), section.getSourceCode(),
                section.getName());
    }
}
