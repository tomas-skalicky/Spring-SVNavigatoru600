package com.svnavigatoru600.service.users;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityTypeEnum;
import com.svnavigatoru600.repository.users.AuthorityDao;
import com.svnavigatoru600.service.util.Localization;

/**
 * Provides convenient methods to work with {@link Authority} objects.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class AuthorityService {

    /**
     * The object which provides a persistence.
     */
    @Inject
    private AuthorityDao authorityDao;

    public AuthorityService() {
    }

    /**
     * Returns all {@link Authority Authorities} stored in the repository which are associated with the given
     * <code>username</code>.
     */
    public List<Authority> findAll(final String username) {
        return authorityDao.findByUsername(username);
    }

    /**
     * Stores the given {@link GrantedAuthority authorities} to the repository. If there is already an {@link Authority}
     * with the same {@link com.svnavigatoru600.domain.users.AuthorityId#getUsername() username} and
     * {@link com.svnavigatoru600.domain.users.AuthorityId#getAuthority() authority's name}, throws an exception.
     */
    public void save(final Collection<GrantedAuthority> authorities) {
        authorityDao.save(authorities);
    }

    /**
     * Deletes all {@link Authority Authorities} of the specified {@link com.svnavigatoru600.domain.users.User User}.
     *
     * @param username
     *            The username (=login) of the user
     */
    public void delete(final String username) {
        authorityDao.delete(username);
    }

    /**
     * Gets a {@link Map} which for each constant of the {@link AuthorityTypeEnum} enumeration contains a pair of its
     * {@link AuthorityTypeEnum#getOrdinal() ordinal} and ID of its checkbox.
     */
    public static Map<Long, String> getRoleCheckboxId() {
        final String commonIdFormat = "newAuthorities[%s]";
        final Map<Long, String> checkboxIds = new HashMap<>();

        for (final AuthorityTypeEnum type : AuthorityTypeEnum.values()) {
            final long typeOrdinal = type.getOrdinal();
            checkboxIds.put(typeOrdinal, String.format(commonIdFormat, typeOrdinal));
        }
        return checkboxIds;
    }

    /**
     * Gets a {@link Map} which for each constant of the {@link AuthorityTypeEnum} enumeration contains a pair of its
     * {@link AuthorityTypeEnum#getOrdinal() ordinal} and its localized title.
     */
    @Cacheable("localizedRoleTitles")
    public Map<Long, String> getLocalizedRoleTitles(final HttpServletRequest request, final MessageSource messageSource) {
        final Map<Long, String> ordinalTitleMap = new HashMap<>();

        for (final AuthorityTypeEnum type : AuthorityTypeEnum.values()) {
            final String localizedTitle = Localization.findLocaleMessage(messageSource, request,
                    type.getTitleLocalizationCode());
            ordinalTitleMap.put(type.getOrdinal(), localizedTitle);
        }
        return ordinalTitleMap;
    }
}
