package svnavigatoru.service.forum.contributions;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class EditContributionValidator extends ContributionValidator {

	public boolean supports(Class<?> clazz) {
		return EditContribution.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		EditContribution command = (EditContribution) target;
		this.checkNewText(command.getContribution().getText(), errors);
	}
}
