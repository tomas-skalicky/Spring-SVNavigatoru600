package com.svnavigatoru600.viewmodel.users.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Password;
import com.svnavigatoru600.service.util.PhoneNumber;
import com.svnavigatoru600.viewmodel.users.UpdateUserData;

/**
 * Validates the data of the current {@link com.svnavigatoru600.domain.users.User User} filled in in the
 * <i>user-account.jsp</i> form.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class UpdateUserDataValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return UpdateUserData.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final UpdateUserData command = (UpdateUserData) target;
        checkNewPassword(command.getNewPassword(), command.getNewPasswordConfirmation(), errors);
        checkNewEmail(command.getUser().getEmail(), errors);
        checkNewPhone(command.getUser().getPhone(), errors);
    }

    /**
     * Checks whether the given <code>password</code> and its <code>confirmation</code> are equal and whether they
     * represent the valid password. If both of them are blank, the parameters are OK.
     *
     * @param password
     *            Password which is to be checked.
     * @param confirmation
     *            The confirmation of the <code>password</code>.
     * @param errors
     *            Validations of the passwords are stored there.
     */
    private void checkNewPassword(final String password, final String confirmation, final Errors errors) {
        if (StringUtils.isBlank(password)) {
            // The passwords are not filled in -> will not be changed.
            ;
        } else {
            final boolean arePasswordsEqual = password.equals(confirmation);
            if (!arePasswordsEqual) {
                errors.rejectValue("newPasswordConfirmation", "password.not-same");
            }
            if (!Password.isValid(password)) {
                errors.rejectValue("newPassword", "password.bad-format");
            }
        }
    }

    /**
     * Checks whether the given <code>email</code> is in the valid format unless it is blank.
     */
    private void checkNewEmail(final String email, final Errors errors) {
        if (StringUtils.isBlank(email)) {
            // The email address is blank. It is not an error since this
            // information is optional.
            ;
        } else {
            if (!Email.isValid(email)) {
                errors.rejectValue("user.email", "email.bad-format");
            }
        }
    }

    /**
     * Checks whether the given <code>phone</code> number is in the valid format unless it is blank.
     */
    private void checkNewPhone(final String phone, final Errors errors) {
        if (StringUtils.isBlank(phone)) {
            // The phone number is blank. It is not an error since this
            // information is optional.
            ;
        } else {
            if (!PhoneNumber.isValid(phone)) {
                errors.rejectValue("user.phone", "phone.bad-format");
            }
        }
    }
}
