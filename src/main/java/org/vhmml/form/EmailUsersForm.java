package org.vhmml.form;

import javax.validation.constraints.NotNull;

public class EmailUsersForm {

	@NotNull(message = "Subject is required")
	private String subject;
	
	@NotNull(message = "Message is required")
	private String message;

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
