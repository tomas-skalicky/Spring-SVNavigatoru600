package com.svnavigatoru600.domain.users;

import java.io.Serializable;

/**
 * Helper with the representation of the composite key of the {@link Authority} class.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class AuthorityId implements Serializable {

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

    /**
     * Generated
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((this.authority == null) ? 0 : this.authority.hashCode());
        result = prime * result + ((this.username == null) ? 0 : this.username.hashCode());
        return result;
    }

    /**
     * Generated
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

    @Override
    public String toString() {
        return new StringBuilder("[username=").append(this.username).append(", authority=")
                .append(this.authority).append("]").toString();
    }
}
