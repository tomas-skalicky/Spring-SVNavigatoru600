package com.svnavigatoru600.url.forum;

import javax.servlet.http.HttpServletRequest;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.service.util.HttpRequestUtils;
import com.svnavigatoru600.url.CommonUrlParts;

/**
 * Contains snippets of URL which concern just web pages with only {@link com.svnavigatoru600.domain.forum.Contribution
 * Contributions}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class ContributionsUrlParts {

    public static final String BASE_URL = ThreadsUrlParts.EXISTING_URL;
    public static final String CONTRIBUTIONS_EXTENSION = "prispevky/";
    public static final String CONTRIBUTIONS_NEW_EXTENSION = ContributionsUrlParts.CONTRIBUTIONS_EXTENSION
            + CommonUrlParts.NEW_EXTENSION;
    public static final String CONTRIBUTIONS_CREATED_EXTENSION = ContributionsUrlParts.CONTRIBUTIONS_EXTENSION
            + CommonUrlParts.CREATED_EXTENSION;
    public static final String CONTRIBUTIONS_EXISTING_EXTENSION = ContributionsUrlParts.CONTRIBUTIONS_EXTENSION
            + CommonUrlParts.EXISTING_EXTENSION;
    public static final String CONTRIBUTIONS_DELETED_EXTENSION = ContributionsUrlParts.CONTRIBUTIONS_EXTENSION
            + CommonUrlParts.DELETED_EXTENSION;

    private ContributionsUrlParts() {
    }

    /**
     * Composes an absolute URL which holds details about the given {@link Contribution}.
     */
    public static String getAbsoluteContributionUrl(Contribution contribution, HttpServletRequest request) {
        return String.format("%s%s%d/%s#contribution_%d", HttpRequestUtils.getContextHomeDirectory(request),
                ContributionsUrlParts.BASE_URL, contribution.getThread().getId(),
                ContributionsUrlParts.CONTRIBUTIONS_EXTENSION, contribution.getId());
    }

    /**
     * Composes a relative URL which holds details about the newly added {@link Contribution}.
     */
    public static String getRelativeContributionUrlAfterCreation(Contribution newContribution) {
        return String.format("%s%d/%s#contribution_%d", ContributionsUrlParts.BASE_URL,
                newContribution.getThread().getId(), ContributionsUrlParts.CONTRIBUTIONS_CREATED_EXTENSION,
                newContribution.getId());
    }
}
