package org.vhmml.web;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class VhmmlMessageUtil {

	public static final String CONTEXT_ATT_GLOBAL_MESSAGES = "vhmmlGlobalMessages";
	
	@Autowired
	private ServletContext servletContext;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public VhmmlMessageUtil() {
		super();
	}
	
	public Map<String, VhmmlMessage> getGlobalMessages() throws IOException {
		Map<String, VhmmlMessage> globalMessages = new HashMap<String, VhmmlMessage>();
		Object existingMessages = servletContext.getAttribute(CONTEXT_ATT_GLOBAL_MESSAGES);
		
		if(existingMessages != null) {
			String messagesJson = (String)existingMessages;
			globalMessages = objectMapper.readValue(messagesJson, new TypeReference<Map<String, VhmmlMessage>>(){});			
		} 	
		
		return globalMessages;
	}

	/**
	 * This method adds the given message to the list of messages in the servlet context 
	 * if the message doesn't already exist.
	 * 
	 * @param message The message to be added
	 * @param severity The {@link VhmmlMessage.Severity} of the message.  
	 * @return true if the message is a new message that hasn't already been added to the servlet context.
	 */	
	public boolean addGlobalMessage(VhmmlMessage message) throws IOException {
		boolean newMessage = true;
		Map<String, VhmmlMessage> globalMessages = getGlobalMessages();
		
		for(VhmmlMessage nextMessage : globalMessages.values()) {
			if(nextMessage.getMessage().equals(message.getMessage())) {
				newMessage = false;
				break;
			}
		}
		
		if(newMessage) {
			globalMessages.put(message.getKey(), message);
			servletContext.setAttribute(CONTEXT_ATT_GLOBAL_MESSAGES, objectMapper.writeValueAsString(globalMessages));
			newMessage = true;
		}		
		
		return newMessage;
	}
	
	public void removeGlobalMessage(String messageKey) throws IOException {
		Map<String, VhmmlMessage> messages = getGlobalMessages();
		messages.remove(messageKey);
		servletContext.setAttribute(CONTEXT_ATT_GLOBAL_MESSAGES, objectMapper.writeValueAsString(messages));
	}
	
	public void removeAllGlobalMessages() {
		servletContext.setAttribute(CONTEXT_ATT_GLOBAL_MESSAGES, null);
	} 
}
