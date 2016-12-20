// declare a global that can be used on any page to show messages
var Messages;

$(function() {
	Messages = new PageMessages();
	Messages.showUserMessages();
	
	if(pageMessages) {
		if($.isArray(pageMessages)) {
			Messages.addMessages(pageMessages);
		} else {
			Messages.addMessage(pageMessages);
		}		
	}
	
	$('#vhmmlMessages').on('click', 'div.messages.global button.close', function() {
		var messageKey = $(this).parent('div.messages').attr('id');
		
		$.ajax({
			url: contextPath + '/user/messages/remove/' + messageKey,
			method: 'POST'
		});
	});
});

function PageMessages(userOpts) {	
	
	var defaultOpts = {
		elementId: 'vhmmlMessages'
	};
	var opts = $.extend({}, defaultOpts, userOpts);
	var pageMessages = createPageMessages(opts);	
	var nextMessageKey = 0;
	var severityList = {
		SUCCESS: {icon: 'glyphicon-ok', cssClass: 'success'}, 
		INFO: {icon: 'glyphicon-info-sign', cssClass: 'info'},
		WARN: {icon: 'glyphicon-warning-sign', cssClass: 'warning'},
		ERROR: {icon: 'glyphicon-exclamation-sign', cssClass: 'danger'}
	};	
	
	initEvents();
	
	this.addMessage = function(message) {
		
		if(!message.key) {
			message.key = 'vhmml-message-' + nextMessageKey++;
		}
		var $element = message.element ? message.element : pageMessages.$element;	
		$element.prepend(getMessageHtml(message));
		pageMessages.messageMap[message.key] = message;
		 
		$('#' + opts.elementId)[0].scrollIntoView();
		
		return message; 
	};
	
	this.addMessages = function(messages) {
		
		for(var i = 0; i < messages.length; i++) {
			this.addMessage(messages[i]);
		}		
		 
		return messages; 
	};
	
	this.removeMessage = function(message) {
		var messageKey = typeof message === 'string' ? message : message.key;
		$(getId(messageKey)).remove();
		delete pageMessages.messageMap[messageKey];
	};
	
	this.removeMessages = function(messages) {
		
		for(var i = 0; i < messages.length; i++) {
			this.removeMessage(messages[i]);
		}		
		 
		return messages; 
	};
	
	this.removeAll = function() {
		this.removeMessages(Object.keys(pageMessages.messageMap));
	};
	
	this.showResponseMessage = function(response, $element) {
		var severity = response.status === 200 ? 'SUCCESS' : 'ERROR';
		var jsonResponse = isJson(response.responseText);		
		var message = jsonResponse ? jsonResponse.message : response.responseText;
		Messages.addMessage({message: message, severity: severity, element: $element});
	};
	
	this.showUserMessages = function() {
		$.ajax({
			url: contextPath + '/user/messages',
			success: function(userMessages) {
				if(userMessages) {
					// global messages is a map stored so we iterate over it's message properties
					for(messageKey in userMessages) {
						var message = userMessages[messageKey];
						message.key = messageKey;
						message.cssClass = 'global';
						Messages.addMessage(message);
					}			
				}
			}
		});
	}
	
	function getMessageHtml(message) {
		var html = '';
		var severity = severityList[message.severity];
		var cssClass = 'alert alert-' + severity.cssClass + ' alert-dismissible messages';
		cssClass = message.cssClass ? cssClass + ' ' + message.cssClass : cssClass;
		
		html = '<div id="' + message.key + '" class="' + cssClass + '" role="alert">';
			html += '<button type="button" class="close" data-hide="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
			html += '<strong><span class="glyphicon ' + severity.icon + '"/></strong> <span class="message">' + message.message + '</span>';
		html += '</div>';		
		
		return html;
	}
	
	function createPageMessages(options) {
		var pageMessages = {};		
		pageMessages.$element = $(getId(options.elementId));			
		pageMessages.messageMap = {};
		
		return pageMessages;
	}	
	
	function initEvents() {
		$('body').on('click', '[data-hide]', function() {
			$(this).parent('div.alert').addClass('hidden');
		});
	}
}