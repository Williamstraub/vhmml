package org.vhmml.web;

public class VhmmlMessage {
	
	public enum Severity {
		SUCCESS("alert-success", "glyphicon-ok"), 
		INFO("alert-info", "glyphicon-info-sign"),
		WARN("alert-warning", "glyphicon-warning-sign"),
		ERROR("alert-danger", "glyphicon-exclamation-sign");
		
		private String cssClass;
		private String icon;
		
		private Severity(String cssClass, String icon) {
			this.cssClass = cssClass;
			this.icon = icon;
		}
		
		public String getCssClass() {
			return cssClass;
		}
		
		public String getIcon() {
			return icon;
		}
	}
	
	private String key;
	private String message;
	private Severity severity;
	
	public VhmmlMessage() {
		super();
	}
	
	public VhmmlMessage(String message) {
		this.message = message;
		this.severity = Severity.SUCCESS;
	}
	
	public VhmmlMessage(String message, Severity severity) {
		this.message = message;
		this.severity = severity;
	}
	
	public VhmmlMessage(String key, String message, Severity severity) {		
		this(message, severity);
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
}
