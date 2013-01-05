package com.svnavigatoru600.service.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.svnavigatoru600.domain.users.User;

/**
 * Provides a set of static functions related to users.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class UserUtils {

    private UserUtils() {
    }

    /**
     * Indicates whether the current {@link User user} in the application is logged.
     */
    public static boolean isLogged() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) && (auth.getPrincipal() instanceof User);
    }

    /**
     * Gets the {@link User} who is currently logged in the application.
     * 
     * <b>Precondition:</b> The {@link UserUtils#isLogged() isLogger} function must be <code>true</code>.
     */
    public static User getLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
