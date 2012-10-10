package svnavigatoru.service.forum.contributions;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public abstract class ContributionValidator implements Validator {

	public void checkNewText(String text, Errors errors) {
		String field = "contribution.text";
		if (StringUtils.isBlank(text)) {
			errors.rejectValue(field, "forum.contributions.text.not-filled-in");
		}
	}
}
