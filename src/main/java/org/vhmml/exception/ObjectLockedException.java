package org.vhmml.exception;

public class ObjectLockedException extends Exception {	

	private static final long serialVersionUID = -4204518000429559305L;

	public ObjectLockedException(String message) {
		super(message);		
	}

	public ObjectLockedException(Throwable cause) {
		super(cause);
	}

	public ObjectLockedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ObjectLockedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
