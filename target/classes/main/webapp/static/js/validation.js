// delcare a global variable that can be used to statically validate & show validation messsages
var Validation;
var serverSideMessagesHidden = false;

$(function() {
	Validation = new Validation();
	
	if($.validator) {
		//add some custom validation methods
		$.validator.addMethod('assertTrue', function(value, element, param) {
		    return value === 'true'; 
		}, 'You must select {0}');
		
		$.validator.addMethod('assertFalse', function(value, element, param) {
		    return !value; 
		}, 'You must not select {0}');
		
		$.validator.addMethod('equals', function(value, element, param) { 
	        return this.optional(element) || value === param; 
	    }, 'Value must equal {0}');
		
		$.validator.addMethod('passwordCheck', function(value, element, param) {
		   return value
		   	&& value.length >= 6
		   	&& /[a-z]/.test(value) // has a lowercase letter
		   	&& /[A-Z]/.test(value) // has an uppercase  letter
		   	&& /\d/.test(value); // has a digit
		}, 'Password must be a minimum of 6 characters in length, include at least one upper and lower case letter, and a number');
		
		// fieldValidationMessages is a JSON object with server-side validation error messages, this logic displays those messages
		if(fieldValidationMessages) {					
			Validation.showFieldValidationMessages(fieldValidationMessages);		
		}
		
		// trim whitespace on validation so we don't get errors on things like a space after the email address
		$.each($.validator.methods, function (key, value) {
	        $.validator.methods[key] = function () {           
	            if(arguments.length > 0) {          	
	            	var el = $(arguments[1]);
	            	var selectedValue = el.val();
	            	
	            	if(typeof selectedValue === 'string') {
	            		el.val($.trim(selectedValue));
	            	}	            		            	
	            }

	            return value.apply(this, arguments);
	        };
	    });
		
		// automatically initialize validation for any forms that have a data-validation-rule-url attribute
		$('form').each(function() {
			Validation.initFormValidation($(this));
		});
	}	
});

function Validation(userOpts) {	
	
	this.getErrorDisplayRules = function() {
		var errorDisplayOptions = {};
		
		errorDisplayOptions.errorPlacement = function($errorLabel, $inputElement) {
			Validation.createErrorPopover($errorLabel.text(), $inputElement);		
		};
		
		errorDisplayOptions.success = function($errorLabel, inputElement) {
			$(inputElement).popover('hide');							
		};
		
		errorDisplayOptions.showErrors = function(errorMap, errorList) {
			this.defaultShowErrors();	
					
			if(errorList.length) {
				var $firstErrorField = $(errorList[0].element);
//				var offset = $firstErrorField.offset().top;
//				var visibleAreaStart = $(window).scrollTop();
//			    var visibleAreaEnd = visibleAreaStart + window.innerHeight;
	//
//			    if(offset < visibleAreaStart || offset > visibleAreaEnd) {
//			    	errorList[0].element.scrollIntoView();
//			    }
				scrollElementIntoView($firstErrorField);
			}
			for(var i = 0; i < errorList.length; i++) {
				var $inputElement = $(errorList[i].element);
				$inputElement.popover('show');			
			}			
		};
		
		return errorDisplayOptions;
	}
	
	this.createErrorPopover = function(errorMessage, $inputElement) {
		if($inputElement) {
			var customErrorPlacement = $inputElement.attr('data-error-placement'); 
			var messageLocation = customErrorPlacement ? customErrorPlacement : 'right'; 
			$inputElement.attr('data-toggle', 'popover');
			$inputElement.attr('data-trigger', 'manual');
			$inputElement.attr('data-placement', messageLocation);
			$inputElement.attr('data-content', errorMessage);
			$inputElement.popover().data('bs.popover').tip().addClass('error');
		}
	}
	
	this.showFieldValidationMessages = function(validationMessages) {
		for(var i = 0; i < validationMessages.length; i++) {
			var error = validationMessages[i];
			var $inputElement = $('#' + error.field);
			$inputElement = $inputElement.length ? $inputElement : $('input[name="' + error.field + '"]');
			
			if($inputElement.length) {
				Validation.createErrorPopover(error.defaultMessage, $inputElement);
				$inputElement.popover('show');					
				
				$inputElement.blur(function() {
					if(!serverSideMessagesHidden) {
						$inputElement.popover('hide');
						serverSideMessagesHidden = true; 
					}				
				});
			} else {
				console.log('error validating field, no input element found with id or name ' + error.field);
			}			
		}
	}	
	
	this.initFormValidation = function($form) {
		
		var validationRules = $form.data('validationRules');
		
		if(validationRules) {
			// validate() actually initializes the jQuery validation plugin
			// if you want to validate the form manually use the .valid() method, i.e. if($myForm.valid()) { do stuff and submit } 
			$form.validate(validationRules);
			
			$form.find('input[type="submit"]').click(function() {
				$form.submit();
			});
		} else {
			Validation.getValidationRules($form);
		}	
	};
	
	this.getValidationRules = function($form) {		
		var validationRuleUrl = $form.attr('data-validation-rule-url');
		
		if(validationRuleUrl) {
			$.ajax({
				url : contextPath + validationRuleUrl,
				type : 'GET',
				dataType: 'json',
				success : function(rules) {
					// getErrorDisplayRules adds the custom vHMML error message display logic to the jQuery validation plugin configuration
					validationRules = $.extend(rules, Validation.getErrorDisplayRules(), Validation.getSubmitHandler());
					$form.data('validationRules', validationRules);
					Validation.initFormValidation($form);					
				}
		    });
		}		
	};
	
	this.getSubmitHandler = function() {
		return {
			submitHandler: function(form) { 
				form.submit();
			}
		};
	}
}