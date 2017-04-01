package com.svnavigatoru600.viewmodel;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class SendNotification {

    private boolean status = false;
    private String checkboxTitle = null;

    public SendNotification() {
    }

    public SendNotification(boolean status, String checkboxTitle) {
        this.status = status;
        this.checkboxTitle = checkboxTitle;
    }

    public boolean isStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCheckboxTitle() {
        return this.checkboxTitle;
    }

    public void setCheckboxTitle(String checkboxTitle) {
        this.checkboxTitle = checkboxTitle;
    }

    @Override
    public String toString() {
        return new StringBuilder("[status=").append(this.status).append(", checkboxTitle=").append(this.checkboxTitle)
                .append("]").toString();
    }
}
