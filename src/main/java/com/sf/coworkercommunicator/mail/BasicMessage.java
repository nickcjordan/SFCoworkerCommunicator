package com.sf.coworkercommunicator.mail;

public enum BasicMessage {
	
	OUT_OF_OFFICE		("ooo", "Out Of Office", "Out of Office Today", "I will be out of office today"),
	WORK_FROM_HOME	("wfh", "Work From Home", "Working From Home Today", "I will be working from home today"),
	PAID_TIME_OFF			("pto", "Paid Time Off", "Taking PTO Today", "I will be taking PTO today"),
	LATE_TO_OFFICE		("late", "Late To Work", "Arriving to Office Late", "I will be arriving to the office a little late today");
	
	private String code;
	private String spokenText;
	private String subject;
	private String text;
	
	private BasicMessage(String code, String spokenText, String subject, String text) {
		this.code = code;
		this.spokenText = spokenText;
		this.subject = subject;
		this.text = text;
	}
	
	public static BasicMessage getMessageFromCode(String id) {
		for (BasicMessage message : BasicMessage.values()) {
			if (message.getCode().equals(id)) {
				return message;
			}
		}
		return null;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
