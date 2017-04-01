package com.svnavigatoru600.web.users.administration;

/**
 * Parent of all controllers which create and edit {@link com.svnavigatoru600.domain.users.User users}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractNewEditUserController extends AbstractUserController {

    public static final String COMMAND = "userCommand";
}
