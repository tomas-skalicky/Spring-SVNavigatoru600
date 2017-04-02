package com.svnavigatoru600.service.forum;

import java.io.Serializable;

import javax.inject.Inject;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.domain.forum.ForumThread;
import com.svnavigatoru600.domain.users.User;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class ForumPermissionEvaluator implements PermissionEvaluator {

    private ContributionService contributionService;
    private ThreadService threadService;

    @Inject
    public void setContributionService(final ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @Inject
    public void setThreadService(final ThreadService threadService) {
        this.threadService = threadService;
    }

    @Override
    public boolean hasPermission(final Authentication authentication, final Object targetDomainObject, final Object permission) {
        if (targetDomainObject instanceof ForumContribution) {
            final ForumContribution contribution = (ForumContribution) targetDomainObject;
            return hasUserPermission(authentication, contribution.getAuthor(), permission);
        } else if (targetDomainObject instanceof ForumContribution) {
            final ForumThread thread = (ForumThread) targetDomainObject;
            return hasUserPermission(authentication, thread.getAuthor(), permission);
        } else {
            throw new RuntimeException("Unsupported type of target domain object.");
        }
    }

    @Override
    public boolean hasPermission(final Authentication authentication, final Serializable targetId, final String targetType,
            final Object permission) {
        if (ForumContribution.class.getName().equals(targetType)) {
            final ForumContribution contribution = contributionService.findById((Integer) targetId);
            return hasUserPermission(authentication, contribution.getAuthor(), permission);
        } else if (ForumThread.class.getName().equals(targetType)) {
            final ForumThread thread = threadService.findById((Integer) targetId);
            return hasUserPermission(authentication, thread.getAuthor(), permission);
        } else {
            throw new RuntimeException("Unsupported type of target domain object.");
        }
    }

    private boolean hasUserPermission(final Authentication authentication, final User author, final Object permission) {
        final boolean isUserLoggedIn = (authentication != null) && (authentication.getName() != null);
        if (!isUserLoggedIn) {
            return false;
        }

        return authentication.getName().equals(author.getUsername());
    }

}
