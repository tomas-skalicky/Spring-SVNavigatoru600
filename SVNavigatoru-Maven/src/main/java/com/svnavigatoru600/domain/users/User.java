package com.svnavigatoru600.domain.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.util.AuthorityUtils;
import com.svnavigatoru600.service.util.CheckboxUtils;
import com.svnavigatoru600.service.util.FullNameFormat;
import com.svnavigatoru600.service.util.UserFullNameFormatter;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 4756202473560293423L;

    private UserService userService;

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Updates the persisted copy of this object.
     */
    public void update() {
        this.userService.update(this);
    }

    /**
     * Default constructor. It is necessary because of /WEB-INF/model-beans/User.xml.
     */
    public User() {
    }

    private String username;
    private String password;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean isTestUser;
    private Set<GrantedAuthority> authorities;

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the full name of this {@link User} in the default {@link FullNameFormat}.
     */
    public String getFullName() {
        return this.getFullName(FullNameFormat.FIRST_LAST);
    }

    /**
     * Gets the full name of this {@link User} in the given {@link FullNameFormat}.
     */
    public String getFullName(FullNameFormat format) {
        return format.accept(new UserFullNameFormatter(this));
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isTestUser() {
        return this.isTestUser;
    }

    public void setTestUser(boolean isTestUser) {
        this.isTestUser = isTestUser;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    /**
     * Gets an array of names of authorities (= roles).
     */
    public String[] getAuthorityNames() {
        List<String> authorityNames = new ArrayList<String>();
        for (GrantedAuthority authority : this.authorities) {
            authorityNames.add(authority.getAuthority());
        }
        return authorityNames.toArray(new String[this.authorities.size()]);
    }

    /**
     * Indicates whether this {@link User} has the given authority.
     */
    public boolean hasAuthority(AuthorityType authority) {
        for (GrantedAuthority ownedAuthority : this.getAuthorities()) {
            if (ownedAuthority.getAuthority().equals(authority.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indicates whether this {@link User} can see published news.
     */
    public boolean canSeeNews() {
        return this.hasAuthority(AuthorityType.ROLE_MEMBER_OF_SV);
    }

    /**
     * Indicates whether this {@link User} can edit published news.
     */
    public boolean canEditNews() {
        return this.hasAuthority(AuthorityType.ROLE_MEMBER_OF_BOARD);
    }

    /**
     * Indicates whether this {@link User} can see all users of the application.
     */
    public boolean canSeeUsers() {
        return this.hasAuthority(AuthorityType.ROLE_USER_ADMINISTRATOR);
    }

    /**
     * Indicates whether this {@link User} can see his user account.
     */
    public boolean canSeeHisAccount() {
        return this.hasAuthority(AuthorityType.ROLE_REGISTERED_USER);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void setEmailToNullIfBlank() {
        if (StringUtils.isBlank(this.email)) {
            this.email = null;
        }
    }

    /**
     * Sets {@link User#getAuthorities() authorities} of this {@link User} according to the given indicators.
     * 
     * @param indicatorsOfNewAuthorities
     *            <code>true</code> in the index <code>x</code> in the array means that the authority with the
     *            {@link AuthorityType#ordinal() ordinal}<code> == x</code> has been selected as one of the
     *            new authorities of this user. And vice versa.
     * @return <code>true</code> if the new authorities are different from those before the method invocation;
     *         otherwise <code>false</code>.
     */
    public boolean updateAuthorities(boolean[] indicatorsOfNewAuthorities) {
        Set<GrantedAuthority> checkedAuthorities = AuthorityUtils.convertIndicatorsToAuthorities(
                indicatorsOfNewAuthorities, this.username);
        // The role ROLE_REGISTERED_USER is automatically added.
        checkedAuthorities.add(new Authority(this.username, AuthorityType.ROLE_REGISTERED_USER.name()));

        boolean authoritiesChanged = !CheckboxUtils.areSame(
                AuthorityUtils.getArrayOfCheckIndicators(checkedAuthorities),
                AuthorityUtils.getArrayOfCheckIndicators(this.getAuthorities()));

        this.setAuthorities(checkedAuthorities);
        return authoritiesChanged;
    }

    @Override
    public String toString() {
        return new StringBuilder("[username=").append(this.username).append(", password=")
                .append(this.password).append(", enabled=").append(this.enabled).append(", firstName=")
                .append(this.firstName).append(", lastName=").append(this.lastName).append(", email=")
                .append(this.email).append(", phone=").append(this.phone).append(", isTestUser=")
                .append(this.isTestUser).append(", authorities=").append(this.authorities).append("]")
                .toString();
    }
}
