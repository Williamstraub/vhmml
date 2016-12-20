var addListOptionDialogTemplate;	
var addListOptionDialog = new Dialog();

var recordType = null;

$(function() {
	addListOptionDialogTemplate = Handlebars.compile($("#add-list-option-dialog-template").html());
		
	window.onload = function() {
		// we always lock the record on page load because a page refresh will cause
		// the onunload event to fire, which unlocks the record because we want the record unlocked when
		// the user leaves this page even if they just close the tab without saving, etc. - so we need 
		// to make sure lockRecord is called every time this page is loaded.
		setLocked(true);
	}

	window.onunload = function() {
		setLocked(false);
	};
	
	if($('#hasParts').val() === 'true' || $('input.hasArchivalContent').val() === 'true') {
		$('button.add').hide();
	}
	
	$(document).on('typeahead:select', function(e, selected) {
		var $parentField = $(e.target).parents('div.authorityField');
		$parentField.find('input.displayName').val(selected.displayName);
		$parentField.find('input.lcUri').val(selected.authorityUriLC);
		$parentField.find('input.viafUri').val(selected.authorityUriVIAF);
	});
	
	initTypeAhead($('input.contributor'));
	initTextFilters();	
	initGroupBoxes();
	
	$('#readingRoomObjectForm').on('click', 'div.formSectionToggle', function(e) {
		// some form section headers have buttons, we don't want clicking those to toggle the accordion
		if(!$(e.target).is(':button')) {
			var $this = $(this);
			var $parentHeading = $this.parent('h3');
			
			// if the triangle toggle icon is hidden then expand/collapse is disabled 
			if(!$parentHeading.find('span.glyphicon').is(':hidden')) {
				var $formSection = $parentHeading.next('div.formSection');
				
				if($formSection.is(':hidden')) {
					$formSection.slideDown(function() {
						$this.find('span.glyphicon').removeClass('glyphicon-triangle-right').addClass('glyphicon-triangle-bottom');
						var $parentHeading = $this.parents('h3')
						$parentHeading.find('button.add').show();
						$parentHeading.next('div.formSection').next('button.add').show();
					});
				} else {
					$formSection.slideUp(function() {
						$this.find('span.glyphicon').removeClass('glyphicon-triangle-bottom').addClass('glyphicon-triangle-right');
						var $parentHeading = $this.parents('h3')
						$parentHeading.find('button.add').hide();
						$parentHeading.next('div.formSection').next('button.add').hide();
					})
				}				
			}				
		}
	});
	
	$('#toggleAll').click(function() {
		var $this = $(this);
		var $icon = $this.find('span.glyphicon');
		var $sections = $('#recordType').val() == 'ARCHIVAL_OBJECT' ? $('div.formSection').not('.objectParts') : $('div.formSection').not('.archivalContent');
		
		$icon.toggleClass('glyphicon-triangle-bottom').toggleClass('glyphicon-triangle-right');
		
		if($icon.hasClass('glyphicon-triangle-right')) {
			$sections.slideUp();
			$('div.formSectionToggle span.glyphicon').removeClass('glyphicon-triangle-bottom').addClass('glyphicon-triangle-right')
			// we hide the add part & add item buttons if there are parts already in the object so they have to toggle the parts section open to see 
			// the add button, this keeps users from accidentally adding new parts by instinctively clicking the add button to open the accordion 
			if($('#hasParts').val() === 'true') {
				$('button.addPart').hide();
			}
			$this.find('label').text('Open All');			
		} else {
			$sections.each(function() {
				var $formSection = $(this);
				// if there aren't any parts we don't open the parts section, they have to add one first
				if($('#hasParts').val() === 'true' || !$formSection.prev('h3').hasClass('objectParts')) {
					$formSection.slideDown();
				}
				$('div.formSectionToggle span.glyphicon').removeClass('glyphicon-triangle-right').addClass('glyphicon-triangle-bottom')			
				$('button.add').show();			
				$this.find('label').text('Collapse All');
			});
			
			
		}			
	});
	
	$('#recordType').on('focus', function () {       
        previous = this.value;
    })
	
	$('#recordType').change(function() {
		var $recordType = $(this);
		var newRecordType = $recordType.val();
		var previousRecordType = recordType;
		
		if(previousRecordType && newRecordType != previousRecordType && previousRecordType != 'READING_ROOM_OBJECT') {
			
			if(newRecordType === 'ARCHIVAL_OBJECT' || previousRecordType === 'ARCHIVAL_OBJECT') {
				new Dialog({
					title: 'Change not allowed',
					message: 'Sorry, you cannot change objects from archival to non-archival or vice-versa.',
					closeButtonLabel: 'OK',
					closeButtonCssClass: 'catalog-database',
					closeButtonFunction: function() {
						$recordType.val(previousRecordType);
					}
				}).show();				
			} else {
				confirmDialog.show({
					title: 'Change Record Type?',
					message: 'By changing the type of record this will also change the available metadata fields and the type of part(s) that can be added. Do you wish to proceed?',
					closeButtonLabel: 'No',
					closeButtonFunction: function() {
						$recordType.val(previousRecordType);
					},
					buttons: {
						'Yes': function() {					
							showFieldsForRecordType(newRecordType);	
							confirmDialog.hide();
						}
					}
				});
			}			
		} else {
			showFieldsForRecordType(newRecordType);
		}
	});
	
	var $form = $('#readingRoomObjectForm');
	
	$form.on('change', 'select.authorityListField', function() {
		selectAuthorityUrls($(this));
	});
	
	$form.on('change', 'select.hasAssociatedUri', function() {
		selectAssociatedUri($(this));
	});
	
	$form.on('change', '#accessRestriction', function() {
		selectAccessRestriction($(this));
	});
	
	$form.on('click', 'button.addField', function() {
		addRepeatableField($(this).parents('div.repeatableField'))
	});
	
	$form.on('click', 'button.removeField', function() {
		deleteRepeatableField($(this).parents('div.repeatableFieldGroup'));
	});
	
	$('div.container').on('click', 'button.addListOption', function() {
		var $addButton = $(this);
		var title = $addButton.text();			
		
		addListOptionDialog.show({
			title: title,
			body: addListOptionDialogTemplate(),
			moveable: true,
			buttonsCssClass: 'catalog-database',		
			buttons: {
				'Add': function() {
					addListOption($addButton);
				}	
			}
		});
		
		if($addButton.attr('data-auth-list-field')) {
			$('#addListOptionForm div.authorityUri').show();
		} else {
			$('#addListOptionForm div.authorityUri').hide();
		}
	});
			
	$('select.authorityListField').each(function() {
		selectAuthorityUrls($(this));	
	});
	
	$('button.addPart + ul li').click(function(e) {
		addPart($(this).attr('data-part-type'));
	});
	
	$('button.addArchivalItem + ul li').click(function(e) {
		addArchivalItem();
	});
	
	$('div.container').on('click', 'button.addContentItem', function(e) {
		var $contentSection = $(this).parents('div.objectPart').find('div.partContents');
		addContentItem($contentSection);
	});			
	
	var $container = $('div.container');		
	
	$container.on('change', 'select.partType', function() {
		setPartType($(this).val(), $(this).parents('div.objectPart'));
	});
	
	$container.on('click', 'div.objectPart button.delete', function() {
		deletePart($(this).parents('div.objectPart'));
	});
	
	$container.on('click', 'div.contentItem button.delete', function() {
		deleteContentItem($(this).parents('div.contentItem'));
	});
	
	$container.on('change', 'input.partNumber', function() {
		$partNumberInput = $(this);
		var newPartNumber = $partNumberInput.val();
		var $parentPart = $partNumberInput.parents('div.objectPart');
		$parentPart.find('h3 span.partLabel').text(' Part ' + newPartNumber + ' Description');
		$parentPart.find('h3 span.partItemsLabel').text(' Part ' + newPartNumber + ' Items');
	});
	
	$container.on('change', 'input.itemNumber', function() {
		$itemNumberInput = $(this);
		$itemNumberInput.parents('div.contentItem').find('h3 span.contentLabel').text('Item ' + $itemNumberInput.val() + ' Description');
	});
	
	$container.on('change', 'input.archivalFolder', function() {
		$input = $(this);
		$input.parents('div.contentItem').find('h3 span.archivalFolder').text($input.val());
	});
	
	$container.on('change', 'input.archivalItem', function() {
		$input = $(this);
		$input.parents('div.contentItem').find('h3 span.archivalItem').text($input.val());
	});
	
	$('.importedValue').next('.glyphicon-remove-circle').click(function() {
		removeImportedValue($(this));
	});
	
	if(editing) {
		showFieldsForRecordType($('#recordType').val());
		
		if($('#hasParts').val() === 'true') {
			setPartsToggle(false);
		}
		
		if($('input.hasArchivalContent').val() === 'true') {
			setContentItemContainerToggle(false);
		}
	}
});

