$(function() {
	new ConfigAdmin();
});

function ConfigAdmin() {
	initCkEditors();
	$('#saveConfigButton').click(function() {
		saveConfigValues();
	});
	
	function initCkEditors() {
		$('.ckEditor').each(function() {			
			CKEDITOR.replace($(this).attr('name'));
			CKEDITOR.config.width = 550;
			CKEDITOR.config.height = 150;
			CKEDITOR.config.removePlugins = 'list, image, sourcearea';
			CKEDITOR.config.enterMode = CKEDITOR.ENTER_BR;
		});
	}
	
	function saveConfigValues() {
		var configData = [];
		
		$('#appConfigForm tbody tr').each(function(index) {
			var $row = $(this);
			var configValueKey = $row.attr('data-config-key');
			var ckEditor = CKEDITOR.instances['configValue_' + configValueKey];
			var $input = $row.find('.configValue');
			var value = $input.val();
			
			if(ckEditor) {
				value = ckEditor.getData();
			} else if($input.attr('type') == 'checkbox') {
				value = $input.is(':checked') ? 'true' : 'false'; 
			} 
				
			configData[index] = {
				key: configValueKey,
				inputType: $row.attr('data-config-type'),
				value: value
			};
		});
		
		$.ajax({
			url: contextPath + '/admin/config',
			method: 'POST',
			data: JSON.stringify(configData),
			contentType: 'application/json',			
			complete: function(response) {
				Messages.showResponseMessage(response);
			}
		});
	}
}

