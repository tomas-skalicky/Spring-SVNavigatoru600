package com.svnavigatoru600.web.forum.contributions;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.service.forum.contributions.ContributionValidator;

/**
 * Parent of controllers which create and edit the {@link Contribution}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class NewEditContributionController extends ContributionController {

    /**
     * Command used in /main-content/forum/contributions/new-edit-contribution.jsp.
     */
    public static final String COMMAND = "newEditContributionCommand";
    protected Validator validator;

    public NewEditContributionController(final ContributionDao contributionDao,
            final ContributionValidator validator, final MessageSource messageSource) {
        super(contributionDao, messageSource);
        this.validator = validator;
    }
}
