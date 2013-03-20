package com.svnavigatoru600.web.users.administration;

import org.springframework.stereotype.Controller;

/**
 * Parent of all controllers which create and edit {@link com.svnavigatoru600.domain.users.User users}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractNewEditUserController extends AbstractUserController {

    public static final String COMMAND = "userCommand";
}
