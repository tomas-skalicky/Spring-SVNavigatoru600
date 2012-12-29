package com.svnavigatoru600.service.forum.contributions;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * Checks via convenient methods whether the currently logged-in user has rights to manipulate with a
 * particular {@link Thread}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public final class ContributionService {

    @PreAuthorize("hasPermission(#contributionId, 'com.svnavigatoru600.domain.forum.Contribution', 'edit')")
    public void canEdit(int contributionId) {
    }

    @PreAuthorize("hasPermission(#contributionId, 'com.svnavigatoru600.domain.forum.Contribution', 'delete')")
    public void canDelete(int contributionId) {
    }
}
