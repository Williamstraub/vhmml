// TODO: put this code into an object
var correctionsDialog = new Dialog();

$(function() {
	if($('#correctionsFormTemplate').length) {
		initCorrectionsDialog();
	}
});

function initCorrectionsDialog() {
	if(!isAuthenticated) {
		$('body').on('click', 'input.allowContactYes', function() {
			$('div.contactEmailWrapper input.contactEmail').removeAttr('disabled');
		});
		
		$('body').on('click', 'input.allowContactNo', function() {
			$('div.contactEmailWrapper input.contactEmail').attr('disabled', 'disabled');
		});
	}
}

function showCorrectionsDialog(hmmlProjectNumber) {	
	var $formWrapper = $('#correctionsFormWrapper');
	
	correctionsDialog.show({
		title: 'HMML welcomes corrections, comments, or cataloging information about manuscripts and other items in our collection. Please submit your comments below.',
		body: $formWrapper.html(),
		moveable: true,
		buttons: {
			'Submit': function() {
				var processingMessage = null;
				var $form = correctionsDialog.getBody().find('form.correctionsForm');
				
				if(hmmlProjectNumber) {
					$form.find('#hmmlProjectNumber').val(hmmlProjectNumber);
				}
				
				$.ajax({
					url: contextPath + '/feedback/submitCorrections',
					method: 'POST',
					data: $form.serialize(),
					beforeSend: function() {
						correctionsDialog.hide();
						processingMessage = Messages.addMessage({message: 'Your message is being submitted...', severity: 'WARN'});
					},
					complete: function(response, textStatus) {
						Messages.removeMessage(processingMessage);
						Messages.showResponseMessage(response);														
					}
				});
			}
		}
	});
}