package org.vhmml.controller.validation;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.Email;


public class JQueryValidationRuleBuilder {
	
	private static final Logger LOG = Logger.getLogger(JQueryValidationRuleBuilder.class);
	private static Map<String, String> defaultMessages = new HashMap<String, String>();
	
	static {
		
		defaultMessages.put(getDefaultAnnotationMessage(NotNull.class), "Required field");
		defaultMessages.put(getDefaultAnnotationMessage(Null.class), "Field must be empty");
		defaultMessages.put(getDefaultAnnotationMessage(AssertFalse.class), "This field cannot be selected");
		defaultMessages.put(getDefaultAnnotationMessage(AssertTrue.class), "This field must be selected");
		defaultMessages.put(getDefaultAnnotationMessage(DecimalMax.class), "Max value is {0}");
		defaultMessages.put(getDefaultAnnotationMessage(DecimalMin.class), "Min value is {0}");
		defaultMessages.put(getDefaultAnnotationMessage(Future.class), "Date must occur in the future");
		defaultMessages.put(getDefaultAnnotationMessage(Past.class), "Date must occur in the past");
		defaultMessages.put(getDefaultAnnotationMessage(Max.class), "Max value is {0}");
		defaultMessages.put(getDefaultAnnotationMessage(Min.class), "Min value is {0}");
		defaultMessages.put(getDefaultAnnotationMessage(Email.class), "Invalid email address");
		defaultMessages.put(getDefaultAnnotationMessage(Size.class), "Invalid length");
		defaultMessages.put(getDefaultAnnotationMessage(Digits.class), "Must be a number");
	}
	
	public static Map<String, Map<String, Map<String, Object>>> buildJQueryValidationRules(Class<?> formBeanClass) {
		Map<String, Map<String, Map<String, Object>>> rules = new HashMap<String, Map<String,Map<String,Object>>>(50);
		Map<String, Map<String, Object>> jqueryRules = new HashMap<String, Map<String,Object>>(10);
		Map<String, Map<String, Object>> jqueryMessages = new HashMap<String, Map<String,Object>>(10);			
		rules.put("rules", jqueryRules);
		rules.put("messages", jqueryMessages);		
		
		Field[] fields = formBeanClass.getDeclaredFields();
				
		for(Field field : fields) {
			Map<String, Object> fieldRules = new HashMap<String, Object>(50);
			Map<String, Object> fieldMessages = new HashMap<String, Object>(50);
			
			if (field.getAnnotations().length > 0) {
				getFieldValidationRules(field, fieldRules, fieldMessages);
				
				if (fieldRules.size() > 0) {
					jqueryRules.put(field.getName(), fieldRules);
					jqueryMessages.put(field.getName(), fieldMessages);
				}
			}
		}
		
		getClassValidationRules(formBeanClass, jqueryRules, jqueryMessages);		
		
		return rules;
	}
	
	private static void getClassValidationRules(Class<?> formBeanClass, Map<String, Map<String, Object>> jqueryRules, Map<String, Map<String, Object>> jqueryMessages) {
		Annotation[] annotations = formBeanClass.getAnnotations();		
		
		for(Annotation annotation : annotations) {
			if (FieldMatch.class.isAssignableFrom(annotation.annotationType())) {
				FieldMatch fieldMatchAnnotation = ((FieldMatch)annotation);
				
				// get the existing rules for the "first" field, we need to add a rule to the list of rules for that field
				Map<String, Object> fieldRules = jqueryRules.get(fieldMatchAnnotation.second());
				Map<String, Object> fieldMessages = jqueryMessages.get(fieldMatchAnnotation.second());
				
				if(fieldRules == null) {
					fieldRules = new HashMap<String, Object>(50);
					fieldMessages = new HashMap<String, Object>(50);
				}
				
				fieldRules.put("equalTo", "#" + fieldMatchAnnotation.first());
				fieldMessages.put("equalTo", fieldMatchAnnotation.message());
				jqueryRules.put(fieldMatchAnnotation.second(), fieldRules);
				jqueryMessages.put(fieldMatchAnnotation.second(), fieldMessages);
			}
		}		
	}
	
