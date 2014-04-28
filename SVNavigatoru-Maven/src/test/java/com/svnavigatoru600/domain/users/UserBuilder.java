package com.svnavigatoru600.domain.users;

import java.util.Set;

import org.jpatterns.gof.BuilderPattern;
import org.jpatterns.gof.BuilderPattern.Builder;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@BuilderPattern
@Builder
public final class UserBuilder {

    /**
     * Built {@link User} object.
     */
    private final User user = new User();

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder withUsername(String username) {
        this.user.setUsername(username);
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.user.setPassword(password);
        return this;
    }

    public UserBuilder enabled(boolean enabled) {
        this.user.setEnabled(enabled);
        return this;
    }

    public UserBuilder withFirstName(String firstName) {
        this.user.setFirstName(firstName);
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        this.user.setLastName(lastName);
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.user.setEmail(email);
        return this;
    }

    public UserBuilder withPhone(String phone) {
        this.user.setPhone(phone);
        return this;
    }

    public UserBuilder withSmtpPort(int smtpPort) {
        this.user.setSmtpPort(smtpPort);
        return this;
    }

    public UserBuilder forTestPurposes(boolean isTestUser) {
        this.user.setTestUser(isTestUser);
        return this;
    }

    public UserBuilder withAuthorities(Set<GrantedAuthority> authorities) {
        this.user.setAuthorities(authorities);
        return this;
    }

    public User build() {
        return this.user;
    }
}
