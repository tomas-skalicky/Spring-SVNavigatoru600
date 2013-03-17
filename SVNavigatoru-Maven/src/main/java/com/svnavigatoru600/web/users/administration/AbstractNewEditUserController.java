package com.svnavigatoru600.web.users.administration;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import com.svnavigatoru600.service.users.AuthorityService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.viewmodel.users.validator.AbstractUserDataValidator;

/**
 * Parent of all controllers which create and edit {@link com.svnavigatoru600.domain.users.User users}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractNewEditUserController extends AbstractUserController {

    public static final String COMMAND = "userCommand";

    private final Validator validator;

    @Inject
    private AuthorityService authorityService;

    /**
     * Constructor.
     */
    public AbstractNewEditUserController(UserService userService, AbstractUserDataValidator validator,
            MessageSource messageSource) {
        super(userService, messageSource);
        this.validator = validator;
    }

    /**
     * Trivial getter
     */
    protected Validator getValidator() {
        return this.validator;
    }

    protected AuthorityService getAuthorityService() {
        return this.authorityService;
    }
}
