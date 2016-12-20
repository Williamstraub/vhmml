package org.vhmml.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.vhmml.controller.validation.MaxLength;

public class ForgotPasswordForm {

	@NotNull(message = "Email is required")
	@Email(message = "Invalid email address")
	@MaxLength(max = 100)
	private String emailAddress;
	
	public ForgotPasswordForm() {
		super();
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}
