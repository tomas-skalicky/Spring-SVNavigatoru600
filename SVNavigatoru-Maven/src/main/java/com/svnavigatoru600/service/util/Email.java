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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides a set of static functions related to emails. The implementation
 * borrowed from <a href=
 * "http://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/"
 * >Mkyong</a>.
 * 
 * @author Tomas Skalicky
 */
public class Email {

	/** Logger for this class and subclasses */
	protected static final Log logger = LogFactory.getLog(Email.class);

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

	/**
	 * Indicates whether the given <code>email</code> is valid.
	 */
	public static boolean isValid(String email) {
		return email.matches(Email.VALID_EMAIL_REGEXP);
	}

	/**
	 * Indicates whether the given <code>email</code> holds an address, or is
	 * blank.
	 */
	public static boolean isSpecified(String email) {
		return !StringUtils.isBlank(email);
	}

	/**
	 * Sends an email with the given arguments; no matter which way of sending
	 * (i.e. SSL, TLS, or something else) is used. <b>Precondition:</b> The
	 * function assumes that the given <code>recipient</code> represents a
	 * correct email address.
	 */
	public static void sendMail(String recipient, String subject, String messageText) {
		Email.sendMailWithoutSSL(recipient, subject, messageText);
		// Email.sendMailViaSSL(recipient, subject, messageText);
		// Email.sendMailViaTLS(recipient, subject, messageText);
	}

	/**
	 * Sends an email with the given arguments without SSL protocol.
	 * <b>Precondition:</b> The function assumes that the given
	 * <code>recipient</code> represents a correct email address.
	 */
	public static void sendMailWithoutSSL(String recipient, String subject, String messageText) {
		Properties props = new Properties();
		props.put("mail.smtp.host", Email.HOST);
		props.put("mail.smtp.port", Email.STANDARD_PORT);
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
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
	 * Sends an email with the given arguments via SSL protocol.
	 * <b>Precondition:</b> The function assumes that the given
	 * <code>recipient</code> represents a correct email address.
	 */
	public static void sendMailViaSSL(String recipient, String subject, String messageText) {
		Properties props = new Properties();
		props.put("mail.smtp.host", Email.HOST);
		props.put("mail.smtp.socketFactory.port", Email.SSL_PORT);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", Email.SSL_PORT);

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
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
	 * Sends an email with the given arguments via TLS/STARTTLS protocol.
	 * <b>Precondition:</b> The function assumes that the given
	 * <code>recipient</code> represents a correct email address.
	 */
	public static void sendMailViaTLS(String recipient, String subject, String messageText) {
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
	 * The final part of all functions <code>sendMailWithoutSSL</code>,
	 * <code>sendMailViaSSL</code> and <code>sendMailViaTLS</code>.
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

		Email.logger.info(String.format("Email '%s' has been sent to '%s'.", subject, recipient));
	}
}
