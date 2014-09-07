package com.svnavigatoru600.repository;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionName;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public interface WysiwygSectionDao {

    /**
     * Returns a {@link WysiwygSection} stored in the repository which has the given
     * {@link WysiwygSectionName name}.
     */
    WysiwygSection findByName(WysiwygSectionName name);

    /**
     * Updates the given {@link WysiwygSection} in the repository. The old version of this section should be
     * already stored there.
     */
    void update(WysiwygSection section);
}
