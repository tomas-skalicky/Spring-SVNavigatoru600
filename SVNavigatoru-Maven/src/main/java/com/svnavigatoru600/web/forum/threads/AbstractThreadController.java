package com.svnavigatoru600.web.forum.threads;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * Parent of all controllers which handle all operations upon the {@link Thread} s.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
@PreAuthorize("hasRole('ROLE_MEMBER_OF_SV')")
public abstract class AbstractThreadController extends AbstractPrivateSectionMetaController {

    protected static final String BASE_URL = "/forum/temata/";
    protected ThreadDao threadDao;
    protected MessageSource messageSource;

    public AbstractThreadController(ThreadDao threadDao, MessageSource messageSource) {
        this.threadDao = threadDao;
        this.messageSource = messageSource;
    }
}
