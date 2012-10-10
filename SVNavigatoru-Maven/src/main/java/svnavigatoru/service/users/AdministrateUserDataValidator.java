package svnavigatoru.service.users;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import svnavigatoru.domain.users.User;
import svnavigatoru.service.util.Password;

/**
 * Validates the data of an existing {@link User} filled in in the
 * <i>user-administration.jsp</i> form.
 * 
 * @author Tomas Skalicky
 */
@Service
public class AdministrateUserDataValidator extends UserDataValidator {

	@Override
	protected void checkNewPassword(String password, Errors errors) {
		if (StringUtils.isBlank(password)) {
			// The password is not filled in -> will not be changed.
		} else {
			if (!Password.isValid(password)) {
				errors.rejectValue("newPassword", "password.bad-format");
			}
		}
	}
}