function initTypeAhead($inputs) {
	// disabling typeahead until we have time to refine it because it's causing cataloging issues
	/*
	var getContributors = new Bloodhound({
		datumTokenizer : Bloodhound.tokenizers.obj.whitespace('value'),
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		remote : {
			url : contextPath + '/catalogDatabase/authorityList/contributors/%QUERY',
			wildcard : '%QUERY'
		}
	});
	
	$inputs.typeahead(null, {
		name : 'contributors',
		display : 'name',
		source : getContributors
	});
	*/
}

function removeGroupBoxes($element) {	
	$element.find('div.ui-groupbox, div.ui-groupbox-clear').remove();
}

function initGroupBoxes($element) {
	var $selectLists = $element ? $element.find('select.groupbox') : $('select.groupbox');
	
	$selectLists.each(function() {
		initGroupBox($(this));		
	});
}

function initGroupBox($selectList) {
	var $groupbox = $('<div class="groupbox">');
	// if there already was a group box, remove it
	$selectList.prevAll('div.groupbox').remove();
	$selectList.before($groupbox);
	var available = [];
	var selected = [];
	
	$selectList.find('option').each(function() {
		var $option = $(this);
		
		if($option.attr('selected')) {
			selected.push({'id': $option.attr('value'), 'name': $option.text(), 'option': $option});				
		} else {
			available.push({'id': $option.attr('value'), 'name': $option.text(), 'option': $option});
		}
	});
	
	$groupbox.groupbox({               
		'labelList1': 'Available',
		'itemsList1': available,
		'labelList2': 'Selected',
		'itemsList2': selected,
		'afterMove': function(event, data) {
			var selectedValues = $selectList.val() ? $selectList.val() : [];
			var selectedOption = data.item.option;
			
			if(selectedOption.attr('selected')) {				
				selectedOption.removeAttr('selected');
				var index = selectedValues.indexOf(selectedOption.attr('value'));
				selectedValues.splice(index, 1);
			} else {				
				selectedOption.attr('selected', 'selected');
				selectedValues.push(selectedOption.attr('value'));
			}
			
			$selectList.val(selectedValues);			
		}
    });
}

