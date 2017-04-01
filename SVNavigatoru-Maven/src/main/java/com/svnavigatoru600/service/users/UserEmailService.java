package com.svnavigatoru600.service.users;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.AbstractEmailService;
import com.svnavigatoru600.service.Configuration;
import com.svnavigatoru600.service.util.Email;
import com.svnavigatoru600.service.util.Localization;

/**
 * Provide sending of emails concerning user administration, e.g. sending a new password.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class UserEmailService extends AbstractEmailService {

    private static final String USER_ACCOUNT_TITLE_CODE = "user-account.title";
    private static final String EMAIL_CODE = "email";
    private static final String PASSWORD_CODE = "password";

    private static final String EMAIL_SUBJECT_NEW_USER_CODE = "email.subject.new-user";
    private static final String EMAIL_TEXT_NEW_USER_CODE = "email.text.new-user";
    private static final String EMAIL_SUBJECT_PASSWORD_RESET_CODE = "email.subject.password-reset";
    private static final String EMAIL_TEXT_PASSWORD_RESET_CODE = "email.text.password-reset";
    private static final String EMAIL_SUBJECT_PASSWORD_CHANGED_CODE = "email.subject.password-changed";
    private static final String EMAIL_TEXT_PASSWORD_CHANGED_CODE = "email.text.password-changed";
    private static final String EMAIL_SUBJECT_AUTHORITIES_CHANGED_CODE = "email.subject.authorities-changed";
    private static final String EMAIL_TEXT_AUTHORITIES_CHANGED_CODE = "email.text.authorities-changed";
    private static final String EMAIL_SUBJECT_USER_DELETED_CODE = "email.subject.user-deleted";
    private static final String EMAIL_TEXT_USER_DELETED_CODE = "email.text.user-deleted";

    private static final String USER_ROLES_NO_AUTHORITY_CODE = "user-roles.no-authority";
    private static final String USER_ROLES_MEMBER_OF_SV_CODE = "user-roles.member-of-sv";
    private static final String USER_ROLES_MEMBER_OF_BOARD_CODE = "user-roles.member-of-board";
    private static final String USER_ROLES_USER_ADMINISTRATOR_CODE = "user-roles.user-administrator";

    /**
     * Sends an email of the {@link User newUser} with his credentials.
     * <p>
     * The function is invoked when the user has already been successfully added to the repository by the administrator.
     */
    public void sendEmailOnUserCreation(User newUser, String newPassword, HttpServletRequest request,
            MessageSource messageSource) {
        final String email = newUser.getEmail();

        String subject = Localization.findLocaleMessage(messageSource, request,
                UserEmailService.EMAIL_SUBJECT_NEW_USER_CODE);

        String addressing = getLocalizedRecipientAddressing(newUser, request, messageSource);
        String usernameLabel = Localization.findLocaleMessage(messageSource, request,
                UserEmailService.USER_ACCOUNT_TITLE_CODE);
        String emailLabel = Localization.findLocaleMessage(messageSource, request, UserEmailService.EMAIL_CODE);
        String passwordLabel = Localization.findLocaleMessage(messageSource, request, UserEmailService.PASSWORD_CODE);
        String signature = getLocalizedAdminSignature(request, messageSource);
        Object[] messageParams = new Object[] { addressing, Configuration.DOMAIN, usernameLabel, newUser.getUsername(),
                emailLabel, email, passwordLabel, newPassword, signature };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                UserEmailService.EMAIL_TEXT_NEW_USER_CODE, messageParams);

        Email.sendMail(newUser, subject, messageText);
    }

    /**
     * Sends an email to the given {@link User} with his <code>newPassword</code>.
     * <p>
     * The method is invoked when user's password has already been successfully reset.
     * 
     * @param user
     *            User whose password has been reset. The recipient of the email
     * @param newPassword
     *            New not-hashed password of the user
     */
    public void sendEmailOnPasswordReset(User user, String newPassword, HttpServletRequest request,
            MessageSource messageSource) {
        String subject = Localization.findLocaleMessage(messageSource, request,
                UserEmailService.EMAIL_SUBJECT_PASSWORD_RESET_CODE);

        String addressing = getLocalizedRecipientAddressing(user, request, messageSource);
        String signature = getLocalizedAdminSignature(request, messageSource);
        Object[] messageParams = new Object[] { addressing, Configuration.DOMAIN, newPassword, signature };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                UserEmailService.EMAIL_TEXT_PASSWORD_RESET_CODE, messageParams);

        Email.sendMail(user, subject, messageText);
    }

    /**
     * Sends an email to the given {@link User} with his <code>newPassword</code>.
     * <p>
     * The function is invoked when user's password has already been successfully changed by the administrator.
     */
    public void sendEmailOnPasswordChange(User user, String newPassword, HttpServletRequest request,
            MessageSource messageSource) {
        String subject = Localization.findLocaleMessage(messageSource, request,
                UserEmailService.EMAIL_SUBJECT_PASSWORD_CHANGED_CODE);

        String addressing = getLocalizedRecipientAddressing(user, request, messageSource);
        String signature = getLocalizedAdminSignature(request, messageSource);
        Object[] messageParams = new Object[] { addressing, Configuration.DOMAIN, newPassword, signature };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                UserEmailService.EMAIL_TEXT_PASSWORD_CHANGED_CODE, messageParams);

        Email.sendMail(user, subject, messageText);
    }

    /**
     * Converts the new authorities of the given {@link User user} to a {@link String} representation. The conversion is
     * for email purposes.
     */
    private String convertAuthoritiesForEmail(User user, HttpServletRequest request, MessageSource messageSource) {
        StringBuilder newAuthorities = new StringBuilder();
        if (user.canSeeNews()) {
            if (user.canEditNews()) {
                newAuthorities.append(Localization.findLocaleMessage(messageSource, request,
                        UserEmailService.USER_ROLES_MEMBER_OF_BOARD_CODE));
            } else {
                newAuthorities.append(Localization.findLocaleMessage(messageSource, request,
                        UserEmailService.USER_ROLES_MEMBER_OF_SV_CODE));
            }
        }

        if (user.canSeeUsers()) {
            if (newAuthorities.length() > 0) {
                newAuthorities.append(", ");
            }
            newAuthorities.append(Localization.findLocaleMessage(messageSource, request,
                    UserEmailService.USER_ROLES_USER_ADMINISTRATOR_CODE));
        }

        if (user.getAuthorities().size() == 1) {
            // The user is registered, but has no other authority.
            newAuthorities.append(Localization.findLocaleMessage(messageSource, request,
                    UserEmailService.USER_ROLES_NO_AUTHORITY_CODE));
        }
        return newAuthorities.toString();
    }

    /**
     * Sends an emailto the given {@link User} with his new {@link com.svnavigatoru600.domain.users.Authority
     * Authorities}.
     * <p>
     * The function is invoked when user's authorities have already been successfully changed by the administrator.
     */
    public void sendEmailOnAuthoritiesChange(User user, HttpServletRequest request, MessageSource messageSource) {
        String subject = Localization.findLocaleMessage(messageSource, request,
                UserEmailService.EMAIL_SUBJECT_AUTHORITIES_CHANGED_CODE);

        String addressing = getLocalizedRecipientAddressing(user, request, messageSource);
        String newAuthorities = convertAuthoritiesForEmail(user, request, messageSource);
        String signature = getLocalizedAdminSignature(request, messageSource);
        Object[] messageParams = new Object[] { addressing, Configuration.DOMAIN, newAuthorities, signature };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                UserEmailService.EMAIL_TEXT_AUTHORITIES_CHANGED_CODE, messageParams);

        Email.sendMail(user, subject, messageText);
    }

    /**
     * Sends an email to the given {@link User} since his account has been deleted by the administrator.
     * <p>
     * The method is invoked when the user has already been successfully deleted.
     * 
     * @param user
     *            User which is to be deleted.
     */
    public void sendEmailOnUserDeletion(User user, HttpServletRequest request, MessageSource messageSource) {
        String subject = Localization.findLocaleMessage(messageSource, request,
                UserEmailService.EMAIL_SUBJECT_USER_DELETED_CODE);

        String addressing = getLocalizedRecipientAddressing(user, request, messageSource);
        String signature = getLocalizedAdminSignature(request, messageSource);
        Object[] messageParams = new Object[] { addressing, user.getUsername(), signature };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                UserEmailService.EMAIL_TEXT_USER_DELETED_CODE, messageParams);

        Email.sendMail(user, subject, messageText);
    }
}
