package com.svnavigatoru600.domain.users;

/**
 * All allowed roles (authorities) in the application.
 * 
 * @author Tomas Skalicky
 */
public enum AuthorityType {

    /**
     * Parent of all regular users.
     */
    ROLE_REGISTERED_USER,

    /**
     * Inherits from ROLE_REGISTERED_USER.
     */
    ROLE_MEMBER_OF_SV,

    /**
     * Inherits from ROLE_MEMBER_OF_SV.
     */
    ROLE_MEMBER_OF_BOARD,

    /**
     * Inherits from ROLE_REGISTERED_USER.
     */
    ROLE_USER_ADMINISTRATOR
}
