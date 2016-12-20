package org.vhmml.service;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private SimpleMailMessage templateMessage;
	
	public EmailServiceImpl() {
		super();
	}
	
	public void sendMessage(String toAddress, String subject, String message) {
		
		MimeMessage msg = mailSender.createMimeMessage();		
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, "UTF-8");
			helper.setFrom(templateMessage.getFrom());
			helper.setTo(toAddress);
			helper.setSubject(subject);
			helper.setText(message, true);
		} catch (MessagingException e) {
			throw new MailPreparationException("MessagingException creating email message with toAddress = " + toAddress + ", subject = " + subject + ", message = " + message, e);
		}		
		
		mailSender.send(msg);  
	}
	
	public void sendMessages(List<String> toAddressList, String subject, String message) {
		for(String emailAddress : toAddressList) {			
			sendMessage(emailAddress, subject, message);
		}
	}
}
