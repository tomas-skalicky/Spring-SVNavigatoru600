package com.svnavigatoru600.web.users.administration;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.service.users.validator.AbstractUserDataValidator;

/**
 * Parent of all controllers which create and edit {@link com.svnavigatoru600.domain.users.User users}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractNewEditUserController extends AbstractUserController {

    public static final String COMMAND = "userCommand";

    protected Validator validator;

    /**
     * Constructor.
     */
    public AbstractNewEditUserController(UserDao userDao, AbstractUserDataValidator validator,
            MessageSource messageSource) {
        super(userDao, messageSource);
        this.validator = validator;
    }
}
