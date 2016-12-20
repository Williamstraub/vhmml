package org.vhmml.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "application_configuration")
public class ConfigurationValue {

	@Id
	@Column(name = "prop_key")
	private String key;
	
	@Column(name = "prop_value")
	private String value;
	
	@Column(name = "input_type")
	private String inputType;
	
	public ConfigurationValue() {
		super();
	}
	
	public ConfigurationValue(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
}
