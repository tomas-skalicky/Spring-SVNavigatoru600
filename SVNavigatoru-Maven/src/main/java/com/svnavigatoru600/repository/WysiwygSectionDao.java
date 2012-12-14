package com.svnavigatoru600.repository;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionName;

public interface WysiwygSectionDao {

    /**
     * Returns a {@link WysiwygSection} stored in the repository which has the given <code>name</code>.
     */
    public WysiwygSection findByName(WysiwygSectionName name);

    /**
     * Updates the given <code>section</code> in the repository. The old version of the <code>section</code>
     * should be already stored there.
     */
    public void update(WysiwygSection section);
}
