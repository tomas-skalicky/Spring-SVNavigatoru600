package com.svnavigatoru600.repository;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionName;

@MapperInterface
public interface WysiwygSectionDao {

    /**
     * Returns a {@link WysiwygSection} stored in the repository which has the given <code>name</code>.
     */
    WysiwygSection findByName(WysiwygSectionName name);

    /**
     * Updates the given <code>section</code> in the repository. The old version of the <code>section</code>
     * should be already stored there.
     */
    void update(WysiwygSection section);
}
