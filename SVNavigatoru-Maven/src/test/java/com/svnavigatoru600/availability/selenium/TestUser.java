package com.svnavigatoru600.availability.selenium;

/**
 * Represents the user created only for the purposes of Selenium testing.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
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
    public String getUsername() {
        return this.username;
    }

    /**
     * @param username
     *            The new username of this {@link TestUser}.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The password of this {@link TestUser}.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @param password
     *            The new password of this {@link TestUser}.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
