package com.svnavigatoru600.viewmodel.users.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.service.util.Password;

/**
 * Validates the data of an existing {@link com.svnavigatoru600.domain.users.Use User} filled in in the
 * <i>user-administration.jsp</i> form.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class AdministrateUserDataValidator extends AbstractUserDataValidator {

    @Override
    protected void checkNewPassword(String password, Errors errors) {
        if (StringUtils.isBlank(password)) {
            // The password is not filled in -> will not be changed.
            ;
        } else {
            if (!Password.isValid(password)) {
                errors.rejectValue("newPassword", "password.bad-format");
            }
        }
    }
}
