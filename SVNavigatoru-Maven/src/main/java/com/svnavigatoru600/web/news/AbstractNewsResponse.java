package com.svnavigatoru600.web.news;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractNewsResponse {

    /**
     * Indicates whether the command has been successful, or not.
     */
    private boolean successful = false;

    /**
     * Sets up everything in a way that the processing of the command has been successful.
     */
    public void setSuccess() {
        this.successful = true;
    }

    /**
     * Sets up everything in a way that the processing of the command has failed.
     */
    public void setFail() {
        this.successful = false;
    }

    public boolean isSuccessful() {
        return this.successful;
    }
}