function showFieldsForRecordType(selectedType) {
	var selectTypeDisplayName = $('#recordType option:selected').text();
	var previousRecordType = recordType;
	recordType = selectedType;
	
	if(recordType && recordType !== 'READING_ROOM_OBJECT') {
		
		$('div.objectData').removeClass(previousRecordType).addClass(recordType);
		
		// if no record type was selected before, everything was hidden
		if(!previousRecordType || previousRecordType === 'READING_ROOM_OBJECT') {
			$('#toggleAll, .objectData, h3.objectParts').show();
		}
		
		if(recordType === 'ARCHIVAL_OBJECT') {			
			var headingText = $('#readingRoomObjectId').val() ? 'Edit ' + selectTypeDisplayName + ' Record' : 'Add a new archival record';
			$('h2 span.mainHeading').text(headingText);			
			$('h3.objectData span.accessDataHeading').text(' Archival Object Access Data');
			$('h3.objectData span.objectDataHeading').text(' Archival Object Data');
			$('h3.objectData span.physicalDescriptionHeading').text(' Archival Object Physical Description');
			var $archivalFields = $('#readingRoomObjectForm .archivalField');
			$archivalFields.show();
			// the ck editor & groupbox hides the original text/select field when rendering, we don't want to show it
			$archivalFields.find('textarea.ckEditor').hide();
			$('select.archivalField.groupbox').hide();
			$('.objectParts').hide();			
			$('h3.archivalContent').show();
			setContentToggle($('.archivalContent.contentSection'), false);
		} else {
			var headingText = $('#readingRoomObjectId').val() ? 'Edit ' + selectTypeDisplayName + ' Record' : 'Add a new catalog record';
			$('h2 span.mainHeading').text(headingText);
			$('h3.objectData span.accessDataHeading').text(' Object Access Data');
			$('h3.objectData span.objectDataHeading').text(' Object Data');
			$('h3.objectData span.physicalDescriptionHeading').text(' Object Physical Description');		
			$('#readingRoomObjectForm .archivalField').hide();
			$('button.addPart').next('ul').find('li').show().not('.' + recordType).hide();
			$('.archivalContent').hide();
			$('h3.objectParts').show();			
		}				
	} else {
		$('#toggleAll, .objectData, .objectParts').hide();				
	}
}

