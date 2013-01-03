package com.svnavigatoru600.service.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.Configuration;

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
        props.put("mail.smtp.host", Email.HOST);
        props.put("mail.smtp.port", Email.STANDARD_PORT);
        props.put("mail.smtp.auth", "true");

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
        props.put("mail.smtp.host", Email.HOST);
        props.put("mail.smtp.socketFactory.port", Email.SSL_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", Email.SSL_PORT);

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
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

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
     * 
     * @throws MessagingException
     * @throws AddressException
     */
    private static void sendMail(Session session, String recipient, String subject, String messageText)
            throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Email.SENDER));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setContent(messageText, "text/plain; charset=\"UTF-8\"");
        // Option without the charset specification:
        // message.setText(messageText);

        Transport.send(message);

        Email.LOGGER.info(String.format("Email '%s' has been sent to '%s'.", subject, recipient));
    }

    /**
     * Sends an email of the {@link User newUser} with his credentials.
     * <p>
     * The function is invoked when the user has already been successfully added to the repository by the
     * administrator.
     */
    public static void sendEmailOnUserCreation(User newUser, String newPassword, HttpServletRequest request,
            MessageSource messageSource) {
        String subject = Localization.findLocaleMessage(messageSource, request, "email.subject.new-user");
        String email = newUser.getEmail();
        Object[] messageParams = new Object[] { newUser.getLastName(), Configuration.DOMAIN,
                newUser.getUsername(), email, newPassword };
        String messageText = Localization.findLocaleMessage(messageSource, request, "email.text.new-user",
                messageParams);

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
                "email.subject.password-reset");
        Object[] messageParams = new Object[] { user.getLastName(), Configuration.DOMAIN, newPassword };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                "email.text.password-reset", messageParams);

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
                "email.subject.password-changed");
        Object[] messageParams = new Object[] { user.getLastName(), Configuration.DOMAIN, newPassword };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                "email.text.password-changed", messageParams);

        Email.sendMail(user.getEmail(), subject, messageText);
    }

    /**
     * Sends an emailto the given {@link User} with his new {@link Authority Authorities}.
     * <p>
     * The function is invoked when user's authorities have already been successfully changed by the
     * administrator.
     */
    public static void sendEmailOnAuthoritiesChange(User user, HttpServletRequest request,
            MessageSource messageSource) {
        String subject = Localization.findLocaleMessage(messageSource, request,
                "email.subject.authorities-changed");

        // Converts the new authorities to the String representation.
        StringBuilder newAuthorities = new StringBuilder();
        if (user.canSeeNews()) {
            if (user.canEditNews()) {
                newAuthorities.append(Localization.findLocaleMessage(messageSource, request,
                        "user-roles.member-of-board"));
            } else {
                newAuthorities.append(Localization.findLocaleMessage(messageSource, request,
                        "user-roles.member-of-sv"));
            }
        }
        if (user.canSeeUsers()) {
            if (newAuthorities.length() > 0) {
                newAuthorities.append(", ");
            }
            newAuthorities.append(Localization.findLocaleMessage(messageSource, request,
                    "user-roles.user-administrator"));
        }
        if (user.getAuthorities().size() == 1) {
            // The user is registered, but has no other authority.
            newAuthorities.append(Localization.findLocaleMessage(messageSource, request,
                    "user-roles.no-authority"));
        }
        // End of the conversion.

        Object[] messageParams = new Object[] { user.getLastName(), Configuration.DOMAIN, newAuthorities };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                "email.text.authorities-changed", messageParams);

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
        String subject = Localization.findLocaleMessage(messageSource, request, "email.subject.user-deleted");
        Object[] messageParams = new Object[] { user.getLastName(), user.getUsername(), Configuration.DOMAIN };
        String messageText = Localization.findLocaleMessage(messageSource, request,
                "email.text.user-deleted", messageParams);

        Email.sendMail(user.getEmail(), subject, messageText);
    }
}
