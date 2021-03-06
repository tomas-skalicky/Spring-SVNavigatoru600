package com.svnavigatoru600.repository.wysiwyg.impl.direct;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.repository.wysiwyg.impl.WysiwygSectionFieldEnum;

/**
 * For more information, see {@link com.svnavigatoru600.repository.users.impl.direct.UserRowMapper UserRowMapper}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class WysiwygSectionRowMapper implements RowMapper<WysiwygSection> {

    @Override
    public WysiwygSection mapRow(ResultSet rs, int rowNum) throws SQLException {
        WysiwygSection section = new WysiwygSection();
        section.setName(rs.getString(WysiwygSectionFieldEnum.NAME.getColumnName()));
        section.setLastSaveTime(new Date(rs.getTimestamp(WysiwygSectionFieldEnum.LAST_SAVE_TIME.getColumnName()).getTime()));
        section.setSourceCode(rs.getString(WysiwygSectionFieldEnum.SOURCE_CODE.getColumnName()));
        return section;
    }
}
