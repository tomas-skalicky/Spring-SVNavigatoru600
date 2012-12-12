package com.svnavigatoru600.selenium;

/**
 * Represents the user created only for the purposes of Selenium testing.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
public class TestUser {

    /**
     * The username of this {@link TestUser}.
     */
    private String username;
    /**
     * The password of this {@link TestUser}.
     */
    private String password;

    /**
     * @return The username of this {@link TestUser}.
     */
    public final String getUsername() {
        return this.username;
    }

    /**
     * @param username
     *            The new username of this {@link TestUser}.
     */
    public final void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return The password of this {@link TestUser}.
     */
    public final String getPassword() {
        return this.password;
    }

    /**
     * @param password
     *            The new password of this {@link TestUser}.
     */
    public final void setPassword(final String password) {
        this.password = password;
    }
}
