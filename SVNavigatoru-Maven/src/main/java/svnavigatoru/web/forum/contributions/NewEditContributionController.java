package svnavigatoru.web.forum.contributions;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import svnavigatoru.domain.forum.Contribution;
import svnavigatoru.repository.forum.ContributionDao;
import svnavigatoru.service.forum.contributions.ContributionValidator;

/**
 * Parent of controllers which create and edit the {@link Contribution}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class NewEditContributionController extends ContributionController {

	/**
	 * Command used in
	 * /main-content/forum/contributions/new-edit-contribution.jsp.
	 */
	public static final String COMMAND = "newEditContributionCommand";
	protected Validator validator;

	public NewEditContributionController(ContributionDao contributionDao, ContributionValidator validator,
			MessageSource messageSource) {
		super(contributionDao, messageSource);
		this.validator = validator;
	}
}
