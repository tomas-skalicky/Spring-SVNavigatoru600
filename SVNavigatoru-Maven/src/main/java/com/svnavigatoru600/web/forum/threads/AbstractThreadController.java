package com.svnavigatoru600.web.forum.threads;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;

import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * Parent of all controllers which handle all operations upon {@link com.svnavigatoru600.domain.forum.Thread Threads}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@PreAuthorize("hasRole('ROLE_MEMBER_OF_SV')")
public abstract class AbstractThreadController extends AbstractPrivateSectionMetaController {

    private final ThreadService threadService;
    private final MessageSource messageSource;

    public AbstractThreadController(final ThreadService threadService, final MessageSource messageSource) {
        this.threadService = threadService;
        this.messageSource = messageSource;
    }

    /**
     * Trivial getter
     */
    protected ThreadService getThreadService() {
        return threadService;
    }

    /**
     * Trivial getter
     */
    protected MessageSource getMessageSource() {
        return messageSource;
    }
}
