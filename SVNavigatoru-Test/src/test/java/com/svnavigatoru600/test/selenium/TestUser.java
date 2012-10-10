package com.svnavigatoru600.test.selenium;

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

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
