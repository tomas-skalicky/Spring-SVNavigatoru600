package com.svnavigatoru600.web.forum.contributions;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import com.svnavigatoru600.service.forum.ContributionService;
import com.svnavigatoru600.viewmodel.forum.contributions.validator.AbstractContributionValidator;
import com.svnavigatoru600.web.SendNotificationController;
import com.svnavigatoru600.web.SendNotificationModelFiller;

/**
 * Parent of controllers which create and edit the {@link com.svnavigatoru600.domain.forum.Contribution
 * Contributions}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractNewEditContributionController extends AbstractContributionController implements
        SendNotificationController {

    /**
     * Command used in /main-content/forum/contributions/new-edit-contribution.jsp.
     */
    public static final String COMMAND = "newEditContributionCommand";
    private final Validator validator;
    private final SendNotificationModelFiller sendNotificationModelFiller;

    public AbstractNewEditContributionController(ContributionService contributionService,
            SendNotificationModelFiller sendNotificationModelFiller, AbstractContributionValidator validator,
            MessageSource messageSource) {
        super(contributionService, messageSource);
        this.validator = validator;
        this.sendNotificationModelFiller = sendNotificationModelFiller;
    }

    /**
     * Trivial getter
     */
    protected Validator getValidator() {
        return this.validator;
    }

    /**
     * Trivial getter
     */
    protected SendNotificationModelFiller getSendNotificationModelFiller() {
        return this.sendNotificationModelFiller;
    }
}
