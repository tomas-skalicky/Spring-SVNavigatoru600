package com.svnavigatoru600.viewmodel.users;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.users.User;

/**
 * Holder of the full names of all {@link User} in the application. The class is used in the
 * <i>user-administration.jsp</i> form.
 * 
 * @author Tomas Skalicky
 */
@Service
public class ShowAllUsers {

    private List<User> users;
    private Map<User, String> localizedDeleteQuestions = null;
    private boolean userCreated = false;
    private boolean userDeleted = false;

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Map<User, String> getLocalizedDeleteQuestions() {
        return this.localizedDeleteQuestions;
    }

    public void setLocalizedDeleteQuestions(Map<User, String> localizedDeleteQuestions) {
        this.localizedDeleteQuestions = localizedDeleteQuestions;
    }

    public boolean isUserCreated() {
        return this.userCreated;
    }

    public void setUserCreated(boolean userCreated) {
        this.userCreated = userCreated;
    }

    public boolean isUserDeleted() {
        return this.userDeleted;
    }

    public void setUserDeleted(boolean userDeleted) {
        this.userDeleted = userDeleted;
    }
}
