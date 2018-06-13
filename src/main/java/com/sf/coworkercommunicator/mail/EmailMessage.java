package com.sf.coworkercommunicator.mail;

public class EmailMessage {
	
	private String recipient;
	private String code;
	private String spokenText;
	private String subject;
	private String message;
	
	public EmailMessage(String recipient, BasicMessage m) {
		super();
		this.recipient = recipient;
		this.code = m.getCode();
		this.spokenText = m.getSpokenText();
		this.subject = m.getSubject();
		this.message = m.getText();
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSpokenText() {
		return spokenText;
	}

	public void setSpokenText(String spokenText) {
		this.spokenText = spokenText;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
