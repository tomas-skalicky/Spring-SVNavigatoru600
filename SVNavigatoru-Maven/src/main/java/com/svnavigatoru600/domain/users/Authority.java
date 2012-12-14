package com.svnavigatoru600.domain.users;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.repository.users.AuthorityDao;

public class Authority implements GrantedAuthority, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -65071618776307089L;

    @SuppressWarnings("unused")
    private AuthorityDao authorityDao;

    public void setAuthorityDao(AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }

    /**
     * Default constructor. Necessary.
     */
    public Authority() {
    }

    public Authority(String username, String authorityType) {
        this.id = new AuthorityId();
        this.id.setUsername(username);
        this.id.setAuthority(authorityType);
    }

    private AuthorityId id;

    public AuthorityId getId() {
        return this.id;
    }

    public void setId(AuthorityId id) {
        this.id = id;
    }

    public String getAuthority() {
        return this.id.getAuthority();
    }
}
