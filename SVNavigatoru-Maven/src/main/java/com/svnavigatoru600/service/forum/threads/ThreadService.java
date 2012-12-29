package com.svnavigatoru600.service.forum.threads;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * Checks via convenient methods whether the currently logged-in user has rights to manipulate with a
 * particular {@link Thread}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class ThreadService {

    @PreAuthorize("hasPermission(#threadId, 'com.svnavigatoru600.domain.forum.Thread', 'edit')")
    public void canEdit(int threadId) {
    }

    @PreAuthorize("hasPermission(#threadId, 'com.svnavigatoru600.domain.forum.Thread', 'delete')")
    public void canDelete(int threadId) {
    }
}
