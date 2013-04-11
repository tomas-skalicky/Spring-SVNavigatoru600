package com.svnavigatoru600.repository.wysiwyg.impl.hibernate;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionName;
import com.svnavigatoru600.repository.WysiwygSectionDao;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class WysiwygSectionDaoImpl extends HibernateDaoSupport implements WysiwygSectionDao {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public WysiwygSection findByName(WysiwygSectionName name) {
        this.logger.info(String.format("Load a section (name '%s')", name.name()));
        return getHibernateTemplate().load(WysiwygSection.class, name.name());
    }

    @Override
    public void update(WysiwygSection section) {
        Date now = new Date();
        section.setLastSaveTime(now);
        this.logger.info(String.format("Update a section (name '%s', lastSaveTime '%s')", section.getName(),
                section.getLastSaveTime().toString()));
        getHibernateTemplate().update(section);
    }
}
