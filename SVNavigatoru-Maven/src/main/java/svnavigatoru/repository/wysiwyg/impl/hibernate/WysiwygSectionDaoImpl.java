package svnavigatoru.repository.wysiwyg.impl.hibernate;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import svnavigatoru.domain.WysiwygSection;
import svnavigatoru.domain.WysiwygSectionName;
import svnavigatoru.repository.WysiwygSectionDao;

public class WysiwygSectionDaoImpl extends HibernateDaoSupport implements WysiwygSectionDao {

	/** Logger for this class and subclasses */
	protected final Log logger = LogFactory.getLog(this.getClass());

	public WysiwygSection findByName(WysiwygSectionName name) {
		this.logger.info(String.format("Load a section (name '%s')", name.name()));
		return this.getHibernateTemplate().load(WysiwygSection.class, name.name());
	}

	public void update(WysiwygSection section) {
		Date now = new Date();
		section.setLastSaveTime(now);
		this.logger.info(String.format("Update a section (name '%s', lastSaveTime '%s')", section.getName(), section
				.getLastSaveTime().toString()));
		this.getHibernateTemplate().update(section);
	}
}
