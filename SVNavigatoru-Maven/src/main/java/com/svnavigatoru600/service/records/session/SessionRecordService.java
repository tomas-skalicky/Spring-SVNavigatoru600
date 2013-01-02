package com.svnavigatoru600.service.records.session;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.repository.records.SessionRecordDao;

/**
 * Provides convenient methods to work with {@link SessionRecord} objects.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class SessionRecordService {

    /**
     * The object which provides a persistence.
     */
    private final SessionRecordDao recordDao;

    /**
     * Constructor.
     */
    @Inject
    public SessionRecordService(SessionRecordDao recordDao) {
        this.recordDao = recordDao;
    }
}
