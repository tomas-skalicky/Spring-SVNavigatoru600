package com.svnavigatoru600.viewmodel.users;

import java.util.List;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.users.User;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class SendNewPassword {

    private User user = null;
    private List<User> administrators = null;

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getAdministrators() {
        return this.administrators;
    }

    public void setAdministrators(List<User> administrators) {
        this.administrators = administrators;
    }
}
