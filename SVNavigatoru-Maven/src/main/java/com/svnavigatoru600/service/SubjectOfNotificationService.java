package com.svnavigatoru600.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.users.User;

/**
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
public interface SubjectOfNotificationService {

    /**
     * Retrieves {@link User Users} who are to be notified either about a newly posted {@link Object}, or
     * about an updated one.
     */
    List<User> gainUsersToNotify();

    /**
     * Notifies all users which have corresponding rights by email about changes in the given updated
     * {@link Object}.
     */
    void notifyUsersOfUpdate(Object updatedObject, HttpServletRequest request, MessageSource messageSource);

    /**
     * Notifies all users which have corresponding rights by email about a creation of the given new
     * {@link Object}.
     */
    void notifyUsersOfCreation(Object newObject, HttpServletRequest request, MessageSource messageSource);
}
