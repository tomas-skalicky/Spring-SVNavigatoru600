package com.svnavigatoru600.web.forum.threads;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * Parent of all controllers which handle all operations upon {@link com.svnavigatoru600.domain.forum.Thread
 * Threads}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
@PreAuthorize("hasRole('ROLE_MEMBER_OF_SV')")
public abstract class AbstractThreadController extends AbstractPrivateSectionMetaController {

    protected static final String BASE_URL = "/forum/temata/";
    private final ThreadService threadService;
    private final MessageSource messageSource;

    public AbstractThreadController(ThreadService threadService, MessageSource messageSource) {
        this.threadService = threadService;
        this.messageSource = messageSource;
    }

    /**
     * Trivial getter
     */
    protected ThreadService getThreadService() {
        return this.threadService;
    }

    /**
     * Trivial getter
     */
    protected MessageSource getMessageSource() {
        return this.messageSource;
    }
}