	private static void getFieldValidationRules(Field field, Map<String, Object> fieldRules, Map<String, Object> fieldMessages) {
		
		Annotation[] annotations = field.getAnnotations();
		
		for(Annotation annotation : annotations) {
			if (NotNull.class.isAssignableFrom(annotation.annotationType())) {
				NotNull notNull = ((NotNull)annotation);
				fieldRules.put("required", true);				
				fieldMessages.put("required", getMessage(notNull, notNull.message()));
			} else if (Null.class.isAssignableFrom(annotation.annotationType())) {
				Null nullAnnotation = ((Null)annotation);
				fieldRules.put("equals", "");								
				fieldMessages.put("equals", getMessage(nullAnnotation, nullAnnotation.message()));				
			} else if (AssertFalse.class.isAssignableFrom(annotation.annotationType())) {
				AssertFalse assertFalse = (AssertFalse)annotation;
				fieldRules.put("assertFalse", true);
				fieldMessages.put("assertFalse", getMessage(assertFalse, assertFalse.message()));
			} else if (AssertTrue.class.isAssignableFrom(annotation.annotationType())) {
				AssertTrue assertTrue = (AssertTrue)annotation;
				fieldRules.put("assertTrue", true);
				fieldMessages.put("assertTrue", getMessage(assertTrue, assertTrue.message()));
			} else if (DecimalMax.class.isAssignableFrom(annotation.annotationType())) {
				DecimalMax decimalMax = (DecimalMax)annotation;
				fieldRules.put("max", new Double(decimalMax.value()));
				fieldRules.put("number", true);
				fieldMessages.put("max", getMessage(decimalMax, decimalMax.message(), decimalMax.value()));
				fieldMessages.put("number", getMessage(decimalMax, decimalMax.message(), decimalMax.value()));
			} else if (DecimalMin.class.isAssignableFrom(annotation.annotationType())) {
				DecimalMin decimalMin = (DecimalMin)annotation;
				fieldRules.put("min", new Double(decimalMin.value()));
				fieldRules.put("number", true);
				fieldMessages.put("min", getMessage(decimalMin, decimalMin.message(), decimalMin.value()));
				fieldMessages.put("number", getMessage(decimalMin, decimalMin.message(), decimalMin.value()));
			} else if (Future.class.isAssignableFrom(annotation.annotationType())) {
				Future future = (Future)annotation;
				fieldRules.put("futureDate", true);
				fieldMessages.put("futureDate", getMessage(future, future.message()));
			} else if (Max.class.isAssignableFrom(annotation.annotationType())) {
				Max max = (Max)annotation;
				fieldRules.put("max", new Long(max.value()));
				fieldRules.put("number", true);
				fieldMessages.put("max", getMessage(max, max.message(), max.value()));
				fieldMessages.put("number", ((Max)annotation).message());
			} else if (Min.class.isAssignableFrom(annotation.annotationType())) {
				Min min = (Min)annotation;
				fieldRules.put("min", new Long(min.value()));
				fieldRules.put("number", true);
				fieldMessages.put("min", getMessage(min, min.message(), min.value()));
				fieldMessages.put("number", getMessage(min, min.message(), min.value()));
			} else if (Past.class.isAssignableFrom(annotation.annotationType())) {
				Past past = (Past)annotation;
				fieldRules.put("pastDate", true);
				fieldMessages.put("pastDate", getMessage(past, past.message()));
			} else if (Email.class.isAssignableFrom(annotation.annotationType())) {
				Email email = (Email)annotation;
				fieldRules.put("email", true);
				fieldMessages.put("email", getMessage(email, email.message()));
			} else if (Pattern.class.isAssignableFrom(annotation.annotationType())) {
				throw new RuntimeException("Pattern annotation is not supported, create a custom validation rule, since Java and Javascript regexes are different.");
			} else if (Size.class.isAssignableFrom(annotation.annotationType())) {
				Size size = (Size)annotation;
				fieldRules.put("minlength", size.min());
				fieldRules.put("maxlength", size.max());
				fieldMessages.put("minlength", getMessage(size, size.message(), size.min(), size.max()));				
				fieldMessages.put("maxlength", getMessage(size, size.message(), size.min(), size.max()));
			} else if (Password.class.isAssignableFrom(annotation.annotationType())) {
				fieldRules.put("passwordCheck", true);
				fieldMessages.put("passwordCheck", ((Password)annotation).message());
			} else if (MaxLength.class.isAssignableFrom(annotation.annotationType())) {
				MaxLength maxLength = (MaxLength) annotation;
				fieldRules.put("maxlength", maxLength.max());
				fieldMessages.put("maxlength", getMessage(maxLength, maxLength.message(), maxLength.max()));
			}  else if (MinLength.class.isAssignableFrom(annotation.annotationType())) {
				MinLength minLength = (MinLength) annotation;
				fieldRules.put("minlength", minLength.min());
				fieldMessages.put("minlength", getMessage(minLength, minLength.message(), minLength.min()));
			} else if (Digits.class.isAssignableFrom(annotation.annotationType())) {
				Digits digits = (Digits)annotation;
				int length = digits.integer();
				if (digits.fraction() > 0) {
					length += digits.fraction();
					length += 1;
				}
				fieldRules.put("maxlength", new Integer(length));
				fieldRules.put("number", true);
				fieldMessages.put("maxlength", getMessage(digits, digits.message()));
				fieldMessages.put("number", getMessage(digits, digits.message()));
			}
		}
	}
	
	private static String getMessage(Annotation annotation, String message, Object... arguments) {
		
		String defaultMessage = defaultMessages.get(message);
		// the javax & hibernate validation annotations use property keys for default messages
		// so if the message on an annotation is something like {javax.validation.constraints.NotNull.message}
		// we know that a custom message wasn't specified on the annotation and we get the default value
		// mapped to the key in our defaultMessages map, such as {javax.validation.constraints.NotNull.message} == {0} is required
		message = defaultMessage == null ? message : defaultMessage; 
			
		if(StringUtils.isEmpty(message)) {
			message = defaultMessages.get(annotation.annotationType());
		}
				
		return MessageFormat.format(message, arguments);
	}
	
	private static String getDefaultAnnotationMessage(Class<?> annotationClass) {
		String defaultValue = null;
		
		try {
			defaultValue = (String)annotationClass.getMethod("message").getDefaultValue();
		} catch (Exception e) {		
			LOG.error("Unable to retrieve default annotation message value for annotation class " + annotationClass);
		}
		
		return defaultValue;
	}
}
