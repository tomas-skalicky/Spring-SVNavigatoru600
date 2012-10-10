package svnavigatoru.service.users;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import svnavigatoru.domain.users.AuthorityType;
import svnavigatoru.domain.users.User;
import svnavigatoru.service.util.FirstName;
import svnavigatoru.service.util.LastName;

/**
 * Validates the data of a new or an existing {@link User} determined by an
 * administrator.
 * 
 * @author Tomas Skalicky
 */
@Service
public abstract class UserDataValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return AdministrateUserData.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		AdministrateUserData command = (AdministrateUserData) target;
		this.checkNewPassword(command.getNewPassword(), errors);
		User user = command.getUser();
		this.checkNewFirstName(user.getFirstName(), errors);
		this.checkNewLastName(user.getLastName(), errors);
		this.checkNewAuthorities(command.getNewAuthorities(), errors);
	}

	/**
	 * Checks whether the given <code>password</code> represents the valid
	 * password.
	 * 
	 * @param password
	 *            Password which is to be checked.
	 * @param errors
	 *            Validations of the password are stored there.
	 */
	protected abstract void checkNewPassword(String password, Errors errors);

	/**
	 * Checks whether the given <code>firstName</code> is valid.
	 */
	protected void checkNewFirstName(String firstName, Errors errors) {
		String field = "user.firstName";
		if (StringUtils.isBlank(firstName)) {
			errors.rejectValue(field, "first-name.not-filled-in");
		} else {
			if (!FirstName.isValid(firstName)) {
				errors.rejectValue(field, "first-name.bad-format");
			}
		}
	}

	/**
	 * Checks whether the given <code>lastName</code> is valid.
	 */
	protected void checkNewLastName(String lastName, Errors errors) {
		String field = "user.lastName";
		if (StringUtils.isBlank(lastName)) {
			errors.rejectValue(field, "last-name.not-filled-in");
		} else {
			if (!LastName.isValid(lastName)) {
				errors.rejectValue(field, "last-name.bad-format");
			}
		}
	}

	/**
	 * Checks whether the given <code>authorities</code> are valid.
	 */
	protected void checkNewAuthorities(boolean[] authorities, Errors errors) {
		if (authorities[AuthorityType.ROLE_MEMBER_OF_BOARD.ordinal()]
				&& !authorities[AuthorityType.ROLE_MEMBER_OF_SV.ordinal()]) {
			errors.rejectValue("newAuthorities", "user-roles.if_board_then_sv");
		}
	}
}
