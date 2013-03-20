package com.svnavigatoru600.web.users.administration;

import javax.inject.Inject;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * Parent of all controllers which handle all operations concerning administration of
 * {@link com.svnavigatoru600.domain.users.User users}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractUserController extends AbstractPrivateSectionMetaController {

    private MessageSource messageSource = null;

    /**
     * Constructor.
     */
    @Inject
    public AbstractUserController() {
        LogFactory.getLog(this.getClass()).debug("The UserController object created.");
    }

    /**
     * Trivial getter
     */
    protected MessageSource getMessageSource() {
        return this.messageSource;
    }

    /**
     * Trivial setter
     */
    @Inject
    @Required
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
