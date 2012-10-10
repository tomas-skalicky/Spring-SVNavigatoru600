package svnavigatoru.domain.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import svnavigatoru.repository.users.UserDao;
import svnavigatoru.service.util.FullNameFormat;

public class User implements UserDetails, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4756202473560293423L;

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void update() {
		this.userDao.update(this);
	}

	/**
	 * Default constructor. It is necessary because of
	 * /WEB-INF/model-beans/User.xml.
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
	private Set<GrantedAuthority> authorities;

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
	 * Gets the full name of this {@link User} in the default
	 * {@link FullNameFormat}.
	 */
	public String getFullName() {
		return this.getFullName(FullNameFormat.FIRST_LAST);
	}

	/**
	 * Gets the full name of this {@link User} in the given
	 * {@link FullNameFormat}.
	 */
	public String getFullName(FullNameFormat format) {
		switch (format) {
		case FIRST_LAST:
			return String.format("%s %s", this.firstName, this.lastName);
		case LAST_FIRST:
			return String.format("%s %s", this.lastName, this.firstName);
		case LAST_COMMA_FIRST:
			return String.format("%s, %s", this.lastName, this.firstName);
		default:
			throw new RuntimeException("Unsupported format of the full name.");
		}
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

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public void setEmailToNullIfBlank() {
		if (StringUtils.isBlank(this.email)) {
			this.email = null;
		}
	}
}