function addRepeatableField($repeatableField) {
	// destroy the typeahead on the original field so we don't copy all the typeahead markup to the new field	
	var $originalFieldContributors = $repeatableField.find('input.contributor');
	$originalFieldContributors.typeahead('destroy');
	var repeatableFieldName = $repeatableField.attr('id');
	var $repeatableFieldGroups = $repeatableField.children('div.repeatableFieldGroup');
	var $repeatableFieldGroupHtml = $($repeatableFieldGroups[0].outerHTML);
	// don't want to copy notes from the previous field, they have stuff like imported values so they're not relevant to a new field
	$repeatableFieldGroupHtml.find('label.note').remove();
	
	// if the inputs belong to a list, increment the list index on the new fields (tt-hint is an input inserted by twitter bootstrap typeahead)
	var $inputs = $repeatableField.find('input, select').not('.tt-hint');	
	var listName = null;
	var listIndex = null;
	
	for(var i = $inputs.length - 1; i >= 0; i--) {
		var $input = $($inputs[i]);
		listName = $input.attr('data-list-name');
		listIndex = $input.attr('data-list-index');
		
		if(listName && listIndex) {
			break;
		}
	}
	
	if(listName && listIndex) {
		setFieldListIndex($repeatableFieldGroupHtml, listName, ++listIndex, true);
	}
	
	$repeatableField.append($repeatableFieldGroupHtml);
	// re-initialized typeahed on the original field and the new one
	initTypeAhead($originalFieldContributors);
	initTypeAhead($repeatableFieldGroupHtml.find('input.contributor'));
	initCkEditors($repeatableFieldGroupHtml);
}	

function addListOption($addButton) {				
	var $dialogForm = $('#vhmml-dialog form[name="addListOptionForm"]');
	var type = $addButton.attr('data-add-type');
	var $selectList = $addButton.prevAll('select');
	var parentOptionId = null;
	
	switch(type) {
		case 'CITY':
			parentOptionId = $('#countryId').val();
			break;
		case 'REPOSITORY':
			parentOptionId = $('#cityId').val();
			break;
	}
	
	$.ajax({
		url: contextPath + '/catalogDatabase/addListOption',
		method: 'POST',
		data: {
			type: type,
			name: $dialogForm.find('input[name="name"]').val(),
			authorityUriLC: $dialogForm.find('input[name="authorityUriLC"]').val(),
			authorityUriVIAF: $dialogForm.find('input[name="authorityUriVIAF"]').val(),
			parentOptionId: parentOptionId
		},
		success: function(newOption) {
			var lcUri = newOption.authorityUriLC ? newOption.authorityUriLC : '';
			var viafUri = newOption.authorityUriVIAF ? newOption.authorityUriVIAF : '';
			var optionValueProp = $selectList.attr('data-value-property');
			var value = optionValueProp ? newOption[optionValueProp] : newOption.id;
			var $newOption = $('<option value="' + value + '" data-authority-uri-lc="' + lcUri + '" data-authority-uri-viaf="' + viafUri + '">' + newOption.name + '</option>');
			$selectList.append($newOption);
			sortOptions($selectList);
			$selectList.find('option[value="' + value + '"]').prop('selected', true).attr('selected', 'selected');
			
			if($selectList.hasClass('groupbox')) {
				initGroupBox($selectList);
			}
			
			// fire change event so any dependent lists get updated correctly, e.g. if country is added
			// then we should select the country and show the list of cities for that country and unhide the
			// add city button
			$selectList.change();			
		},
		error: function(response) {
			Messages.addMessage({
				message: response.responseText,
				severity: 'ERROR'
			});
		},
		complete: function() {
			addListOptionDialog.hide();
		}
	});
}

function sortOptions($selectList) {
	var $options = $selectList.children('option');
	var options = [];
	var optionsHtml = '';
	
	$options.each(function() {
		var $this = $(this);
		options.push({name: $this.text(), html: $this[0].outerHTML});
	});
	
	options.sort(function(a, b) {
		var nameA = a.name.toUpperCase();
		var nameB = b.name.toUpperCase();
		
		if (nameA < nameB) {
			return -1;
		}
		
		if (nameA > nameB) {
			return 1;
		}

		return 0;
	});
	
	for(var i = 0; i < options.length; i++) {
		optionsHtml += options[i].html;
	}
	
	$selectList.html(optionsHtml);
}

function selectAuthorityUrls($selectList) {
	var $genericUri = $selectList.parents('div.form-group').next('div.form-group').find('input.authorityUriGeneric');
	
	if($genericUri.length) {
		$genericUri.val($selectList.find(':selected').attr('data-authority-uri-generic'));
	} else {
		var $authorityUriLc = $selectList.parents('div.form-group').next('div.form-group').find('input.authorityUriLC');		
		$authorityUriLc.val($selectList.find(':selected').attr('data-authority-uri-lc'));
		var $authorityUriViaf = $authorityUriLc.parents('div.form-group').next('div.form-group').find('input.authorityUriVIAF');
		$authorityUriViaf.val($selectList.find(':selected').attr('data-authority-uri-viaf'));	
	}		
}

