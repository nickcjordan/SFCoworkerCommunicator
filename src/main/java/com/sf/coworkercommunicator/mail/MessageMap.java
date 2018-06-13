package com.sf.coworkercommunicator.mail;

import java.util.HashMap;
import java.util.Map;

public class MessageMap {
	
	private static Map<String, String> emailAddresses;
	
	static {
		emailAddresses = new HashMap<String, String>();
		emailAddresses.put("nick", "nick.jordan.ec4t@statefarm.com");
		emailAddresses.put("connor", "connor.jankus.ee2h@statefarm.com");
		emailAddresses.put("rashid", "mamunor.rashid.e0lo@statefarm.com");
		emailAddresses.put("scrum master", "mamunor.rashid.e0lo@statefarm.com");
	}
	
	public static EmailMessage getMessage(String id, String recipient) {
		BasicMessage message = BasicMessage.getMessageFromCode(id);
		return new EmailMessage(recipient.toLowerCase(), message);
	}

}
