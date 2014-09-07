package com.svnavigatoru600.service.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.svnavigatoru600.domain.users.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.svnavigatoru600.service.Configuration;

/**
 * Provides a set of common static functions related to emails. The implementation borrowed from <a href=
 * "http://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/" >Mkyong</a>.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class Email {

	private static final Log LOGGER = LogFactory.getLog(Email.class);

	// DO NOT FORGET to change the implementation of the sendMail function if
	// you change these constants.
	// private static final String USERNAME = "";
	// private static final String PASSWORD = "Wa=az2chepHaH+q";
	// private static final String HOST = "smtp.gmail.com";
	private static final String USERNAME = "admin@svnavigatoru600.com";
	private static final String PASSWORD = "bx0477280577";
	private static final String HOST = "mail.svnavigatoru600.com";
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
     * Sends an email to the recipient.
     */
    public static void sendMail(User recipient, String subject, String messageText) {
        final String email;
        if (StringUtils.isNotEmpty(recipient.getRedirectEmail())) {
            email = recipient.getRedirectEmail();
        } else {
            email = recipient.getEmail();
        }

        sendMail(email, subject, messageText, recipient.getSmtpPort());
    }

	/**
	 * Sends an email with the given arguments; no matter which way of sending (i.e. SSL, TLS, or something
	 * else) is used. <b>Precondition:</b> The function assumes that the given <code>recipient</code>
	 * represents a correct email address.
	 *
	 * @param recipient
	 *            Recipient's email address
	 * @param smtpPort
	 *            25 or 2525
	 */
	private static void sendMail(String recipient, String subject, String messageText, int smtpPort) {
		if (!Email.isSpecified(recipient)) {
			return;
		}

		String subjectWithDiacritics = Localization.stripCzechDiacritics(subject);
		LOGGER.info(String.format("Sending email to %s, smtpPort=%d, subject='%s', messageText='%s'",
				recipient, smtpPort, subjectWithDiacritics, messageText));
		Email.sendMailWithoutSSL(recipient, subjectWithDiacritics, messageText, smtpPort);
		// Email.sendMailViaSSL(recipient, subjectWithDiacritics, messageText);
		// Email.sendMailViaTLS(recipient, subjectWithDiacritics, messageText);
	}

	/**
	 * Sends an email with the given arguments without SSL protocol. <b>Precondition:</b> The function assumes
	 * that the given <code>recipient</code> represents a correct email address.
	 *
	 * @param recipient
	 *            Recipient's email address
	 * @param smtpPort
	 *            25 or 2525
	 */
	private static void sendMailWithoutSSL(String recipient, String subject, String messageText, int smtpPort) {
		Properties props = new Properties();
		props.put(MAIL_SMTP_HOST, Email.HOST);
		props.put(MAIL_SMTP_PORT, smtpPort);
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
	 * <p>
	 * NOTE: the content of the result email is HTML text.
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
		message.setContent(Email.getMessageBody(messageText));

		Transport.send(message);

		Email.LOGGER.info(String.format("Email '%s' has been sent to '%s'.", subject, recipient));
	}

	/**
	 * Creates an email body which contents the given <code>messageText</code>. The MIME subtype is
	 * <code>html</code>.
	 */
	private static Multipart getMessageBody(String messageText) throws MessagingException {
		MimeBodyPart messageBodyPart = new MimeBodyPart();

		// Fills the message
		messageBodyPart.setText(messageText, Configuration.DEFAULT_ENCODING, "html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		return multipart;
	}
}
