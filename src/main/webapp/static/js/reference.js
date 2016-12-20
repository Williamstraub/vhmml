var pagingControl;
var isReferenceAdmin;
var citationDetailsDialog = new Dialog();
var confirmDialog = new Dialog();
var deleteByCollectionDialog = new Dialog();

// if the URL to get to this page had search parameters there will be a search result on the request that gets set using inline 
// javascript on the page
// these variables are initialized with inline javascript on the page using JSP EL Expression
var initialSearchResult;
var selectedEntry;
var selectedSearchType;
var initialSortBy;
	
$(function() {		
	pagingControl = PagingControl.get('referenceTable');
	
	$('#searchBy li a').click(function() {
		var $this = $(this);
		var selectedSearchBy = $this.text();
		$('#searchBy span.selected').text(selectedSearchBy);
		$('#searchType').val($this.attr('data-value'));
	});
	
	$('#searchButton').click(function(e) {
		newSearch();
		e.preventDefault();
	});
	
	$('#clearSearchButton').click(function() {
		$('#searchForm').trigger('reset');
		newSearch();
	});
	
	if(initialSortBy) {
		pagingControl.setSort(initialSortBy);
	}
	
	if(initialSearchResult) {
		renderSearchResults(initialSearchResult);			
	}		
	
	if(selectedEntry) {
		showEntry(selectedEntry);
	}
	
	if(selectedSearchType) {
		$('#searchBy li a.' + selectedSearchType).click();	
	}
	
	initCorrectionsDialog();
});

function newSearch() {
	pagingControl.setPage(0);
	pagingControl.setSort(initialSortBy);
	referenceSearch();
}

function sort(sortBy) {
	pagingControl.setSort(sortBy);
	referenceSearch();
}

function referenceSearch() {
	var pagingData = pagingControl.getPagingData();
	var searchText = $('#searchString').val();
	
	// if they don't enter any searchText we just bring back everything, but if they enter some text it has to be at least 2 characters
	if(searchText && searchText.length < 2) {
		$('#referenceTable, #noResultsMessage').hide();
		$('#invalidSearchMessage').show();
	} else {
		var searchParams = {
			searchString: searchText,
			searchType: $('#searchType').val()
		};
		
		var data = $.extend({}, pagingData, searchParams);
		
		$.ajax({
			url: contextPath + '/reference/search',
			data: data,
			beforeSend: function() {
				showSpinner({element: $('#referenceTable')});
			},
			success: function(searchResult) {
				renderSearchResults(searchResult);
				var ev = document.createEvent('HTMLEvents');
				ev.initEvent('ZoteroItemUpdated', true, true);
				document.dispatchEvent(ev);
			}
		});
	}	
}

function renderSearchResults(searchResult) {
	pagingControl.updatePaging(searchResult);
	
	if(searchResult && searchResult.items && searchResult.items.length) {			
		var html = getTableHeaderHtml(); 
		var items = searchResult.items;
		
		for(var i = 0; i < items.length; i++) {
			var item = items[i];
			var icon = getIcon(item);
			var titleHtml = '<span class="glyphicon glyphicon-' + icon + '"/>&nbsp;&nbsp;' + item.displayTitle;
			var date = item.date && item.date != 'null' ? item.date : '';
			
			html += '<tr id="' + item.id +'" data-title="' + item.displayTitle + '">';
				
				html += '<td class="col-md-2">' + (item.author ? item.author : '' );
				
				if(item.highlightFields) {				
					html += '<br/><span class="table-row-caption">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
					html += item.highlightFields[Object.keys(item.highlightFields)[0]];					
					html += getTagsHtml(item);					
					html += '</span><br/>';
				}
				
				html += item.coinsData;
				html += '</td>';
				
				html += '<td class="col-md-6">';
				html += '<a href="javascript:viewEntry(\'' + item.id + '\');">' + titleHtml + '</a>';				
				html += '</td>';
				html += '<td class="col-md-2">' + date + '</td>';
				
				if(isReferenceAdmin) {
					html += '<td class="col-md-2 actions text-right">';									
						html += '<a onclick="javascript:removeEntry(this);" class="btn btn-danger" role="button">Delete</a>';
					html += '</td>';
				}				
			html += '</tr>';			
		}
							
		$('#noResultsMessage, #invalidSearchMessage').hide();
		$('#referenceTable').html(html);
		$('#referenceTable').css('text-align', 'left').show();
	} else {
		$('#referenceTable, #invalidSearchMessage').hide();
		$('#noResultsMessage').show();
	}
};

