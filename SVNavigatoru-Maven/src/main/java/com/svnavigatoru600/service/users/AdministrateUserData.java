package com.svnavigatoru600.service.users;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;

/**
 * Holder of user's data when the administrator views and modifies them in the <i>user-administration.jsp</i>
 * form.
 * 
 * @author Tomas Skalicky
 */
@Service
public class AdministrateUserData {

    private User user = null;
    private String newPassword = "";
    private boolean[] newAuthorities = new boolean[AuthorityType.values().length];
    private Map<Integer, String> roleCheckboxId = null;
    private Map<Integer, String> localizedRoleCheckboxTitles = null;
    private boolean dataSaved = false;

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

    public boolean[] getNewAuthorities() {
        return this.newAuthorities;
    }

    public void setNewAuthorities(boolean[] newAuthorities) {
        this.newAuthorities = newAuthorities;
    }

    public Map<Integer, String> getRoleCheckboxId() {
        return this.roleCheckboxId;
    }

    public void setRoleCheckboxId(Map<Integer, String> roleCheckboxId) {
        this.roleCheckboxId = roleCheckboxId;
    }

    public Map<Integer, String> getLocalizedRoleCheckboxTitles() {
        return this.localizedRoleCheckboxTitles;
    }

    public void setLocalizedRoleCheckboxTitles(Map<Integer, String> localizedRoleCheckboxTitles) {
        this.localizedRoleCheckboxTitles = localizedRoleCheckboxTitles;
    }

    public boolean isDataSaved() {
        return this.dataSaved;
    }

    public void setDataSaved(boolean dataSaved) {
        this.dataSaved = dataSaved;
    }
}
