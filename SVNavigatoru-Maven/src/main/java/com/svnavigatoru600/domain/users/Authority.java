package com.svnavigatoru600.domain.users;

import java.io.Serializable;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.service.users.AuthorityService;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class Authority implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = -65071618776307089L;

    @SuppressWarnings("unused")
    private AuthorityService authorityService;

    @Inject
    public void setAuthorityService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * Initialises no property.
     */
    public Authority() {
    }

    /**
     * Initialises authority's username and its type. Other properties are not touched.
     */
    public Authority(String username, AuthorityType authorityType) {
        this(username, authorityType.name());
    }

    /**
     * Initialises authority's username and its type. Other properties are not touched.
     */
    public Authority(String username, String authorityTypeName) {
        this.id = new AuthorityId();
        this.id.setUsername(username);
        this.id.setAuthority(authorityTypeName);
    }

    private AuthorityId id;

    public AuthorityId getId() {
        return this.id;
    }

    public void setId(AuthorityId id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return this.id.getAuthority();
    }

    @Override
    public String toString() {
        return this.id.toString();
    }
}