function getTagsHtml(item) {
	var html = '';
	
	if(item.tags && item.tags.length) {
		html += '<b>  &middot;  ';
		
		for(var i = 0; i < item.tags.length; i++) {
			if(i > 0) {
				html += ', ';
			}
			html += item.tags[i].toUpperCase();
		}
		
		html += '</b>';
	}
	
	return html;
}

function getTableHeaderHtml() {
	var pagingData = pagingControl.getPagingData();
	var sortIconName = pagingData.direction === 'DESC' ? 'down' : 'up';
	var sortIcon = '&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-' + sortIconName + '"/>';
	
	var html = 
		'<caption class="small text-left">' +
			'<em>Click a title to see citation information</em>' +
			'<a href="javascript:showCorrectionsDialog();" class="right"><img src="' + contextPath + '/static/img/glyphicons-conversation2.png" width="20" alt="" />&nbsp;Corrections or additions?</a>' +
		'</caption>' +
		
		'<tr>' +
			'<th class="col-md-4"><a href="javascript:sort(\'author\');">AUTHOR(S)' + (pagingData.sortBy === 'author' ? sortIcon : '') + '</a></th>' +
			'<th class="col-md-4"><a href="javascript:sort(\'displayTitle\');">TITLE' + (pagingData.sortBy === 'displayTitle' ? sortIcon : '') + '</a></th>' +
			'<th class="col-md-2"><a href="javascript:sort(\'date\');">DATE' + (pagingData.sortBy === 'date' ? sortIcon : '') + '</a></th>';
			
			if(isReferenceAdmin) {
				html += '<th class="col-md-2 text-right">ACTIONS</th>';
			}
				
		html += '</tr>';
		
	return html;
}

function viewEntry(id) {
	
	$.ajax({
		url: contextPath + '/reference/getEntry?entryId=' + id,			
		complete: function(response) {
			var entry = JSON.parse(response.responseText);
			showEntry(entry);
		}
	});
}

function showEntry(entry) {
	var icon = getIcon(entry);
	var title = '<span class="glyphicon glyphicon-' + icon + '"/>&nbsp;&nbsp;' + entry.type;			
	
	citationDetailsDialog.show({
		title: title,
		body: getEntryHtml(entry),
		moveable: true
	});
	
	initEmailEntryLink(entry);
}

function getIcon(entry) {
	var icon = 'file';	
		
	switch(entry.itemType) {
		case 'BOOK':
			icon = 'book';
			break;
		case 'BLOG_POST':
		case 'WEBPAGE':
			icon = 'globe';
			break;
		case 'VIDEO_RECORDING':
			icon = 'facetime-video';
			break;
		case 'JOURNAL_ARTICLE':
		case 'MAGAZINE_ARTICLE':
			icon = 'file';
			break;
		case 'BOOK_SECTION':
			icon = 'bookmark';
			break;
		case 'THESIS':
			icon = 'education';
			break;
		case 'CONFERENCE_PAPER':
			icon = 'duplicate';
			break;
		case 'ENCYCLOPEDIA_ARTICLE':
			icon = 'list';
			break;
		default:
			icon = 'file';
	}
	
	return icon;
}

