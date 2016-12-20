$(function() {	
	
	$('.ckEditor').each(function() {
		
		CKEDITOR.replace($(this).attr('name'), {
			toolbar: [
				{name: 'clipboard', items: ['Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo']},
				{name: 'editing', items: ['Scayt']},
				{name: 'links', items: ['Link', 'Unlink']},
				{name: 'insert', items: ['SpecialChar']},
				{name: 'tools', items: ['Maximize']},
				{name: 'document', items: ['Source']},
				{name: 'basicstyles', items: ['Bold', 'Italic', 'Strike', '-', 'Superscript', 'Subscript']},
				{name: 'paragraph', items: ['-', 'BidiLtr', 'BidiRtl' ]}
			]
		});
		
		CKEDITOR.config.width = 550;
		CKEDITOR.config.height = 100;
		CKEDITOR.config.removePlugins = 'list, image';	
	});						
	
	initTagsInput($('#relatedTerms'), 'relatedTerms', 'Add Term');		
	initTagsInput($('#contributors'), 'contributors', 'Add Contributor');
	
	$('button.saveButton').click(function() {
		$('#editTermForm').submit();
	});
	
	$('button.cancelButton').click(function() {
		window.location.href = contextPath + '/lexicon?searchText=' + searchText + '&startsWithLetter=' + startsWithLetter + '&selectedPage=' + selectedPage;
	});
});

function initTagsInput($element, inputName, addLabel) {
	$element.tagsInput({
		autocomplete_url: contextPath + '/lexicon/admin/getAutoCompleteList?listType=' + inputName,
		removeWithBackspace: false,
		defaultText: addLabel,
		'height':'200px',
		'width':'400px',
		'delimiter': '|'
	});		
}