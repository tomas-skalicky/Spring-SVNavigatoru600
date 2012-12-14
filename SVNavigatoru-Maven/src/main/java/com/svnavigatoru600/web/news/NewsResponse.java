package com.svnavigatoru600.web.news;

public abstract class NewsResponse {

    /**
     * Indicates whether the command has been successful, or not.
     */
    protected boolean successful = false;

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
        return successful;
    }
}