function getEntryHtml(entry) {
	var html = '<table id="referenceEntryTable" class="table table-striped">';	
	var displayFields = entry.displayFields;
	
	for(var i = 0; i < displayFields.length; i++) {
		var field = displayFields[i];
		html += getAttributeHtml(field.label, entry[field.name]);
	}
	
	// available url & perm link are always at the end
	var availableLink = entry.url ? '<a class="availableLink" href="' + entry.url + '" target="_blank">' + entry.url + '</a>' : '';
	html += getAttributeHtml('Available', availableLink, 'dialog-link');
	
//	comment out perm links until they are true perm links that won't change even if items are re-imported from Zotero
//	var permUrl = scheme + '://' + serverName + ':' + serverPort + contextPath + '/reference/entry/' + entry.id;
//	var permLink = '<a href="' + permUrl + '">' + permUrl + '</a>';
//	html += getAttributeHtml('URL for this Reference citation', permLink, 'dialog-link');
	
	html += '</table>'; 	
	html += '<a class="emailRefLink"><span class="glyphicon glyphicon-envelope emailRefIcon popover-trigger pointer"></span>';
	html += '<label class="emailRefIconLabel popover-trigger pointer">Email this to yourself</label></a>';
	html += '<span class="emailResponse">';
	html += 	'<span class="glyphicon"></span>';
	html += 	'<label></label>';
	html += '</span>';
	
	return html;
}

function getAttributeHtml(label, attributeValue, rowCssClass) {		
	var value = attributeValue ? attributeValue : '';		
	var rowClass = rowCssClass ? rowCssClass : '';
	return '<tr class="' + rowClass + (attributeValue ? '' : ' disabled') + '"><td><label>' + label + ':&nbsp;</label>' + value + '</td></tr>';		
}

function initEmailEntryLink(entry) {
	
	if(isAuthenticated) {
		$('a.emailRefLink').click(function() {
			emailEntryToUser(entry);
		});
	} else {
		var $emailRefIcon = $('.emailRefIcon');
		
		$emailRefIcon.popover({
			trigger: 'click',
			html:true,
			content: '<input class="emailAddress" type="text" placeholder="email address"/><button class="btn home sendEmail">Send</button>'
		});
		
		$emailRefIcon.on('shown.bs.popover', function () {
			$('a.emailRefLink input.emailAddress').focus();
		});
		
		$('.emailRefIconLabel').click(function(e) {
			$emailRefIcon.click();
			return false;
		});
		
		var $modal = $('.modal');
		$modal.off('click', 'button.sendEmail').on('click', 'button.sendEmail', function() {
			emailEntryToUser(entry, $(this).prev('input.emailAddress').val());
		});
		
		$modal.off('keypress', 'input.emailAddress').on('keypress', 'input.emailAddress', function(e) {
			if(e.which == 13) {
				emailEntryToUser(entry, $(this).val());
			}
		});
	}	
}

function emailEntryToUser(entry, emailAddress) {
	var spinner = undefined;
	
	$.ajax({
		url: contextPath + '/reference/emailEntryToUser',
		method: 'POST',
		data: {
			entryId: entry.id,
			emailAddress: emailAddress
		},
		beforeSend: function() {
			$('.emailRefIcon').next('.popover').remove();
			spinner = showSpinner({
				element: $('a.emailRefLink'),
				css: {'display': 'inline'},
				imageCss: {'height': '18px'} 
			});
		},
		complete: function(response, textStatus) {
			spinner.hide();
			initEmailEntryLink(entry);
			showEmailResponse(response.responseText, textStatus === 'success');			
		}
	});
}

function showEmailResponse(message, success) {
	var icon = success ? 'ok' : 'exclamation-sign';
	var cssClass = success ? 'success' : 'error';
	var $emailResponseElement = $('span.emailResponse');
	$emailResponseElement.addClass(cssClass);
	$emailResponseElement.children('span.glyphicon').attr('class', 'glyphicon glyphicon-' + icon);
	$emailResponseElement.children('label').text(message);
	$emailResponseElement.show();
	setTimeout(function() {
		$emailResponseElement.fadeOut();
	}, 3000);
}