package com.svnavigatoru600.service.util;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.domain.users.NotificationType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.Configuration;
import com.svnavigatoru600.web.users.EditMyAccountController;

/**
 * Provides a set of static functions related to emails. The implementation borrowed from <a href=
 * "http://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/" >Mkyong</a>.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class Email {

    private static final Log LOGGER = LogFactory.getLog(Email.class);

    // DO NOT FORGET to change the implementation of the sendMail function if
    // you change these constants.
    // private static final String USERNAME = "";
    // private static final String PASSWORD = "";
    // private static final String HOST = "smtp.gmail.com";
    private static final String USERNAME = "admin@svnavigatoru600.com";
    private static final String PASSWORD = "bx0477280577";
    private static final String HOST = "mail.svnavigatoru600.com";
    private static final int STANDARD_PORT = 2525;
    private static final int SSL_PORT = 465;
    private static final int TLS_PORT = 587;
    private static final String PROTOCOL = "smtp";
    private static final String SENDER = "admin@svnavigatoru600.com";

    private static final String VALID_EMAIL_REGEXP = "^[\\w\\-]+(\\.[\\w\\-]+)*@([\\w\\-]+\\.)+[a-zA-Z]{2,}$";

    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";

    private Email() {
    }

    /**
     * Indicates whether the given <code>email</code> is valid.
     */
    public static boolean isValid(String email) {
        return email.matches(Email.VALID_EMAIL_REGEXP);
    }

    /**
     * Indicates whether the given <code>email</code> holds an address, or is blank.
     */
    public static boolean isSpecified(String email) {
        return !StringUtils.isBlank(email);
    }

    /**
     * Sends an email with the given arguments; no matter which way of sending (i.e. SSL, TLS, or something
     * else) is used. <b>Precondition:</b> The function assumes that the given <code>recipient</code>
     * represents a correct email address.
     * 
     * @param recipient
     *            Recipient's email address
     */
    public static void sendMail(String recipient, String subject, String messageText) {
        if (!Email.isSpecified(recipient)) {
            return;
        }

        Email.sendMailWithoutSSL(recipient, subject, messageText);
        // Email.sendMailViaSSL(recipient, subject, messageText);
        // Email.sendMailViaTLS(recipient, subject, messageText);
    }

    /**
     * Sends an email with the given arguments without SSL protocol. <b>Precondition:</b> The function assumes
     * that the given <code>recipient</code> represents a correct email address.
     * 
     * @param recipient
     *            Recipient's email address
     */
    private static void sendMailWithoutSSL(String recipient, String subject, String messageText) {
        Properties props = new Properties();
        props.put(MAIL_SMTP_HOST, Email.HOST);
        props.put(MAIL_SMTP_PORT, Email.STANDARD_PORT);
        props.put(MAIL_SMTP_AUTH, Boolean.TRUE);

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Email.USERNAME, Email.PASSWORD);
            }
        });

        try {
            Email.sendMail(session, recipient, subject, messageText);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends an email with the given arguments via SSL protocol. <b>Precondition:</b> The function assumes
     * that the given <code>recipient</code> represents a correct email address.
     * 
     * @param recipient
     *            Recipient's email address
     */
    @SuppressWarnings("unused")
    private static void sendMailViaSSL(String recipient, String subject, String messageText) {
        Properties props = new Properties();
        props.put(MAIL_SMTP_HOST, Email.HOST);
        props.put("mail.smtp.socketFactory.port", Email.SSL_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put(MAIL_SMTP_AUTH, Boolean.TRUE);
        props.put(MAIL_SMTP_PORT, Email.SSL_PORT);

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Email.USERNAME, Email.PASSWORD);
            }
        });

        try {
            Email.sendMail(session, recipient, subject, messageText);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends an email with the given arguments via TLS/STARTTLS protocol. <b>Precondition:</b> The function
     * assumes that the given <code>recipient</code> represents a correct email address.
     * 
     * @param recipient
     *            Recipient's email address
     */
    @SuppressWarnings("unused")
    private static void sendMailViaTLS(String recipient, String subject, String messageText) {
        Properties props = new Properties();
        props.put(MAIL_SMTP_AUTH, Boolean.TRUE);
        props.put("mail.smtp.starttls.enable", Boolean.TRUE);

        Session session = Session.getInstance(props);

        try {
            Transport transport = session.getTransport(Email.PROTOCOL);
            transport.connect(Email.HOST, Email.TLS_PORT, Email.USERNAME, Email.PASSWORD);

            Email.sendMail(session, recipient, subject, messageText);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The final part of all functions <code>sendMailWithoutSSL</code>, <code>sendMailViaSSL</code> and
     * <code>sendMailViaTLS</code>.
     * 
     * @param recipient
     *            Recipient's email address
     */
    private static void sendMail(Session session, String recipient, String subject, String messageText)
            throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Email.SENDER));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setContent(messageText, "text/html; charset=\"UTF-8\"");
        // Option without the charset specification:
        // message.setText(messageText);

        Transport.send(message);

        Email.LOGGER.info(String.format("Email '%s' has been sent to '%s'.", subject, recipient));
    }

    private static final String USER_ACCOUNT_TITLE_CODE = "user-account.title";
    private static final String EMAIL_CODE = "email";
    private static final String PASSWORD_CODE = "password";

    private static final String USER_ROLES_NO_AUTHORITY_CODE = "user-roles.no-authority";
    private static final String USER_ROLES_MEMBER_OF_SV_CODE = "user-roles.member-of-sv";
    private static final String USER_ROLES_MEMBER_OF_BOARD_CODE = "user-roles.member-of-board";
    private static final String USER_ROLES_USER_ADMINISTRATOR_CODE = "user-roles.user-administrator";

    private static final String EMAIL_TEXT_ADDRESSING_CODE = "email.text.addressing";
    private static final String EMAIL_TEXT_ADMIN_SIGNATURE_CODE = "email.text.admin-signature";
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

    /**
     * Gets localized addressing of the {@link User recipient}.
     * 
     * @param recipient
     *            Addressed user
     */
    public static String getLocalizedRecipientAddressing(User recipient, HttpServletRequest request,
            MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request, Email.EMAIL_TEXT_ADDRESSING_CODE,
                recipient.getLastName());
    }

    /**
     * Gets localized signature of administrator of these web pages.
     */
    public static String getLocalizedAdminSignature(HttpServletRequest request, MessageSource messageSource) {
        return Localization.findLocaleMessage(messageSource, request, Email.EMAIL_TEXT_ADMIN_SIGNATURE_CODE,
                Configuration.DOMAIN);
    }

    /**
     * Sends an email of the {@link User newUser} with his credentials.
     * <p>
     * The function is invoked when the user has already been successfully added to the repository by the
     * administrator.
     */
    public static void sendEmailOnUserCreation(User newUser, String newPassword, HttpServletRequest request,
            MessageSource messageSource) {
        String email = newUser.getEmail();

        String subject = Localization.findLocaleMessage(messageSource, request,
                Email.EMAIL_SUBJECT_NEW_USER_CODE);

        String addressing = Email.getLocalizedRecipientAddressing(newUser, request, messageSource);
        String usernameLabel = Localization.findLocaleMessage(messageSource, request,
                Email.USER_ACCOUNT_TITLE_CODE);
        String emailLabel = Localization.findLocaleMessage(messageSource, request, Email.EMAIL_CODE);
        String passwordLabel = Localization.findLocaleMessage(messageSource, request, Email.PASSWORD_CODE);
        String signature = Email.getLocalizedAdminSignature(request, messageSource);
        Object[] messageParams = new Object[] { addressing, Configuration.DOMAIN, usernameLabel,
                newUser.getUsername(), emailLabel, email, passwordLabel, newPassword, signature };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                Email.EMAIL_TEXT_NEW_USER_CODE, messageParams);

        Email.sendMail(email, subject, messageText);
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
    public static void sendEmailOnPasswordReset(User user, String newPassword, HttpServletRequest request,
            MessageSource messageSource) {
        String subject = Localization.findLocaleMessage(messageSource, request,
                Email.EMAIL_SUBJECT_PASSWORD_RESET_CODE);

        String addressing = Email.getLocalizedRecipientAddressing(user, request, messageSource);
        String signature = Email.getLocalizedAdminSignature(request, messageSource);
        Object[] messageParams = new Object[] { addressing, Configuration.DOMAIN, newPassword, signature };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                Email.EMAIL_TEXT_PASSWORD_RESET_CODE, messageParams);

        Email.sendMail(user.getEmail(), subject, messageText);
    }

    /**
     * Sends an email to the given {@link User} with his <code>newPassword</code>.
     * <p>
     * The function is invoked when user's password has already been successfully changed by the
     * administrator.
     */
    public static void sendEmailOnPasswordChange(User user, String newPassword, HttpServletRequest request,
            MessageSource messageSource) {
        String subject = Localization.findLocaleMessage(messageSource, request,
                Email.EMAIL_SUBJECT_PASSWORD_CHANGED_CODE);

        String addressing = Email.getLocalizedRecipientAddressing(user, request, messageSource);
        String signature = Email.getLocalizedAdminSignature(request, messageSource);
        Object[] messageParams = new Object[] { addressing, Configuration.DOMAIN, newPassword, signature };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                Email.EMAIL_TEXT_PASSWORD_CHANGED_CODE, messageParams);

        Email.sendMail(user.getEmail(), subject, messageText);
    }

    /**
     * Converts the new authorities of the given {@link User user} to a {@link String} representation. The
     * conversion is for email purposes.
     */
    private static String convertAuthoritiesForEmail(User user, HttpServletRequest request,
            MessageSource messageSource) {
        StringBuilder newAuthorities = new StringBuilder();
        if (user.canSeeNews()) {
            if (user.canEditNews()) {
                newAuthorities.append(Localization.findLocaleMessage(messageSource, request,
                        Email.USER_ROLES_MEMBER_OF_BOARD_CODE));
            } else {
                newAuthorities.append(Localization.findLocaleMessage(messageSource, request,
                        Email.USER_ROLES_MEMBER_OF_SV_CODE));
            }
        }

        if (user.canSeeUsers()) {
            if (newAuthorities.length() > 0) {
                newAuthorities.append(", ");
            }
            newAuthorities.append(Localization.findLocaleMessage(messageSource, request,
                    Email.USER_ROLES_USER_ADMINISTRATOR_CODE));
        }

        if (user.getAuthorities().size() == 1) {
            // The user is registered, but has no other authority.
            newAuthorities.append(Localization.findLocaleMessage(messageSource, request,
                    Email.USER_ROLES_NO_AUTHORITY_CODE));
        }
        return newAuthorities.toString();
    }

    /**
     * Sends an emailto the given {@link User} with his new {@link com.svnavigatoru600.domain.users.Authority
     * Authorities}.
     * <p>
     * The function is invoked when user's authorities have already been successfully changed by the
     * administrator.
     */
    public static void sendEmailOnAuthoritiesChange(User user, HttpServletRequest request,
            MessageSource messageSource) {
        String subject = Localization.findLocaleMessage(messageSource, request,
                Email.EMAIL_SUBJECT_AUTHORITIES_CHANGED_CODE);

        String addressing = Email.getLocalizedRecipientAddressing(user, request, messageSource);
        String newAuthorities = Email.convertAuthoritiesForEmail(user, request, messageSource);
        String signature = Email.getLocalizedAdminSignature(request, messageSource);
        Object[] messageParams = new Object[] { addressing, Configuration.DOMAIN, newAuthorities, signature };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                Email.EMAIL_TEXT_AUTHORITIES_CHANGED_CODE, messageParams);

        Email.sendMail(user.getEmail(), subject, messageText);
    }

    /**
     * Sends an email to the given {@link User} since his account has been deleted by the administrator.
     * <p>
     * The method is invoked when the user has already been successfully deleted.
     * 
     * @param user
     *            User which is to be deleted.
     */
    public static void sendEmailOnUserDeletion(User user, HttpServletRequest request,
            MessageSource messageSource) {
        String subject = Localization.findLocaleMessage(messageSource, request,
                Email.EMAIL_SUBJECT_USER_DELETED_CODE);

        String addressing = Email.getLocalizedRecipientAddressing(user, request, messageSource);
        String signature = Email.getLocalizedAdminSignature(request, messageSource);
        Object[] messageParams = new Object[] { addressing, user.getUsername(), signature };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                Email.EMAIL_TEXT_USER_DELETED_CODE, messageParams);

        Email.sendMail(user.getEmail(), subject, messageText);
    }

    private static final String NOTIFICATIONS_EMAIL_TEXT_SIGNATURE_CODE = "notifications.email.text.signature";
    private static final String NOTIFICATIONS_EMAIL_TEXT_UNSUBSCRIPTION_LINK_TEXT_CODE = "notifications.email.text.unsubscription-link-text";
    private static final String NOTIFICATIONS_EMAIL_NEWS_SUBJECT_NEWS_CREATED = "notifications.email.news.subject.news-created";
    private static final String NOTIFICATIONS_EMAIL_NEWS_TEXT_NEWS_CREATED = "notifications.email.news.text.news-created";

    /**
     * Gets a localized signature of emails which serve as notifications of news and changes.
     * 
     * @param notificationType
     *            Type which caused that the currently logged user is going to receive an notification.
     * @param linkForUnsubscription
     *            URL which unsubscribes the currently logged user from the receiving notifications of the
     *            <code>notificationType</code> if the user clicks on it. Moreover, controller associated with
     *            the URL redirects the user to the settings site of the user account.
     */
    public static String getLocalizedNotificationSignature(NotificationType notificationType,
            String linkForUnsubscription, HttpServletRequest request, MessageSource messageSource) {

        String sectionWhichIsToBeUnsubscribed = Localization.findLocaleMessage(messageSource, request,
                notificationType.getTitleLocalizationCode());
        String linkText = Localization.findLocaleMessage(messageSource, request,
                Email.NOTIFICATIONS_EMAIL_TEXT_UNSUBSCRIPTION_LINK_TEXT_CODE);
        String hereClickToUnsubscribe = String.format("<a href='%s'>%s</a>", linkForUnsubscription, linkText);

        Object[] messageParams = new Object[] { Configuration.DOMAIN, sectionWhichIsToBeUnsubscribed,
                hereClickToUnsubscribe };
        return Localization.findLocaleMessage(messageSource, request,
                Email.NOTIFICATIONS_EMAIL_TEXT_SIGNATURE_CODE, messageParams);
    }

    /**
     * Gets URL which unsubscribes the currently logged user from receiving notifications of the
     * <code>notificationType</code> if the user clicks on it. Moreover, controller associated with the URL
     * redirects the user to the settings site of the user account.
     */
    private static String getLinkForUnsubscription(NotificationType notificationType, User user) {
        return String.format("%s%s%s/%s%d/", Configuration.DOMAIN, EditMyAccountController.BASE_URL,
                user.getUsername(), EditMyAccountController.UNSUBSCRIBE_FROM_NOTIFICATIONS_URL_PART,
                notificationType.ordinal());
    }

    /**
     * Sends emails to the given {@link User Users} with notification of the newly posted {@link News}.
     */
    public static void sendEmailOnNewsCreation(News news, List<User> usersToNotify,
            HttpServletRequest request, MessageSource messageSource) {
        String newsTitle = news.getTitle();
        String subject = Localization.findLocaleMessage(messageSource, request,
                Email.NOTIFICATIONS_EMAIL_NEWS_SUBJECT_NEWS_CREATED, newsTitle);

        NotificationType notificationType = NotificationType.IN_NEWS;
        String serverNameAndPort = String.format("%s:%d", request.getServerName(), request.getServerPort());
        String newsText = Url.convertImageRelativeUrlsToAbsolute(news.getText(), serverNameAndPort);

        for (User user : usersToNotify) {
            String addressing = Email.getLocalizedRecipientAddressing(user, request, messageSource);
            String linkForUnsubscription = Email.getLinkForUnsubscription(notificationType, user);
            String signature = Email.getLocalizedNotificationSignature(notificationType,
                    linkForUnsubscription, request, messageSource);
            Object[] messageParams = new Object[] { addressing, newsTitle, newsText, signature };
            String messageText = Localization.findLocaleMessage(messageSource, request,
                    Email.NOTIFICATIONS_EMAIL_NEWS_TEXT_NEWS_CREATED, messageParams);

            Email.sendMail(user.getEmail(), subject, messageText);
        }
    }
}
