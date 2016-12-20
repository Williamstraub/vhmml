function GlobalMessagesAdmin() {
	
	renderMessagesTable();
	initEvents();
	var newMessageDialog = initNewMessageDialog();
	
	function renderMessagesTable() {
		var html = '';
		
		if(allGlobalMessages) {
			for(messageKey in allGlobalMessages) {
				html += getMessageRowHtml(allGlobalMessages[messageKey]);
			}
			
			$('#messagesTable tbody').append(html);
		}		
	}
	
	function initEvents() {
		$('#messagesTable').on('click', 'button.remove', function() {
			removeMessage($(this));
		});
	}
	
	function initNewMessageDialog() {
		var dialogHtml = getAddMessageHtml();
		var newMessageDialog = new Dialog({
			title: 'New Message',
			body: dialogHtml,
			buttons: {
		    	'Save': function() {
		    		addMessage();
		    	}
		    },
			closeButtonCssClass: 'admin',
			buttonsCssClass: 'admin'
		});
		
		$('#addMessageButton').click(function() {
			newMessageDialog.show();
		});
		
		return newMessageDialog;
	}
	
	function addMessage() {
		var message = $('#newMessage').val();
		var severity = $('#newMessageSeverity').val();
		
		if(message) {
			$.ajax({
				method: 'POST',
				url: contextPath + '/admin/messages/add',
				data: {
					message: message,
					severity: severity 
				},
				complete: function(response, textStatus) {	
					newMessageDialog.hide();
					
					if(textStatus !== 'success') {
						Messages.showResponseMessage(response);
					} else {
						var newMessage = JSON.parse(response.responseText);
						Messages.addMessage(newMessage);
						$('#messagesTable tbody').append(getMessageRowHtml(newMessage));
					}				
				}
			});
		} else {
			new Dialog({
				title: 'Empty Message!',
				message: 'Please enter a message',
				closeButtonCssClass: 'admin',
				buttonsCssClass: 'admin'
			}).show();
		}
	}
	
	function removeMessage($button) {
		var $row = $button.parents('tr');
		var messageId = $button.attr('data-key');
		var spinner = undefined;
		
		$.ajax({
			method: 'POST',
			url: contextPath + '/admin/messages/remove/',			
			data: {
				key: messageId
			},
			beforeSend: function() {				
				spinner = showSpinner({
					element: $button.parents('div.buttonWrapper'),
					css: {'height': 'auto', 'display': 'inline-block'} 
				});
			},
			complete: function(response, textStatus) {
				spinner.hide();
				$row.remove();				
				
				if(textStatus !== 'success') {
					Messages.showResponseMessage(response);					
				} else {
					Messages.removeMessage(messageId);
				}		
			}
		});
	}
	
	function getAddMessageHtml() {		
		var html = '<form class="form-horizontal dialogForm">';
		
		html += '<div class="form-group">';
			html += '<label for="severity" class="col-sm-3 control-label">Severity</label>';
			html += '<div class="col-sm-7">';
				html += '<select id="newMessageSeverity" name="severity" class="form-control">';
					html += '<option value="SUCCESS">Success</option>';
					html += '<option value="INFO">Info</option>';
					html += '<option value="WARN">Warning</option>';
					html += '<option value="ERROR">Error</option>';
				html += '</select>';
			html += '</div>';
		html += '</div>';
		
		html += '<div class="form-group">';
			html += '<label for="message" class="col-sm-3 control-label">Message</label>';
			html += '<div class="col-sm-7"><textarea id="newMessage" class="form-control" type="text" name="message"></textarea></div>';
		html += '</div>';
		
		html += '</form>';
		
		return html;
	}
	
	function getMessageRowHtml(message) {
		var html = '<tr>';
			html += '<td>';
				html += '<strong><span class="glyphicon ' + message.severity.icon + '"/></strong> <span class="message">' + message.message + '</span>';
			html += '</td>';
			html += '<td>' + message.severity + '</td>';
			html += '<td>';
				html += '<div class="buttonWrapper">';
					html += '<button class="btn btn-danger remove" data-key="' + message.key + '">Remove</button>';
				html += '</div>';
			html += '</td>';
		html += '</tr>';
		
		return html;
	}
}