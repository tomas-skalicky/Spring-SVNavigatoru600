package com.svnavigatoru600.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionName;
import com.svnavigatoru600.repository.WysiwygSectionDao;

/**
 * Provides convenient methods to work with {@link WysiwygSection} objects.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class WysiwygSectionService {

    /**
     * The object which provides a persistence.
     */
    private final WysiwygSectionDao sectionDao;

    /**
     * Constructor.
     */
    @Inject
    public WysiwygSectionService(WysiwygSectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    /**
     * Returns a {@link WysiwygSection} stored in the repository which has the given
     * {@link WysiwygSectionName name}.
     */
    public WysiwygSection findByName(WysiwygSectionName name) {
        return this.sectionDao.findByName(name);
    }

    /**
     * Updates the given {@link WysiwygSection} in the repository. The old version of this section should be
     * already stored there.
     */
    public void update(WysiwygSection section) {
        this.sectionDao.update(section);
    }
}
