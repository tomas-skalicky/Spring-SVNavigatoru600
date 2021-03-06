package com.svnavigatoru600.service.util;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.google.common.collect.Sets;
import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityTypeEnum;

/**
 * Provides a set of static functions related to {@link Authority authorities (= roles)} and their {@link AuthorityTypeEnum
 * types}.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class AuthorityUtils {

    private AuthorityUtils() {
    }

    /**
     * Gets an array of flags which say which authorities (= roles) are checked (selected), and which authorities are
     * not checked. The array is filled in according to the given array of checked {@link GrantedAuthority} called
     * <code>authorities</code>.
     */
    public static boolean[] getArrayOfCheckIndicators(final Collection<GrantedAuthority> authorities) {
        final boolean[] indicators = AuthorityUtils.createArrayOfCheckIndicators();
        for (final GrantedAuthority authority : authorities) {
            final int authorityOrdinal = AuthorityTypeEnum.valueOf(authority.getAuthority()).ordinal();
            indicators[authorityOrdinal] = true;
        }
        return indicators;
    }

    /**
     * Gets the default array of flags which say which authorities (= roles) are checked (selected), and which
     * authorities are not checked.
     * <p>
     * <b>Precondition:</b> Ordinal values of all {@link AuthorityTypeEnum AuthorityTypes} are exactly in the range of
     * <code>[0, 1, ..., AuthorityType.values().length-1]</code>.
     */
    public static boolean[] getDefaultArrayOfCheckIndicators() {
        final boolean[] indicators = AuthorityUtils.createArrayOfCheckIndicators();
        indicators[AuthorityTypeEnum.ROLE_MEMBER_OF_SV.ordinal()] = true;
        return indicators;
    }

    private static boolean[] createArrayOfCheckIndicators() {
        return CheckboxUtils.createArrayOfCheckIndicators(AuthorityTypeEnum.values().length);
    }

    /**
     * Converts the given array of check <code>indicators</code> to the {@link Set} of {@link GrantedAuthority
     * GrantedAuthorities} associated with an {@link com.svnavigatoru600.domain.users.User User} with the given
     * <code>username</code>.
     *
     * @return Set of those authorities which have been checked, i.e. their flags equal <code>true</code>.
     */
    public static Set<GrantedAuthority> convertIndicatorsToAuthorities(final boolean[] indicators, final String username) {
        final Set<GrantedAuthority> checkedAuthorities = Sets.newHashSet();

        for (int i = 0; i < indicators.length; ++i) {
            final boolean isAuthorityChecked = indicators[i];
            if (isAuthorityChecked) {
                final AuthorityTypeEnum authorityType = AuthorityTypeEnum.values()[i];
                checkedAuthorities.add(new Authority(username, authorityType));
            }
        }
        return checkedAuthorities;
    }
}
