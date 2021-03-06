package com.svnavigatoru600.web.forum.threads;

import org.springframework.context.MessageSource;
import org.springframework.validation.Validator;

import com.svnavigatoru600.service.forum.ThreadService;
import com.svnavigatoru600.viewmodel.forum.threads.validator.AbstractThreadValidator;

/**
 * Parent of controllers which create and edit the {@link com.svnavigatoru600.domain.forum.ForumThread Threads}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractNewEditThreadController extends AbstractThreadController {

    /**
     * Command used in /main-content/forum/threads/new-edit-thread.jsp.
     */
    public static final String COMMAND = "newEditThreadCommand";
    private final Validator validator;

    public AbstractNewEditThreadController(final ThreadService threadService, final AbstractThreadValidator validator,
            final MessageSource messageSource) {
        super(threadService, messageSource);
        this.validator = validator;
    }

    /**
     * Trivial getter
     */
    protected Validator getValidator() {
        return validator;
    }
}
