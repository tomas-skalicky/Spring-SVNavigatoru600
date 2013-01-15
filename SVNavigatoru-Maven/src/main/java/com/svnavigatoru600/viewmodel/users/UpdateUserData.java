package com.svnavigatoru600.viewmodel.users;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.users.User;

/**
 * Holder of user's data when the user views and modifies them in the <i>user-account.jsp</i> form.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class UpdateUserData {

    private User user = null;
    private String newPassword = "";
    private String newPasswordConfirmation = "";
    /**
     * Integer cannot be used as a key type since such a map cannot be accessed by JSTL (see
     * http://stackoverflow.com/questions/924451/jstl-access-a-map-value-by-key).
     */
    private Map<Long, String> localizedNotificationCheckboxTitles = null;
    private boolean dataSaved = false;
    private boolean notificationsUnsubscribed = false;
    private String localizedUnsubscribedNotificationTitle = null;

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirmation() {
        return this.newPasswordConfirmation;
    }

    public void setNewPasswordConfirmation(String newPasswordConfirmation) {
        this.newPasswordConfirmation = newPasswordConfirmation;
    }

    public Map<Long, String> getLocalizedNotificationCheckboxTitles() {
        return this.localizedNotificationCheckboxTitles;
    }

    public void setLocalizedNotificationCheckboxTitles(Map<Long, String> localizedNotificationCheckboxTitles) {
        this.localizedNotificationCheckboxTitles = localizedNotificationCheckboxTitles;
    }

    public boolean isDataSaved() {
        return this.dataSaved;
    }

    public void setDataSaved(boolean dataSaved) {
        this.dataSaved = dataSaved;
    }

    public boolean isNotificationsUnsubscribed() {
        return this.notificationsUnsubscribed;
    }

    public void setNotificationsUnsubscribed(boolean notificationsUnsubscribed) {
        this.notificationsUnsubscribed = notificationsUnsubscribed;
    }

    public String getLocalizedUnsubscribedNotificationTitle() {
        return this.localizedUnsubscribedNotificationTitle;
    }

    public void setLocalizedUnsubscribedNotificationTitle(String localizedUnsubscribedNotificationTitle) {
        this.localizedUnsubscribedNotificationTitle = localizedUnsubscribedNotificationTitle;
    }
}