function selectAssociatedUri($selectList) {
	var $uri = $selectList.parents('div.form-group').next('div.form-group').find('input.associatedUri');
	$uri.val($selectList.find(':selected').attr('data-associated-uri'));
}

function selectAccessRestriction($selectList) {
	var accessRestriction = $selectList.val();
	
	if(accessRestriction === 'ON_SITE_ONLY' || accessRestriction === 'ON_SITE_ONLY_ORDER_SCAN') {
		$('#iconImage, #selectIconButton').hide();
	} else {
		$('#iconImage, #selectIconButton').show();
	}
}

function deleteRepeatableField($fieldGroup) {
	
	confirmDialog.show({
		title: 'Delete?',
		message: 'Are you sure you want to delete this field?',
		closeButtonLabel: 'No',
		buttons: {
			'Yes': function() {					
				$fieldGroup.remove();
				confirmDialog.hide();
			}
		}
	});
}

function closeAllFormSections(onComplete) {
	var $formSections = $('div.formSection');
	var i = 0;
	
	$formSections.slideUp(function() {
		$formSections.prev('h3').find('span.glyphicon').removeClass('glyphicon-triangle-bottom').addClass('glyphicon-triangle-right');
		i++;
		
		if(onComplete && i == $formSections.length) {
			onComplete.call();
		}
	});		
}

function addPart(partType) {		
	var $objectPartsSection = $('div.formSection.objectParts');					
	var $parts = $objectPartsSection.find('div.objectPart');
	var $contributors = $parts.find('input.contributor').typeahead('destroy');
	// copy the first part		
	var $newPart = $($parts[0]);
	var $hasParts = $('#hasParts');
		
	if($hasParts.val() === 'true') {			
		$newPart = $($parts[$parts.length - 1].outerHTML);		
		$newPart.find('span.partItemsLabel').text(' Part Items');
		$newPart.attr('data-part-index', $parts.length);
		$newPart.find('div.repeatableFieldGroup').not(':first-child').remove();
		$newPart.find('div.contentItem').not(':first-child').remove();		
		setFieldListIndex($newPart, 'parts', $parts.length, 'partNumber', true);			
		$newPart.attr('class', 'objectPart ' + partType)
		$newPart.attr('data-part-id', '');		
		$newPart.find('input[type="checkbox"]').attr('checked', false);
		$newPart.find('div.contentItem').attr('data-item-id', '');	
		$newPart.find('input.hasContent').val('false');
		$newPart.find('select option').removeAttr('selected');		
		$objectPartsSection.append($newPart);
		initTypeAhead($newPart.find('input.contributor'));
		initCkEditors($newPart);
	} else {
		// if the object doesn't have any parts yet, we just need to show the fields for the first part (they're already rendered because we always keep one around to copy)
		$hasParts.val('true');	
	}
	
	setPartType(partType, $newPart);
	$newPart.find('input.hasContent').val('false');
	$newPart.find('span.partLabel').text(' Part Description');	
	$newPart.find('select.partType').val(partType);	
	removeGroupBoxes($newPart);
	initGroupBoxes($newPart);
	initTypeAhead($contributors);
	
	// close all other parts and open the new one since that's probably what they want to work on
	closeAllFormSections(function() {
		// enable the expand/collapse link and open the parts section once the other sections are done closing
		setPartsToggle(true);
	});
		
	$objectPartsSection.slideDown(function() {
		$newPart.find('div.formSectionToggle').click();
	});	
	
	var $partContentSection = $newPart.find('div.partContents');
	$partContentSection.find('span.contentLabel').text('Item Description');
	setContentToggle($partContentSection, false);
	$newPart.find('.importedValue').remove();
	
	saveReadingRoomObject();
}

