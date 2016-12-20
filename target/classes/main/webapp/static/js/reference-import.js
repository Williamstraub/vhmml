var selectedGroupId;
var selectedCollectionKey;
var pagingControls;

$(function() {		
	pagingControls = PagingControl.get('itemsTable');
	
	$('#importCollection').click(function() {
		
		$.ajax({
			url: contextPath + '/reference/admin/importCollection',
			beforeSend: function() {		
				Messages.removeAll();
				showSpinner({element: $('#itemsTable')});
			},
			data: {
				groupId: selectedGroupId,
				collectionKey: selectedCollectionKey
			},
			complete: function(response) {					
				handleImportResponse(response);				
			}
		});
	});
	
	$('#itemsTable').on('click', 'span.importItem', function() {
		var itemKey = $(this).attr('id');
		
		$.ajax({
			url: contextPath + '/reference/admin/importItem',
			method: 'POST',
			data: {
				groupId: selectedGroupId,
				itemKey: itemKey
			},
			beforeSend: function() {		
				Messages.removeAll();					
			},				
			complete: function(response) {					
				handleImportResponse(response);					
			}
		});
	});
});

function selectReferenceItem(node, selected, event) {
	var $selectedNode = $('#' + selected.node.id);
	pagingControls.hide();
	
	if($selectedNode.hasClass('collection')) {
		pagingControls.setPage(0);
		pagingControls.hide();
		selectedGroupId = $('#' + selected.node.id).parents('li.group').attr('id');
		selectedCollectionKey = selected.node.id;				
		getCollectionItems();				
	} else {
		selectedGroupId = null;
		selectedCollectionKey = null;
		$('#importCollection').addClass('disabled');
		$('#itemsTable').hide();
	}			
}

function handleImportResponse(response) {
	var importResult = JSON.parse(response.responseText);	
	
	if(importResult.success) {		
		if(importResult.collectionItems) {
			refreshItemsTable(importResult.collectionItems);	
		} else if(importResult.item) {
			$('#' + importResult.item.key).fadeOut().attr('class', 'glyphicon glyphicon-ok success').fadeIn();
		}		
	} 
	
	if(importResult.message) {
		Messages.addMessage({
			message: importResult.message,
			severity: importResult.success ? 'SUCCESS' : 'ERROR'
		});
	}
}

function getCollectionItems() {	
	var pagingData = pagingControls.getPagingData();
	var data = $.extend({groupId: selectedGroupId, collectionKey: selectedCollectionKey}, pagingData);
	
	$.ajax({
		url: contextPath + '/reference/admin/getCollectionItems',
		data: data,
		beforeSend: function() {
			var $itemsTable = $('#itemsTable');
			showSpinner({element: $itemsTable});
			$itemsTable.show();
		},
		success: function(searchResult) {
			pagingControls.updatePaging(searchResult);
			refreshItemsTable(searchResult.zoteroItems);				
			$('#importCollection').removeClass('disabled');
			$('#itemsTable').show();
		}
	});
}

function refreshItemsTable(items) {		
	
	if(items && items.length) {
		var itemsToShow = pagingControls.getPagingData().size;
		var html = getTableHeaderHtml();
		
		for(var i = 0; i < items.length && i < itemsToShow; i++) {
			
			var item = items[i];
			html += '<tr>';
//				just showing regular title for now so we can sort by it
//				var title = item.shortTitle ? item.shortTitle : item.title;								
				var title = item.title;								
				html += '<td>' + title + '</td>';
			
			if(item.creators && item.creators.length) {							
				var creator = item.creators[0];
				
				if(!creator.lastName) {
					console.log(creator);
				}
				
				var name = creator.lastName ? creator.lastName : creator.name;
				html += '<td>' + (name ? name : '&nbsp;') + '</td>';
				html += '<td class="text-center">' + (item.imported ? '<span class="glyphicon glyphicon-ok success"></span>': '<span id="' + item.key + '" class="glyphicon glyphicon-download importItem"></span>') + '</td>';					
			}
			
			html += '</tr>';
		}
		
		$('#itemsTable').css('text-align', 'left').html(html);
	}
}

function sort(sortBy) {
	pagingControls.setPage(0);
	pagingControls.setSort(sortBy);
	getCollectionItems();
}

function getTableHeaderHtml() {
	var pagingData = pagingControls.getPagingData();
	var sortIconName = pagingData.direction === 'DESC' ? 'down' : 'up';
	var sortIcon = '&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-' + sortIconName + '"/>';
	
	var html = '<tr><th><a href="javascript:sort(\'title\');">Title ' + (pagingData.sortBy === 'title' ? sortIcon : '') + '</a></th>';
	html += '<th><a href="javascript:sort(\'creator\');">Creator ' + (pagingData.sortBy === 'creator' ? sortIcon : '') + '</a></th>';
	html += '<th>Imported</th></tr>';
		
	return html;
}