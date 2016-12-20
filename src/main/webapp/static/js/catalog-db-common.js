var pageImages = null;
var imageIconDialog = new Dialog();
var confirmDialog = new Dialog();
var messageDialog = new Dialog();

$(function() {
	
	setIconImage($('#iconImage'));
	initCkEditors();
	initTextFilters();
	
	window.onbeforeunload = function () {
		var message = "All unsaved changes will be lost.",
		e = e || window.event;
		// For IE and Firefox
		if (e) {
			e.returnValue = message;
		}		
		
		// For Safari
		return message;
	};	
	
	$('input.switch').bootstrapSwitch({
		size: 'small',
		onColor: 'success',
		offColor: 'danger',
		onText: 'active',
		offText: 'pending'
	});
	
	$('#selectIconButton').click(function() {
		selectIcon();		
	});
	
	$('body').on('click', '#clearIconButton', function() {
		clearIcon()
	});
	
	$('button.cancel').click(function() {
		var closeUrl = $('button.saveAndClose').attr('data-close-url')
		window.location.replace(contextPath + closeUrl);
	});

	$('button.save').click(function() {
		save($(this).hasClass('saveAndClose'), true);					
	});
	
	$('button.delete').click(function() {
		deleteObject();				
	});	
	
	var $vhmmlDialog = $('#vhmml-dialog');
	
	$vhmmlDialog.on('click', $('#moreImagesButton'), function() {
		showMoreImages();
	});
	
	$vhmmlDialog.on('click', 'img.pageIcon', function() {
		var $image = $(this);
		var imageName = $image.attr('data-image-name');
		
		$.ajax({
			url: contextPath + '/image/setObjectIcon/',
			data: {
				objectType: $('form').attr('data-object-type'),
				objectId: $('input.objectId').val(), 
				iconName: imageName,					
			},
			method: 'POST',
			success: function() {				
				setIconImage($image);
			},
			error: function(response) {
				Messages.addMessage({
					message: response.responseText,
					severity: 'ERROR'
				});
			},
			complete: function() {
				imageIconDialog.hide();
			}
		});
	});	
});

function setIconImage($image) {
	
	var $iconName = $('#iconName');
	var $iconImage = $('#iconImage');
	var $iconPlaceholder = $('div.objectIcon .iconPlaceholder');
	var $clearIconButton = $('#clearIconButton');
	
	if($image && $image.attr('data-image-name')) {
		var imageName = $image.attr('data-image-name');
		$iconName.val(imageName);
		$iconImage.attr('src', $image.attr('src')).show();
		$iconPlaceholder.hide();
		$clearIconButton.show();
	} else {
		$iconName.val('');
		$iconImage.hide();
		$iconPlaceholder.show();
		$clearIconButton.hide();
	}
	
}