function addArchivalItem() {		
	var $archivalContentSection = $('div.formSection.archivalContent');					
	var $items = $archivalContentSection.find('div.contentItem');
	
	// copy the first item
	var $newItem = $($items[0]);
	var $hasContent = $('input.hasArchivalContent');
		
	if($hasContent.val() === 'true') {			
		$newItem = $($items[$items.length - 1].outerHTML);
		$newItem.attr('data-item-index', $items.length);
		$newItem.find('div.repeatableFieldGroup').not(':first-child').remove();		
		setFieldListIndex($newItem, 'content', $items.length, true);
		$newItem.attr('data-item-id', '');		
		$newItem.find('input[type="checkbox"]').attr('checked', false);
		$newItem.find('select option').removeAttr('selected');
		$archivalContentSection.find('.contentItem').last().after($newItem);
		// initTypeAhead($newPart.find('input.contributor'));
		initCkEditors($newItem);
	} else {
		// if the object doesn't have any content yet, we just need to show the fields for the first item (they're already rendered because we always keep one around to copy)
		$hasContent.val('true');	
	}
	
	$newItem.find('select.itemType').val('NOT_SPECIFIED');
	$newItem.find('input.archivalFolder').val('').change();
	$newItem.find('input.archivalItem').val('').change();
	removeGroupBoxes($newItem);
	initGroupBoxes($newItem);
	// initTypeAhead($contributors);
		
	$newItem.find('.importedValue').remove();
	
	closeAllFormSections(function() {
		$archivalContentSection.slideDown(function() {			
			var $toggleIcon = $archivalContentSection.prev('h3').find('span.glyphicon');				
			$toggleIcon.removeClass('glyphicon-triangle-right hidden').addClass('glyphicon-triangle-bottom');
			$newItem.find('div.formSectionToggle').click();						
		});
		
		/*
		$archivalContentSection.slideDown(function() {
			$newItem.find('div.formSectionToggle').click();
		});
		*/	
	});
	
	saveReadingRoomObject();
}

function setPartsToggle(open) {
	var $objectPartsHeading = $('div.objectParts').prev('h3.objectParts');		
	var $partsSection = $objectPartsHeading.next('div.formSection.objectParts');
	var $toggleIcon = $objectPartsHeading.find('span.glyphicon');
	
	$toggleIcon.removeClass('hidden');
	$partsSection.find('h3.partContents').show();
	
	if(open) {
		$toggleIcon.removeClass('glyphicon-triangle-right').addClass('glyphicon-triangle-bottom');
	} else {
		$toggleIcon.removeClass('glyphicon-triangle-bottom').addClass('glyphicon-triangle-right');	
	}
	
	$partsSection.children('div.objectPart').each(function() {
		var $part = $(this);
		if($part.find('input.hasContent').val() === 'true') {
			$part.find('h3.partContents span.glyphicon').removeClass('hidden');
		} else {
			$part.find('button.addContentItem').show();
		}
	});				
}

function setContentItemContainerToggle(open) {
	// for archival items, the content container is right under the object, for non-archival it's the parts section
	var $contentItemContainer = $('div.contentItemContainer');
	var $contentHeading = $('div.contentItemContainer').prev('h3');	
	var $toggleIcon = $contentHeading.find('span.glyphicon');	
	$toggleIcon.removeClass('hidden');
	
	// if it's not archival material, there will be a part content heading under the parts section
	$contentItemContainer.find('h3.partContents').show();
	
	if(open) {
		$toggleIcon.removeClass('glyphicon-triangle-right').addClass('glyphicon-triangle-bottom');
	} else {
		$toggleIcon.removeClass('glyphicon-triangle-bottom').addClass('glyphicon-triangle-right');	
	}
	
	$contentItemContainer.children('div.objectPart').each(function() {
		var $part = $(this);
		if($part.find('input.hasContent').val() === 'true') {
			$part.find('h3.partContents span.glyphicon').removeClass('hidden');
		} else {
			$part.find('button.addContentItem').show();
		}
	});				
}

function setPartType(partType, $partDiv) {		
	$partDiv.attr('class', 'objectPart ' + partType);	
	disableTypeSpecificFields();
}

function addContentItem($contentSection) {
	var $parentPart = $contentSection.parents('div.objectPart');
	var $items = $contentSection.find('div.contentItem');		
	// destroy typeahead elements so we don't copy all the typeahead markup
	$contentSection.find('input.contributor').typeahead('destroy');	
	var $hasContentInput = $parentPart.find('input.hasContent');
		
	if($hasContentInput.val() === 'true') {
		var $newItem = $($items[$items.length - 1].outerHTML);
		var partIndex = $contentSection.parents('div.objectPart').attr('data-part-index');
		var contentListName = 'parts[' + partIndex + '].contents';
		var itemNumber = $items.length + 1;
		$newItem.find('span.contentLabel').text('Item Description');
		setFieldListIndex($newItem, contentListName, $items.length, 'itemNumber', true);
		$newItem.attr('data-item-id', '');
		$newItem.find('select option').removeAttr('selected');
		$contentSection.append($newItem);
		removeGroupBoxes($newItem);
		initGroupBoxes($newItem);
		initCkEditors($newItem);
	}
	
	$hasContentInput.val('true');	
	initTypeAhead($contentSection.find('input.contributor'));	
	
	$contentSection.slideDown(function() {
		setContentToggle($contentSection, true);			
	});
	
	saveReadingRoomObject();
}

