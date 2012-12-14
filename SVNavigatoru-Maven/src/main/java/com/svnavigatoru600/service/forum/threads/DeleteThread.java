package com.svnavigatoru600.service.forum.threads;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class DeleteThread {

    @PreAuthorize("hasPermission(#threadId, 'com.svnavigatoru600.domain.forum.Thread', 'delete')")
    public void canDelete(int threadId) {
    }
}
