package com.sf.coworkercommunicator.mail;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {

	private String username = "sf.coworker.communicator@gmail.com";
	private String password = "scrummybears";


	public EmailMessage sendMessage(String recipient, String sender, String id) {
		
		try {
			EmailMessage message = MessageMap.getMessage(id, recipient, sender);
			sendMessage(message);
			System.out.println("Done");
			return message;
		} catch (MessagingException e) {
			System.out.println("ERROR : did not send message");
			e.printStackTrace();
			return null;
		}
	}

	private void sendMessage(EmailMessage email) throws MessagingException {
		Transport.send(buildMessage(email.getRecipient(), email.getSender(), email.getSubject(), email.getMessage()));
	}

	private  Message buildMessage(String recipient, String sender, String subject, String text) {
		Session session = getSession();
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
			message.setSubject(subject);
			message.setText(text + "\n\nMessage sent from " + sender);
			return message;
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private  Session getSession() {
		return Session.getInstance(buildProperties(), new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}
	
	private  Properties buildProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		return props;
	}
}