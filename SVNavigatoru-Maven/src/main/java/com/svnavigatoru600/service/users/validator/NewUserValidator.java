package com.svnavigatoru600.service.users.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Password;
import com.svnavigatoru600.service.util.UserUtils;
import com.svnavigatoru600.service.util.Username;
import com.svnavigatoru600.viewmodel.users.AdministrateUserData;

/**
 * Validates the data of a new {@link User} filled in in the <i>new-user.jsp</i> form.
 * 
 * @author Tomas Skalicky
 */
@Service
public class NewUserValidator extends UserDataValidator {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(this.getClass());

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void validate(Object target, Errors errors) {
        AdministrateUserData command = (AdministrateUserData) target;
        this.checkNewUsername(command.getUser().getUsername(), errors);
        super.validate(target, errors);
        this.checkNewEmail(command.getUser().getEmail(), errors);
    }

    /**
     * Checks whether the given <code>username</code> is valid.
     */
    private void checkNewUsername(String username, Errors errors) {
        String field = "user.username";
        if (StringUtils.isBlank(username)) {
            errors.rejectValue(field, "username.not-filled-in");
        } else {
            if (!Username.isValid(username)) {
                errors.rejectValue(field, "username.bad-format");
            } else if (UserUtils.isUsernameOccupied(username, this.userDao)) {
                errors.rejectValue(field, "username.occupied-by-somebody");
            }
        }
    }

    @Override
    protected void checkNewPassword(String password, Errors errors) {
        String field = "newPassword";
        if (StringUtils.isBlank(password)) {
            errors.rejectValue(field, "password.not-filled-in");
        } else {
            if (!Password.isValid(password)) {
                errors.rejectValue(field, "password.bad-format");
            }
        }
    }

    /**
     * Checks whether the given <code>email</code> address is valid.
     */
    private void checkNewEmail(String email, Errors errors) {
        String field = "user.email";
        if (StringUtils.isBlank(email)) {
            // The email address is blank. It is not an error since this
            // information is optional.
        } else {
            if (!Email.isValid(email)) {
                errors.rejectValue(field, "email.bad-format");
            } else if (UserUtils.isEmailOccupied(email, this.userDao)) {
                errors.rejectValue(field, "email.occupied-by-somebody");
            }
        }
    }
}