function setContentToggle($contentSection, open) {
	var $toggleIcon = $contentSection.prev('h3').find('span.glyphicon');				
	
	if(open) {
		$toggleIcon.removeClass('glyphicon-triangle-right hidden').addClass('glyphicon-triangle-bottom');
		$contentSection.slideDown();
	} else {
		$toggleIcon.removeClass('glyphicon-triangle-bottom').addClass('glyphicon-triangle-right hidden');	
		$contentSection.hide();
	}		
}

function setFieldListIndex($parentElement, listName, listIndex, newFields) {
	// make a reg ex that will match on listName[x] 
	var escapedFieldName = listName.replace(/\[/g, '\\[').replace(/\]/g, '\\]');
	var fieldNameRegEx = new RegExp(escapedFieldName + '\[[0-9]+\]');
	
	// for each input & select list, increment list indexes on references to the list we're adding to
	// for example, the following is the HTML for the itemNumber field on the second content object of the second part
	// <input id="parts1.contents1.itemNumber" name="parts[1].contents[1].itemNumber" data-list-name="parts[1].contents" data-list-index="1" data-field="itemNumber">
	// if we're adding a content item, the HTML for the last content item is copied and we need to increment attributes that reference the contents list,
	// so things like name="parts[1].contents[1].itemNumber" become name="parts[1].contents[2].itemNumber"
	// NOTE: tt-hint are inputs dynamically inserted by Twitter Bootstrap Typeahead
	$parentElement.find('input, select, textarea').each(function() {			
		var $this = $(this);		
		var fieldListName = $this.data('list-name');
		var fieldName = $this.attr('name');
		
		if(fieldName && fieldListName) {
			var updatedFieldName = fieldName.replace(fieldNameRegEx, listName + '[' + listIndex + ']');
			$this.attr('name', updatedFieldName);
			// the ids are the same as the name without brackets, this is just how the Spring form tags render the HTML
			$this.attr('id', updatedFieldName.replace(/\[/g, '').replace(/\]/g, ''));
			
			// if the field maps to the list we're updating, update it's list index attribute 
			// e.g. we're adding an item to the parts list so fields like parts[0].partNumber need data-list-index incremented
			if(fieldListName == listName) {
				$this.attr('data-list-index', listIndex);
			} else {
				// the field maps to a sub-list, e.g. parts[0].contents[0].itemNumber so increment the data-list-name attribute
				$this.attr('data-list-name', fieldListName.replace(fieldNameRegEx, listName + '[' + listIndex + ']'));
			}
			
			var $hiddenBootstrapSelectInput = $this.next('input[name="_' + fieldName + '"]');

			if($hiddenBootstrapSelectInput.length) {
				$hiddenBootstrapSelectInput.attr('name', '_' + updatedFieldName);
			}			
		}
		
		if($this.is(':checkbox')) {
			// checkboxes created by the Spring tag have a generated hidden field and a special id with a "1" on the end 
			// hidden field also needs to be copied, id needs to be updated correctly and values kept the way Spring expects
			$this.attr('id', $this.attr('id') + '1');
			$this.next('input[type="hidden"]').attr('name', '_' + updatedFieldName);
		} else if(newFields && !$this.hasClass('keepValueOnRepeat')) {
			$this.val('');
		}				
	});
}

function deletePart($part) {
	
	confirmDialog.show({
		title: 'Delete?',
		message: 'Are you sure you want to delete this part?',
		closeButtonLabel: 'No',
		buttons: {
			'Yes': function() {	
				removePart($part);
				saveReadingRoomObject();
				confirmDialog.hide();				
			}
		}
	});		
}

function deleteContentItem($item) {
	confirmDialog.show({
		title: 'Delete?',
		message: 'Are you sure you want to delete this content item?',
		closeButtonLabel: 'No',
		buttons: {
			'Yes': function() {
				removeContentItem($item);
				saveReadingRoomObject();
				confirmDialog.hide();			
			}
		}
	});
}

