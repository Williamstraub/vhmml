package org.vhmml.service;

import java.util.List;

public interface EmailService {
	public void sendMessage(String toAddress, String subject, String message);
	public void sendMessages(List<String> toAddressList, String subject, String message);
}
