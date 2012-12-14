package com.svnavigatoru600.service.users;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.svnavigatoru600.service.util.Email;

/**
 * Validates the data provided by the user in <i>forgotten-password.jsp</i> form.
 * 
 * @author Tomas Skalicky
 */
@Service
public class SendNewPasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SendNewPassword.class.isAssignableFrom(clazz);
    }

    @Override
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
