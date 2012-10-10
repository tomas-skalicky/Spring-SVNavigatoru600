package svnavigatoru.web.forum.contributions;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import svnavigatoru.domain.forum.Contribution;
import svnavigatoru.repository.forum.ContributionDao;
import svnavigatoru.web.PrivateSectionMetaController;

/**
 * Parent of all controllers which handle all operations upon the
 * {@link Contribution}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
@PreAuthorize("hasRole('ROLE_MEMBER_OF_SV')")
public abstract class ContributionController extends PrivateSectionMetaController {

	protected static final String BASE_URL = "/forum/temata/existujici/";
	protected ContributionDao contributionDao;
	protected MessageSource messageSource;

	public ContributionController(ContributionDao contributionDao, MessageSource messageSource) {
		this.contributionDao = contributionDao;
		this.messageSource = messageSource;
	}
}
