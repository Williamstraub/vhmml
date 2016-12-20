package org.vhmml.form;

import javax.validation.constraints.NotNull;

import org.vhmml.controller.validation.FieldMatch;
import org.vhmml.controller.validation.Password;

import com.fasterxml.jackson.annotation.JsonIgnore;

@FieldMatch(first = "newPassword", second = "newPasswordConfirm", message = "Passwords don't match")
public class ChangePasswordForm {

	@NotNull(message = "Current password is required")
	private String currentPassword;
	
	@JsonIgnore
	@NotNull(message = "New password is required")	
	@Password(message = "Invalid Password")
	private String newPassword;
	
	@JsonIgnore
	@NotNull(message = "New password confirmation is required")
	private String newPasswordConfirm;
	
	public ChangePasswordForm() {
		super();
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordConfirm() {
		return newPasswordConfirm;
	}

	public void setNewPasswordConfirm(String newPasswordConfirm) {
		this.newPasswordConfirm = newPasswordConfirm;
	}
}
