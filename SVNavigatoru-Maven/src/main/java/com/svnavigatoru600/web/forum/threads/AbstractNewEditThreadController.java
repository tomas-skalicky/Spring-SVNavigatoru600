package com.svnavigatoru600.web.forum.threads;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.service.forum.threads.validator.AbstractThreadValidator;

/**
 * Parent of controllers which create and edit the {@link Thread}s.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractNewEditThreadController extends AbstractThreadController {

    /**
     * Command used in /main-content/forum/threads/new-edit-thread.jsp.
     */
    public static final String COMMAND = "newEditThreadCommand";
    protected Validator validator;

    public AbstractNewEditThreadController(ThreadDao threadDao, AbstractThreadValidator validator,
            MessageSource messageSource) {
        super(threadDao, messageSource);
        this.validator = validator;
    }
}
