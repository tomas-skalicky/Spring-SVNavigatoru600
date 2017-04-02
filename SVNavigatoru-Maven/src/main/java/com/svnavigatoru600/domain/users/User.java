package com.svnavigatoru600.domain.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.collect.Lists;
import com.svnavigatoru600.service.util.AuthorityUtils;
import com.svnavigatoru600.service.util.CheckboxUtils;
import com.svnavigatoru600.service.util.FullNameFormatEnum;
import com.svnavigatoru600.service.util.UserFullNameFormatterVisitor;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
    /**
     * If the server of recipient (=of this {@link User}) blocks emails from MochaHost, these emails are first sent to
     * recipient's Gmail address (created by admin) and then this Gmail redirects automatically all emails to the
     * recipient {@link User#email}.
     */
    private String redirectEmail;
    private String phone;
    private boolean isTestUser;
    /**
     * The following five subscribed* fields concern email notifications on certain group of news (of any type).
     */
    private boolean subscribedToNews;
    private boolean subscribedToEvents;
    private boolean subscribedToForum;
    private boolean subscribedToOtherDocuments;
    private boolean subscribedToOtherSections;
    private int smtpPort;
    private Set<GrantedAuthority> authorities;

    /**
     * Default constructor. It is necessary because of /WEB-INF/model-beans/User.xml. It initialises no property.
     */
    public User() {
    }

    /**
     * Initialises user's username (=login), password, flag if he is active, first name, last name, email, phone, flag
     * if he is a test user and authorities. Other properties are not touched.
     */
    public User(final String username, final String password, final boolean enabled, final String firstName, final String lastName, final String email,
            final String phone, final boolean isTestUser, final Set<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.isTestUser = isTestUser;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the full name of this {@link User} in the default {@link FullNameFormatEnum}.
     */
    public String getFullName() {
        return this.getFullName(FullNameFormatEnum.FIRST_LAST);
    }

    /**
     * Gets the full name of this {@link User} in the given {@link FullNameFormatEnum}.
     */
    public String getFullName(final FullNameFormatEnum format) {
        return format.accept(new UserFullNameFormatterVisitor(this));
    }

    public String getEmail() {
        return email;
    }

    public String getLowerCasedEmail() {
        if (email != null) {
            return email.toLowerCase();
        } else {
            return email;
        }
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getRedirectEmail() {
        return redirectEmail;
    }

    public void setRedirectEmail(final String redirectEmail) {
        this.redirectEmail = redirectEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public boolean isTestUser() {
        return isTestUser;
    }

    public void setTestUser(final boolean isTestUser) {
        this.isTestUser = isTestUser;
    }

    public boolean isSubscribedToNews() {
        return subscribedToNews;
    }

    public void setSubscribedToNews(final boolean subscribedToNews) {
        this.subscribedToNews = subscribedToNews;
    }

    public boolean isSubscribedToEvents() {
        return subscribedToEvents;
    }

    public void setSubscribedToEvents(final boolean subscribedToEvents) {
        this.subscribedToEvents = subscribedToEvents;
    }

    public boolean isSubscribedToForum() {
        return subscribedToForum;
    }

    public void setSubscribedToForum(final boolean subscribedToForum) {
        this.subscribedToForum = subscribedToForum;
    }

    public boolean isSubscribedToOtherDocuments() {
        return subscribedToOtherDocuments;
    }

    public void setSubscribedToOtherDocuments(final boolean subscribedToOtherDocuments) {
        this.subscribedToOtherDocuments = subscribedToOtherDocuments;
    }

    public boolean isSubscribedToOtherSections() {
        return subscribedToOtherSections;
    }

    public void setSubscribedToOtherSections(final boolean subscribedToOtherSections) {
        this.subscribedToOtherSections = subscribedToOtherSections;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(final Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    /**
     * Gets an array of names of authorities (= roles).
     */
    public String[] getAuthorityNames() {
        final List<String> authorityNames = Lists.newArrayList();
        for (final GrantedAuthority authority : authorities) {
            authorityNames.add(authority.getAuthority());
        }
        return authorityNames.toArray(new String[authorities.size()]);
    }

    /**
     * Returns an object of the authority with the given {@link AuthorityTypeEnum authorityType} if this {@link User} has
     * such a role (~ rights). Otherwise, returns <code>null</code>
     */
    private GrantedAuthority getAuthority(final AuthorityTypeEnum authorityType) {
        final String typeName = authorityType.name();

        // @formatter:off
        return getAuthorities().stream()
                .filter(ownedAuthority -> typeName.equals(ownedAuthority.getAuthority()))
                .findFirst()
                .orElse(null);
        // @formatter:on
    }

    /**
     * Indicates whether this {@link User} has the given authority.
     */
    public boolean hasAuthority(final AuthorityTypeEnum authority) {
        return getAuthority(authority) != null;
    }

    /**
     * Indicates whether this {@link User} can see published news.
     */
    public boolean canSeeNews() {
        return hasAuthority(AuthorityTypeEnum.ROLE_MEMBER_OF_SV);
    }

    /**
     * Indicates whether this {@link User} can edit published news.
     */
    public boolean canEditNews() {
        return hasAuthority(AuthorityTypeEnum.ROLE_MEMBER_OF_BOARD);
    }

    /**
     * Indicates whether this {@link User} can see all users of the application.
     */
    public boolean canSeeUsers() {
        return hasAuthority(AuthorityTypeEnum.ROLE_USER_ADMINISTRATOR);
    }

    /**
     * Indicates whether this {@link User} can see his user account.
     */
    public boolean canSeeHisAccount() {
        return hasAuthority(AuthorityTypeEnum.ROLE_REGISTERED_USER);
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(final int smtpPort) {
        this.smtpPort = smtpPort;
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
        if (StringUtils.isBlank(email)) {
            email = null;
        }
    }

    /**
     * Sets {@link User#getAuthorities() authorities} of this {@link User} according to the given flags.
     *
     * @param indicatorsOfNewAuthorities
     *            <code>true</code> in the index <code>x</code> in the array means that the authority with the
     *            {@link AuthorityTypeEnum#ordinal() ordinal}<code> == x</code> has been selected as one of the new
     *            authorities of this user. And vice versa.
     * @return <code>true</code> if the new authorities are different from those before the method invocation; otherwise
     *         <code>false</code>.
     */
    public boolean updateAuthorities(final boolean[] indicatorsOfNewAuthorities) {
        final Set<GrantedAuthority> checkedAuthorities = AuthorityUtils
                .convertIndicatorsToAuthorities(indicatorsOfNewAuthorities, username);
        // The role ROLE_REGISTERED_USER is automatically added.
        checkedAuthorities.add(new Authority(username, AuthorityTypeEnum.ROLE_REGISTERED_USER));

        boolean authoritiesChanged = true;
        final Collection<GrantedAuthority> currentAuthorities = getAuthorities();
        if (currentAuthorities != null) {
            authoritiesChanged = !CheckboxUtils.areSame(AuthorityUtils.getArrayOfCheckIndicators(checkedAuthorities),
                    AuthorityUtils.getArrayOfCheckIndicators(currentAuthorities));
        }

        setAuthorities(checkedAuthorities);
        return authoritiesChanged;
    }

    @Override
    public String toString() {
        // @formatter:off
        return new StringBuilder("[username=").append(username)
                .append(", password=").append(password)
                .append(", enabled=").append(enabled)
                .append(", firstName=").append(firstName)
                .append(", lastName=").append(lastName)
                .append(", email=").append(email)
                .append(", phone=").append(phone)
                .append(", isTestUser=").append(isTestUser)
                .append(", subscribedToNews=").append(subscribedToNews)
                .append(", subscribedToEvents=").append(subscribedToEvents)
                .append(", subscribedToForum=").append(subscribedToForum)
                .append(", subscribedToOtherDocuments=").append(subscribedToOtherDocuments)
                .append(", subscribedToOtherSections=").append(subscribedToOtherSections)
                .append(", smtpPort=").append(smtpPort)
                .append(", authorities=").append(authorities).append("]").toString();
        // @formatter:on
    }

    /**
     * Updates email, phone and subscription properties of this {@link User} according to the given user.
     *
     * @param sourceUser
     *            {@link User} which is used as a source.
     */
    public void copyEmailPhoneSubscriptions(final User sourceUser) {
        email = sourceUser.email;
        phone = sourceUser.phone;
        copySubscriptions(sourceUser);
    }

    /**
     * Updates subscription properties of this {@link User} according to the given user.
     *
     * @param sourceUser
     *            {@link User} which is used as a source.
     */
    public void copySubscriptions(final User sourceUser) {
        subscribedToNews = sourceUser.subscribedToNews;
        subscribedToEvents = sourceUser.subscribedToEvents;
        subscribedToForum = sourceUser.subscribedToForum;
        subscribedToOtherDocuments = sourceUser.subscribedToOtherDocuments;
        subscribedToOtherSections = sourceUser.subscribedToOtherSections;
    }

    /**
     * Sets all subscription properties of this {@link User} to {@code true}.
     */
    public void subscribeToEverything() {
        subscribedToNews = true;
        subscribedToEvents = true;
        subscribedToForum = true;
        subscribedToOtherDocuments = true;
        subscribedToOtherSections = true;
    }

    /**
     * Adds the specified {@link Authority} to this {@link User} if he has not this authority now. Otherwise, does
     * nothing.
     *
     * @param type
     *            Type of the authority which is to be added to this user.
     */
    public void addAuthority(final AuthorityTypeEnum type) {
        if (!hasAuthority(type)) {
            getAuthorities().add(new Authority(username, type));
        }
    }

    /**
     * Removes the specified {@link Authority} from this {@link User} if he has this authority now. Otherwise, does
     * nothing.
     *
     * @param type
     *            Type of the authority which is to be taken away from this user.
     */
    public void removeAuthority(final AuthorityTypeEnum type) {
        final GrantedAuthority authority = getAuthority(type);
        if (authority != null) {
            getAuthorities().remove(authority);
        }
    }

    /**
     * Filters all {@link User Users} from the given {@link List} out who have not email.
     *
     * @return All users from the <code>users</code> who have email.
     */
    public static List<User> filterWithNoEmailOut(final List<User> users) {
        final List<User> usersWithEmail = new ArrayList<>(users.size());
        for (final User user : users) {
            if (StringUtils.isNotBlank(user.getEmail())) {
                usersWithEmail.add(user);
            }
        }
        return usersWithEmail;
    }
}
