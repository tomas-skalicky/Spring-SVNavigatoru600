package com.svnavigatoru600.service.forum;

import java.io.Serializable;

import javax.inject.Inject;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.domain.users.User;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class ForumPermissionEvaluator implements PermissionEvaluator {

    private ContributionService contributionService;
    private ThreadService threadService;

    @Inject
    public void setContributionService(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @Inject
    public void setThreadService(ThreadService threadService) {
        this.threadService = threadService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (targetDomainObject instanceof Contribution) {
            Contribution contribution = (Contribution) targetDomainObject;
            return hasUserPermission(authentication, contribution.getAuthor(), permission);
        } else if (targetDomainObject instanceof Contribution) {
            Thread thread = (Thread) targetDomainObject;
            return hasUserPermission(authentication, thread.getAuthor(), permission);
        } else {
            throw new RuntimeException("Unsupported type of target domain object.");
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
            Object permission) {
        if (Contribution.class.getName().equals(targetType)) {
            Contribution contribution = this.contributionService.findById((Integer) targetId);
            return hasUserPermission(authentication, contribution.getAuthor(), permission);
        } else if (Thread.class.getName().equals(targetType)) {
            Thread thread = this.threadService.findById((Integer) targetId);
            return hasUserPermission(authentication, thread.getAuthor(), permission);
        } else {
            throw new RuntimeException("Unsupported type of target domain object.");
        }
    }

    private boolean hasUserPermission(Authentication authentication, User author, Object permission) {
        boolean isUserLoggedIn = (authentication != null) && (authentication.getName() != null);
        if (!isUserLoggedIn) {
            return false;
        }

        return authentication.getName().equals(author.getUsername());
    }
}