function removePart($part) {	

	var $partsSection = $('div.formSection.objectParts');
	
	// CK editors are attached to hidden textareas with ids like parts1.description Since we're removing a part, 
	// the ids & names on the textareas need to be updated so the indexes are correct, e.g. if we're removing part 1, 
	// then part2.description becomes parts1.description. The CK editors for these fields are tied to the textarea 
	// by id so we remove all the old CK editors, update the indexes, then re-create the CK Editors
	removeCkEditors($partsSection);
	
	if($('div.objectPart').length > 1) {
		$part.remove();
	} else {
		// any textareas under the part will be removed, so we need to also destory any ck editors under the par
		// the same is true of any text areas under the content items for the part, they will be gone, so the ck editors need to be gone
		// indexes get updated after stuff is removed, should init ckeditors at that time becuase they need to correlate with teh correct id
		// so order is 1) remove textareas & ck editors, update indexes, initialize ckeditors
		clearElementInputs($part);
		$('#hasParts').val('false');			
		$('h3.objectParts').find('span.glyphicon').addClass('hidden glyphicon-triangle-right').removeClass('glyphicon-triangle-bottom');
		$('div.objectParts').hide();
		$part.attr('data-part-id', '');
		$part.find('input.hasContent').val('false');		
		setPartType('UNKNOWN', $part);
		var $contentItems = $part.find('div.contentItem');
		
		for(var i = $contentItems.length - 1; i >= 0; i--) {
			removeContentItem($($contentItems[i]));
		}			
	}	
	
	$('div.objectPart').each(function(listIndex) {
		var $this = $(this);
		setFieldListIndex($(this), 'parts', listIndex);
		$this.attr('data-part-index', listIndex);
	});
	
	initCkEditors($partsSection);
}

function removeContentItem($item) {
	var $itemContainer = $item.parents('div.contentItemContainer');
	var isArchival = $itemContainer.hasClass('archivalContent');
	
	removeCkEditors($itemContainer);
	
	if($itemContainer.find('div.contentItem').length > 1) {
		$item.next('hr').remove();
		$item.remove();		
	} else {
		clearElementInputs($item);
		$item.attr('data-item-id', '');
		$itemContainer.find('input.hasContent, input.hasArchivalContent').val('false');
		$itemContainer.find('button.addContentItem').removeClass('disabled');
		$item.find('h3 span.contentLabel').text('Item Description');		
		setContentToggle($item.parents('div.contentSection'), false);
	}	
	
	$itemContainer.find('div.contentItem').each(function(listIndex) {
		var listName = isArchival ? 'archivalData.content' : 'parts[' + $itemContainer.attr('data-part-index') + '].contents';
		setFieldListIndex($(this), listName, listIndex);
	});
	
	initCkEditors($itemContainer);
}

function clearElementInputs($element) {
	$element.find('input, select, textarea').val('');
	$element.find('input[type="checkbox"]').attr('checked', false);	
	$element.find('select option').removeAttr('selected');
	removeGroupBoxes($element);
	initGroupBoxes($element);	
}

function disableTypeSpecificFields() {
	// for each part, disable any type specific fields based on the type of part	
	$('#readingRoomObjectForm').find('div.objectPart').each(function() {
		var $this = $(this);
		var partType = $this.find('select.partType').val();
		
		// for now, treating unknown part types as manuscript
		if(partType == 'MANUSCRIPT' || partType == 'UNKNOWN') {
			$this.find('.hideForManuscript').attr('disabled', 'disabled');				
			$this.find('.hideForPrinted').not('.alwaysDisabled').removeAttr('disabled');				
		} else if(partType == 'PRINTED') {
			$this.find('.hideForPrinted').attr('disabled', 'disabled');
			$this.find('.hideForManuscript').not('.alwaysDisabled').removeAttr('disabled');
		} else {
			$this.find('.hideForManuscript, .hideForPrinted').not('.alwaysDisabled').removeAttr('disabled');
		}		
	});
}

function saveReadingRoomObject(close, showSuccess) {			
	save(close, showSuccess);
}

function removeImportedValue($removeIcon) {
	confirmDialog.show({
		title: 'Delete?',
		message: 'Are you sure you want to delete the imported value?',
		closeButtonLabel: 'No',
		buttons: {
			'Yes': function() {				
				$removeIcon.prevAll('.importedValue').remove();
				$removeIcon.remove();
				confirmDialog.hide();
			}
		}
	});
}

// note this is called from both beforeunload and unload because unload doesn't work in Chrome beforeunload doesn't work in any other browser
function setLocked(locked) {
	var objectId = $('#readingRoomObjectId').val();
	
	if(objectId) {
		$.ajax({
			url: contextPath + '/catalogDatabase/setLocked/' + objectId + '/' + locked,
			method: 'POST',
			async: false,
			contentType: 'text/plain;charset=UTF-8',
			success: function() {
				recordLocked = false;
			},
			error: function(response) {
				messageDialog.show({
					message: response.responseText + ' You will now be re-directed to the Catalog Database after closing this dialog.',
					closeButtonLabel: 'OK',
					closeButtonFunction: function() {
						window.onbeforeunload = null;
						window.location.replace(contextPath + '/catalogDatabase');
					}
				});
			}
		});
	}
}