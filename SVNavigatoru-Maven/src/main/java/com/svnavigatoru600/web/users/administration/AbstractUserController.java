package com.svnavigatoru600.web.users.administration;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * Parent of all controllers which handle all operations concerning administration of
 * {@link com.svnavigatoru600.domain.users.User users}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractUserController extends AbstractPrivateSectionMetaController {

    private UserService userService = null;
    private MessageSource messageSource = null;

    /**
     * Constructor.
     */
    @Inject
    public AbstractUserController(UserService userService, MessageSource messageSource) {
        LogFactory.getLog(this.getClass()).debug("The UserController object created.");
        this.userService = userService;
        this.messageSource = messageSource;
    }

    /**
     * Trivial getter
     */
    protected UserService getUserService() {
        return this.userService;
    }

    /**
     * Trivial getter
     */
    protected MessageSource getMessageSource() {
        return this.messageSource;
    }

    /**
     * Gets a {@link Map} which for each {@link AuthorityType} (more precisely its ordinal) contains an
     * appropriate ID of its checkbox.
     */
    protected Map<Integer, String> getRoleCheckboxId() {
        String commonIdFormat = "newAuthorities[%s]";
        Map<Integer, String> checkboxIds = new HashMap<Integer, String>();

        for (AuthorityType type : AuthorityType.values()) {
            int typeOrdinal = type.ordinal();
            checkboxIds.put(typeOrdinal, String.format(commonIdFormat, typeOrdinal));
        }
        return checkboxIds;
    }
}
