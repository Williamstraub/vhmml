package org.vhmml.controller.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinLengthValidator implements ConstraintValidator<MinLength, Object> {
	
	private int minLength;

    public void initialize(final MinLength constraintAnnotation) {
        minLength = constraintAnnotation.min();
    }

    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
    	return value != null && ((String)value).length() >= minLength;
    }
}