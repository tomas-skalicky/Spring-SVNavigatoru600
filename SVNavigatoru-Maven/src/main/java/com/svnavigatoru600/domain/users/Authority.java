package com.svnavigatoru600.domain.users;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class Authority implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Initialises no property.
     */
    public Authority() {
    }

    /**
     * Initialises authority's username and its type. Other properties are not touched.
     */
    public Authority(final String username, final AuthorityType authorityType) {
        this(username, authorityType.name());
    }

    /**
     * Initialises authority's username and its type. Other properties are not touched.
     */
    public Authority(final String username, final String authorityTypeName) {
        id = new AuthorityId();
        id.setUsername(username);
        id.setAuthority(authorityTypeName);
    }

    private AuthorityId id = new AuthorityId();

    public AuthorityId getId() {
        return id;
    }

    public void setId(final AuthorityId id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return id.getAuthority();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Authority [id=");
        builder.append(id);
        builder.append("]");
        return builder.toString();
    }
}
