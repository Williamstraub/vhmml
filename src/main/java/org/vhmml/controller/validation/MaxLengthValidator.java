package org.vhmml.controller.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxLengthValidator implements ConstraintValidator<MaxLength, Object> {
	
	private int maxLength;

    public void initialize(final MaxLength constraintAnnotation) {
        maxLength = constraintAnnotation.max();
    }

    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
    	return value == null || ((String)value).length() <= maxLength;
    }
}