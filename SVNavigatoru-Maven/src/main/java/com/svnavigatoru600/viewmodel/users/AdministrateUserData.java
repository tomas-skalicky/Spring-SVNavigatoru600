package com.svnavigatoru600.viewmodel.users;

import java.util.Map;

import com.svnavigatoru600.domain.users.AuthorityTypeEnum;
import com.svnavigatoru600.domain.users.User;

/**
 * Holder of user's data when the administrator views and modifies them in the <i>user-administration.jsp</i> form.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class AdministrateUserData {

    private User user = null;
    private String newPassword = "";
    private boolean[] newAuthorities = new boolean[AuthorityTypeEnum.values().length];
    /**
     * Integer cannot be used as a key type since such a map cannot be accessed by JSTL (see
     * http://stackoverflow.com/questions/924451/jstl-access-a-map-value-by-key).
     */
    private Map<Long, String> roleCheckboxId = null;
    private Map<Long, String> localizedRoleCheckboxTitles = null;
    private boolean dataSaved = false;

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

    public boolean[] getNewAuthorities() {
        return newAuthorities;
    }

    public void setNewAuthorities(final boolean[] newAuthorities) {
        this.newAuthorities = newAuthorities;
    }

    public Map<Long, String> getRoleCheckboxId() {
        return roleCheckboxId;
    }

    public void setRoleCheckboxId(final Map<Long, String> roleCheckboxId) {
        this.roleCheckboxId = roleCheckboxId;
    }

    public Map<Long, String> getLocalizedRoleCheckboxTitles() {
        return localizedRoleCheckboxTitles;
    }

    public void setLocalizedRoleCheckboxTitles(final Map<Long, String> localizedRoleCheckboxTitles) {
        this.localizedRoleCheckboxTitles = localizedRoleCheckboxTitles;
    }

    public boolean isDataSaved() {
        return dataSaved;
    }

    public void setDataSaved(final boolean dataSaved) {
        this.dataSaved = dataSaved;
    }
}
