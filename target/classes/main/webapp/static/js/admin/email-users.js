$(function() {
	new EmailUsersForm();
});

function EmailUsersForm() {
	initCkEditors();
	$('#sendButton').click(function() {
		sendMessage();
	});
	
	function initCkEditors() {
		$('.ckEditor').each(function() {			
			CKEDITOR.replace($(this).attr('name'));
			CKEDITOR.config.width = 550;
			CKEDITOR.config.height = 150;
			/*CKEDITOR.config.removePlugins = 'list, image, sourcearea';*/
			CKEDITOR.config.removePlugins = 'list, sourcearea'
			CKEDITOR.config.enterMode = CKEDITOR.ENTER_BR;
		});
	}
	
	function sendMessage() {		
		
		// have to update the underlying textarea for CKEditors
		for (instance in CKEDITOR.instances) {
		    CKEDITOR.instances[instance].updateElement();
		}
		
		$.ajax({
			url: contextPath + '/admin/emailUsers',
			method: 'POST',
			data: $('#emailUsersForm').serialize(),			
			complete: function(response) {
				Messages.showResponseMessage(response);
			}
		});
	}
}