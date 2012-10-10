package svnavigatoru.service.forum.threads;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public abstract class ThreadValidator implements Validator {

	protected void checkNewName(String name, Errors errors) {
		String field = "thread.name";
		if (StringUtils.isBlank(name)) {
			errors.rejectValue(field, "forum.threads.name.not-filled-in");
		}
	}
}
