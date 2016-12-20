package org.vhmml.exception;

public class UserDisabledException extends Exception {	
	
	private static final long serialVersionUID = -5528253075679914099L;

	public UserDisabledException(String message) {
		super(message);		
	}

	public UserDisabledException(Throwable cause) {
		super(cause);
	}

	public UserDisabledException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserDisabledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
