package com.svnavigatoru600.service.util;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.util.FullNameFormat.FullNameFormatVisitor;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class UserFullNameFormatter implements FullNameFormatVisitor<String> {

    /**
     * {@link User} whose full name will be formatted.
     */
    private final User user;

    public UserFullNameFormatter(User user) {
        this.user = user;
    }

    @Override
    public String visitFirstLast() {
        return String.format("%s %s", this.user.getFirstName(), this.user.getLastName());
    }

    @Override
    public String visitLastFirst() {
        return String.format("%s %s", this.user.getLastName(), this.user.getFirstName());
    }

    @Override
    public String visitLastCommaFirst() {
        return String.format("%s, %s", this.user.getLastName(), this.user.getFirstName());
    }
}
