package org.vhmml.form;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.vhmml.controller.validation.MaxLength;

public class RegistrationForm {
	
	private Long id;

	@NotNull(message = "Email is required")
	@Email(message = "Invalid email address")
	@MaxLength(max = 100)
	private String username;
	
	@NotNull(message = "First name is required")
	@MaxLength(max = 50)
	private String firstName;
	
	@NotNull(message = "Last name is required")
	@MaxLength(max = 50, message = "{0} is way too long")
	private String lastName;
	
	@MaxLength(max = 255)
	private String institution;
	
	@NotNull(message = "Address is required")
	@MaxLength(max = 255)
	private String address;
	
	@NotNull(message = "Phone number is required")
	@MaxLength(max = 20)
	private String phone;
	
	@NotNull(message = "Research interests are required")
	@MaxLength(max = 2000)
	private String researchInterests;
	
	@NotNull(message = "Region and language interests are required")
	@MaxLength(max = 2000)
	private String regionAndLangInterests;
	
	@AssertFalse(message = "Computers cannot register")
	private Boolean computer;

	@AssertTrue(message = "You must be human to register")
	private Boolean human;
	
	private Boolean enabled;
	
	private String internalNotes;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getResearchInterests() {
		return researchInterests;
	}

	public void setResearchInterests(String researchInterests) {
		this.researchInterests = researchInterests;
	}

	public String getRegionAndLangInterests() {
		return regionAndLangInterests;
	}

	public void setRegionAndLangInterests(String regionAndLangInterests) {
		this.regionAndLangInterests = regionAndLangInterests;
	}

	public Boolean getComputer() {
		return computer;
	}

	public void setComputer(Boolean computer) {
		this.computer = computer;
	}

	public Boolean getHuman() {
		return human;
	}

	public void setHuman(Boolean human) {
		this.human = human;
	}

	public String getInternalNotes() {
		return internalNotes;
	}

	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
