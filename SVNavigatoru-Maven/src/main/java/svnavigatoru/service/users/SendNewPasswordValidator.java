package svnavigatoru.service.users;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import svnavigatoru.service.util.Email;

/**
 * Validates the data provided by the user in <i>forgotten-password.jsp</i>
 * form.
 * 
 * @author Tomas Skalicky
 */
@Service
public class SendNewPasswordValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return SendNewPassword.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		String field = "user.email";

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, "email.not-filled-in");
		if (errors.hasErrors()) {
			// The email address is blank.
		} else {
			SendNewPassword command = (SendNewPassword) target;
			if (!Email.isValid(command.getUser().getEmail())) {
				errors.rejectValue(field, "email.bad-format");
			}
		}
	}
}
