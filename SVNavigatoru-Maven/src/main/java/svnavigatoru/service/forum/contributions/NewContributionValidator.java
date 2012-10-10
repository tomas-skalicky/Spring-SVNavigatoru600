package svnavigatoru.service.forum.contributions;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class NewContributionValidator extends ContributionValidator {

	public boolean supports(Class<?> clazz) {
		return NewContribution.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		NewContribution command = (NewContribution) target;
		this.checkNewText(command.getContribution().getText(), errors);
	}
}
