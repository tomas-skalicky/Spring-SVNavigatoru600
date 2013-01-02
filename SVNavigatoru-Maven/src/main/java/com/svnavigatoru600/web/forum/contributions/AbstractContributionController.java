package com.svnavigatoru600.web.forum.contributions;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.service.forum.contributions.ContributionService;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * Parent of all controllers which handle all operations upon the {@link Contribution Contributions}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
@PreAuthorize("hasRole('ROLE_MEMBER_OF_SV')")
public abstract class AbstractContributionController extends AbstractPrivateSectionMetaController {

    protected static final String BASE_URL = "/forum/temata/existujici/";
    private ContributionService contributionService;
    private MessageSource messageSource;

    public AbstractContributionController(ContributionService contributionService, MessageSource messageSource) {
        this.contributionService = contributionService;
        this.messageSource = messageSource;
    }

    /**
     * Trivial getter
     */
    protected ContributionService getContributionService() {
        return this.contributionService;
    }

    /**
     * Trivial getter
     */
    protected MessageSource getMessageSource() {
        return this.messageSource;
    }
}
