package org.vhmml.service;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vhmml.service.ApplicationConfigService.Property;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private ApplicationConfigService configService;
	
	@Autowired
	private EmailService emailService;
	
	public FeedbackServiceImpl() {
		super();
	}
	
	 private static final Logger LOG = Logger.getLogger(FeedbackServiceImpl.class);
	 
	public void sendCorrections(String correctionType, List<String> categories, String corrections, String contactEmail, String hmmlProjectNumber) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {		
		List<String> systemsLibrarianEmailList = configService.getValues(Property.SYSTEMS_LIB_EMAIL_LIST);
		String message = "Dear vHMML Systems Librarian,<br/><br/>";
		InetAddress serverIP;
		 
		/*adapted from http://crunchify.com/how-to-get-server-ip-address-and-hostname-in-java/ by William to include server IP address */
		try {
			serverIP = InetAddress.getLocalHost();
			message += "On server: " + serverIP +"<br/><br/>";
		} catch (Exception e) {
			LOG.error("UnknownHostException while trying to get local server IP address", e);
		}			
		
		message += "It has been suggested that the following correction/addition be made in vHMML " + correctionType + ".<br/><br/>";
		
		if(StringUtils.isNotEmpty(hmmlProjectNumber)) {
			message += "HMML Project Number: " + hmmlProjectNumber + "<br/><br/>";
		}
		
		if(CollectionUtils.isNotEmpty(categories)) {
			message += "Categories:<br/>";
			
			for(int i = 0; i < categories.size(); i++) {
				if(i > 0) {
					message += ", ";
				}
				message += categories.get(i);
			}
			
			message += "<br/><br/>";
		}
		
		message += "Correction(s):<br/>";
		message += corrections;
		
		if(StringUtils.isNotEmpty(contactEmail)) {
			message += "<br/><br/>The user making these suggestions has agreed to allow vHMML personnel to contact them at the following email address in regards to the suggested changes:<br/><br/>" + contactEmail;
		}
		
		emailService.sendMessages(systemsLibrarianEmailList, "vHMML " + correctionType + " Correction/Addition", message);			
	}
}
