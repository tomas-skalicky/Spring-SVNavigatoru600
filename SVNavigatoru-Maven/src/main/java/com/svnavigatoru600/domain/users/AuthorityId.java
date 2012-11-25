package com.svnavigatoru600.domain.users;

import java.io.Serializable;

/**
 * Helper with the representation of the composite key of the {@link Authority}
 * class.
 * 
 * @author Tomas Skalicky
 */
public class AuthorityId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3842400502191977690L;

	private String username;
	private String authority;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.authority == null) ? 0 : this.authority.hashCode());
		result = prime * result + ((this.username == null) ? 0 : this.username.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}

		AuthorityId other = (AuthorityId) obj;
		if (this.authority == null) {
			if (other.authority != null) {
				return false;
			}
		} else if (!this.authority.equals(other.authority)) {
			return false;
		}
		if (this.username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!this.username.equals(other.username)) {
			return false;
		}
		return true;
	}
}