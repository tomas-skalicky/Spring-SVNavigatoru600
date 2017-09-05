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

    public UserBuilder withUsername(final String username) {
        user.setUsername(username);
        return this;
    }

    public UserBuilder withPassword(final String password) {
        user.setPassword(password);
        return this;
    }

    public UserBuilder enabled(final boolean enabled) {
        user.setEnabled(enabled);
        return this;
    }

    public UserBuilder withFirstName(final String firstName) {
        user.setFirstName(firstName);
        return this;
    }

    public UserBuilder withLastName(final String lastName) {
        user.setLastName(lastName);
        return this;
    }

    public UserBuilder withEmail(final String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder withPhone(final String phone) {
        user.setPhone(phone);
        return this;
    }

    public UserBuilder withSmtpPort(final Integer smtpPort) {
        user.setSmtpPort(smtpPort);
        return this;
    }

    public UserBuilder forTestPurposes(final boolean isTestUser) {
        user.setTestUser(isTestUser);
        return this;
    }

    public UserBuilder withAuthorities(final Set<GrantedAuthority> authorities) {
        user.setAuthorities(authorities);
        return this;
    }

    public User build() {
        return user;
    }
}
