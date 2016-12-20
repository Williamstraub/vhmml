package org.vhmml.controller.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validation annotation to validate maximum field length.
 * 
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxLengthValidator.class)
public @interface MaxLength {

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	String message() default "Max length is {0}";
	int max() default Integer.MAX_VALUE;
}
