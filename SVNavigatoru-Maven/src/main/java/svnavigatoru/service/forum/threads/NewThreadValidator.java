package svnavigatoru.service.forum.threads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import svnavigatoru.service.forum.contributions.NewContributionValidator;

@Service
public class NewThreadValidator extends ThreadValidator {

	private NewContributionValidator validator;

	@Autowired
	public NewThreadValidator(NewContributionValidator validator) {
		this.validator = validator;
	}

	public boolean supports(Class<?> clazz) {
		return NewThread.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		NewThread command = (NewThread) target;
		this.checkNewName(command.getThread().getName(), errors);
		this.validator.checkNewText(command.getContribution().getText(), errors);
	}
}
