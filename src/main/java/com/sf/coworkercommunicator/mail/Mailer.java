package com.sf.coworkercommunicator.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {

	private static String username = "sf.coworker.communicator@gmail.com";
	private static String password = "scrummybears";


	public static void main(String[] args) {
		
		try {
			sendMessage("nick.jordan.ec4t@statefarm.com", "subject", "text");
			System.out.println("Done");
		} catch (MessagingException e) {
			System.out.println("ERROR : did not send message");
			e.printStackTrace();
		}
		
	}

	private static void sendMessage(String recipient, String subject, String text) throws MessagingException {
		Message message = buildMessage(recipient, subject, text);
		Transport.send(message);
	}

	private static Message buildMessage(String recipient, String subject, String text) {
		Session session = getSession();
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
			message.setSubject(subject);
			message.setText(text);
			return message;
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private static Session getSession() {
		return Session.getInstance(buildProperties(), new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}
	
	private static Properties buildProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		return props;
	}
}