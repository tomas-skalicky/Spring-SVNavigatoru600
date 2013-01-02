package com.svnavigatoru600.web.forum.threads;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.service.forum.threads.ThreadService;
import com.svnavigatoru600.service.forum.threads.validator.AbstractThreadValidator;

/**
 * Parent of controllers which create and edit the {@link Thread Threads}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractNewEditThreadController extends AbstractThreadController {

    /**
     * Command used in /main-content/forum/threads/new-edit-thread.jsp.
     */
    public static final String COMMAND = "newEditThreadCommand";
    private Validator validator;

    public AbstractNewEditThreadController(ThreadService threadService, AbstractThreadValidator validator,
            MessageSource messageSource) {
        super(threadService, messageSource);
        this.validator = validator;
    }

    /**
     * Trivial getter
     */
    protected Validator getValidator() {
        return this.validator;
    }
}
