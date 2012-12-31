package com.svnavigatoru600.web.forum.contributions;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.service.forum.contributions.validator.AbstractContributionValidator;

/**
 * Parent of controllers which create and edit the {@link Contribution}s.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractNewEditContributionController extends AbstractContributionController {

    /**
     * Command used in /main-content/forum/contributions/new-edit-contribution.jsp.
     */
    public static final String COMMAND = "newEditContributionCommand";
    protected Validator validator;

    public AbstractNewEditContributionController(final ContributionDao contributionDao,
            final AbstractContributionValidator validator, final MessageSource messageSource) {
        super(contributionDao, messageSource);
        this.validator = validator;
    }
}
