var selectedGroupId;
var selectedCollectionKey;

$(function() {
	var $modalBody = $('#deleteByGroupDialog .modal-body');
	
	$('#deleteByCollectionButton').click(function() {		
		confirmDialog.show({
			title: 'Delete Citation?',
			message: 'Are you sure you want to delete all of the citations for the selected collection?',
			buttons: {
				'Delete': function() {
					$.ajax({
						url: contextPath + '/reference/admin/delete/group/' + selectedGroupId + '/collection/' + selectedCollectionKey,
						method: 'DELETE',
						complete: function(response, textStatus) {
			    			Messages.showResponseMessage(response, $modalBody);
			    			
							if(textStatus === 'success') {
								$('#referenceTree').jstree('delete_node', '#' + selectedCollectionKey);
								referenceSearch();
							}
							
							confirmDialog.hide();
						}
					});
				}
			}
		});				
	});
});

// this is the callback function that gets executed when an item in the reference entry tree
// is selected on the delete items by Zotero group collection dialog
function selectReferenceItem(node, selected, event) {
	var $selectedNode = $('#' + selected.node.id);			
	var itemCount = $selectedNode.attr('data-item-count');
	
	if($selectedNode.hasClass('collection') && itemCount > 0) {				
		selectedGroupId = $('#' + selected.node.id).parents('li.group').attr('id');
		selectedCollectionKey = selected.node.id;
		$('#deleteByCollectionButton').removeClass('disabled');
	} else {
		selectedGroupId = null;
		selectedCollectionKey = null;
		$('#deleteByCollectionButton').addClass('disabled');
	}			
}

function removeEntry(button) {
	var $row = $(button).parents('tr');
	var id = $row.attr('id');
	
	confirmDialog.show({
		title: 'Delete Citation?',
		message: 'Are you sure you want to delete the selected citation?',
		buttons: {
			'Delete': function() {
				$.ajax({
					url: contextPath + '/reference/admin/delete/' + id,
					method: 'DELETE',
					complete: function(response, textStatus) {
		    			Messages.showResponseMessage(response);
						if(textStatus === 'success') {
							$row.remove();
						}						
						confirmDialog.hide();
					}
				});
			}
		},
		success: function() {
			$row.remove();
		}
	});
}