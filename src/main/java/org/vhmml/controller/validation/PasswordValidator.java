package org.vhmml.controller.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, Object> {

//	^                 # start-of-string
//	(?=.*[0-9])       # a digit must occur at least once
//	(?=.*[a-z])       # a lower case letter must occur at least once
//	(?=.*[A-Z])       # an upper case letter must occur at least once
//	(?=\S+$)          # no whitespace allowed in the entire string
//	.{6,}             # anything, at least six characters
//	$                 # end-of-string
    private static final String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$";

    public void initialize(final Password constraintAnnotation) {
        // init code here
    }

    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
    	return ((String)value).matches(pattern);
    }
}