function initCkEditors($element) {
	
	var $textareas = $element ? $element.find('textarea.ckEditor') : $('textarea.ckEditor');
	
	$textareas.each(function() {
		var $textarea = $(this);
		var fieldName = $textarea.attr('id');
		var toolbarButtons = [{name: 'basicstyles', items: [ 'Italic', 'Superscript', 'Subscript']},
		        			    {name: 'insert', items: ['SpecialChar']},
		        			    {name: 'document', items: ['Source']},
		        			    {name: 'paragraph', items: ['-', 'BidiLtr', 'BidiRtl' ]}	
		        			  ];
		
		if (CKEDITOR.instances[fieldName]) {
			CKEDITOR.instances[fieldName].destroy();
		}
		
		// there might be a cke div even if there wasn't a ck editor in the instances list in the case where we add a new part or
		// new item because we copied the previous part/item so there's a ckeditor text area with an name like decorations[1] but 
		// the CKEditor.instances list only has the one we copied (decorations[0])
		$textarea.next('div.cke').remove();
		
		if($('form').attr('data-object-type') === 'FOLIO') {
			toolbarButtons.unshift({name: 'basicstyles', items: ['Bold']});
			toolbarButtons.push({name: 'clipboard', items: ['PasteText', 'PasteFromWord']});
			toolbarButtons.push({name: 'paragraph', items: ['NumberedList', 'BulletedList', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-', 'BidiLtr', 'BidiRtl' ]});
			toolbarButtons.push({name: 'styles', items : ['Font']});
		} 	
		
		CKEDITOR.replace(fieldName, {
			toolbar: toolbarButtons 			
		});

		CKEDITOR.config.width = 550;
		CKEDITOR.config.height = 100;
	});
}

function removeCkEditors($element) {
var $textareas = $element ? $element.find('textarea.ckEditor') : $('textarea.ckEditor');
	
	$textareas.each(function() {
		var $textarea = $(this);
		var fieldName = $textarea.attr('id');
		
		if (CKEDITOR.instances[fieldName]) {
			CKEDITOR.instances[fieldName].destroy();
		}
	});
}

function clearCkEditors($element) {
	var $textareas = $element ? $element.find('textarea.ckEditor') : $('textarea.ckEditor');
	
	$textareas.each(function() {
		var $textarea = $(this);
		var fieldName = $textarea.attr('id');
		
		if (CKEDITOR.instances[fieldName]) {
			CKEDITOR.instances[fieldName].setData('');
		}
	});	
}

function initTextFilters() {
	$('input.digitsOnly').textinput({'filter': 'digits'}); 
	$('input.integersOnly').textinput({'whitelist': '0123456789-'}); 
	$('input.positiveDecimalsOnly').textinput({'whitelist': '0123456789.'});
	
	$('div.container').arrive('input.digitsOnly', function(){
		$(this).textinput({'filter': 'digits'}); 			
	});
	
	$('div.container').arrive('input.integersOnly', function(){
		$(this).textinput({'whitelist': '0123456789-'}); 			
	});
	
	$('div.container').arrive('input.positiveDecimalsOnly', function(){
		$(this).textinput({'whitelist': '0123456789.'}); 			
	});
}

function save(close, showSuccess) {
	
	var $form = $('form');
	
	if($form.valid()) {
		if(typeof disableTypeSpecificFields === 'function') {
			disableTypeSpecificFields();
		}
		
		for (instance in CKEDITOR.instances) {
		    CKEDITOR.instances[instance].updateElement();
		}
		
		$.ajax({
			url: $form.attr('action'),
			method: 'POST',
			data: $form.serialize(),
			beforeSend: function() {
				Messages.removeAll();
				$('#savingMessage').show();
			},
			success: function(response) {
				$('input.objectId').val(response.objectId);
				$('span.objectId').text(response.objectId);
				$('input.projectNumber').val(response.projectNumber);
				
				if(close) {
					window.onbeforeunload = null;
					window.location = contextPath + $('button.saveAndClose').attr('data-close-url');									
				} else if(showSuccess && response.message) {
					$('button.delete').removeClass('hidden');
					Messages.addMessage({severity: 'SUCCESS', message: response.message});
				}
			},
			error: function(response) {
				if(response.status === 400) {
					Messages.addMessage({severity: 'ERROR', message: 'Please provide values for the missing required fields.'});
					Validation.showFieldValidationMessages(response.responseJSON);
				} else {
					Messages.addMessage({severity: 'ERROR', message: response.responseText});					
				}				
			},
			complete: function() {
				$('#savingMessage').hide();
				// have to re-disable authority list fields if the user can't edit them
				var $authorityListUrls = $('input.lcUri, input.viafUri');
				
				if($authorityListUrls.length && !authorityListPermission) {
					$authorityListUrls.attr('disabled', 'disabled');
				}
			}
		});
	}
}

function deleteObject() {
	
	// this url is on the delete button because this function is used on both the catalog database add/edit and the folio add/edit pages
	var url = $('button.delete').attr('data-delete-url') + '/' + $('input.objectId').val();
	var $saveAndCloseButton = $('button.saveAndClose');
	var closeUrl = $saveAndCloseButton.attr('data-close-url');
	var homePage = $saveAndCloseButton.attr('data-home-page');  
	
	confirmDialog.show({
		title: 'Delete?',
		message: 'Are you sure you want to delete this record?',
		closeButtonLabel: 'No',
		buttons: {
			'Yes': function() {					
				$.ajax({
					url: url,
					method: 'DELETE',
					success: function(response) {
						if(response === 'DELETE_SUCCESS') {
							messageDialog.show({
								message: 'The record was successfully deleted. You will be sent to the ' + homePage + ' home page after closing this dialog.',
								closeButtonLabel: 'OK',
								closeButtonFunction: function() {
									window.onbeforeunload = null;
									window.location.replace(contextPath + closeUrl);
								}
							});	
						}								
					},
					error: function(response) {
						Messages.addMessage({
							message: response.responseText,
							severity: 'ERROR'
						});
					}
				});					
			}
		}
	});		
}

function selectIcon() {
	var objectType = $('form').attr('data-object-type');
	var objectId = $('input.objectId').val();
	var projectNumber = $('input.projectNumber').val();
	
	// they need to save the object before they can select an icon image
	if(!objectId) {
		messageDialog.show({
			message: 'Please set the ' + (objectType === 'FOLIO' ? 'Folio Object Number' : 'HMML Project Number') + ' and click the save button to select an icon',
			closeButtonLabel: 'OK'
		});
	} else if (projectNumber && !pageImages) {		
		$.ajax({
			url: contextPath + '/image/imageList/' + projectNumber + '?objectType=' + objectType,
			beforeSend: function() {
				imageIconDialog.show({
					title: 'Finding Images',
					message: 'Searching for images, please wait...',
					cssClass: 'imageIconDialog',
					moveable: true
				});
			},
			success: function(images) {						
				if(images && images.length) {
					pageImages = JSON.parse(images);								
				}
										
				showImageSelectionDialog();
			}
		});
	} else {
		showImageSelectionDialog();
	}
}

function clearIcon() {
	
	$.ajax({
		url: contextPath + '/image/clearObjectIcon/',
		data: {
			objectType: $('form').attr('data-object-type'),
			objectId: $('input.objectId').val()			
		},
		method: 'POST',
		success: function() {
			setIconImage(null);						
		},
		error: function(response) {
			Messages.addMessage({
				message: response.responseText,
				severity: 'ERROR'
			});
		}
	});
}

function showImageSelectionDialog() {
	
	var objectType = $('form').attr('data-object-type');
	
	if(pageImages && pageImages.length) {
		var imagesHtml = '<div id="imageThumbnails" class="row">';
		var projectNumber = $('input.projectNumber').val();
		
		for(var i = 0; i < 21 && pageImages[i]; i++) {
			imagesHtml += '<div class="col-lg-4"><img class="pageIcon" src="' + contextPath + '/image/thumbnail/' + objectType + '/' + projectNumber + '/' + pageImages[i] + '" data-image-name="' + pageImages[i] + '" title="' + pageImages[i] + '"/></div>';
		}
		
		imagesHtml += '</div>';
		imagesHtml += '<div class="col-lg-12 text-center"><button id="moreImagesButton" class="btn catalog-database">More Images</button></div>';
		
		imageIconDialog.show({
			title: 'Select Page Icon',
			message: imagesHtml,
			cssClass: 'imageIconDialog',
			moveable: true
		});			
	} else {
		imageIconDialog.show({
			title: 'No Images Available',
			message: 'There are currently no images available for this item.',
			cssClass: 'imageIconDialog'
		});
	}	
}

function showMoreImages() {
	
	if(pageImages) {
		var $thumbnails = $('#imageThumbnails');
		var objectType = $('form').attr('data-object-type');
		var html = '';
		var start = $thumbnails.find('img.pageIcon').length;
		
		if(start < pageImages.length) {
			var end = start + 21 < pageImages.length ? start + 21 : pageImages.length;
			var projectNumber = $('input.projectNumber').val();
			
			for(var i = start; i < end; i++) {
				html += '<div class="col-lg-4"><img class="pageIcon" src="' + contextPath + '/image/thumbnail/' + objectType + '/' + projectNumber + '/' + pageImages[i] + '" data-image-name="' + pageImages[i] + '" title="' + pageImages[i] + '"/></div>';
			}
			
			$thumbnails.append(html);
			
			if(end == pageImages.length) {
				$('#moreImagesButton').hide();
			}
		}						
	}		
}